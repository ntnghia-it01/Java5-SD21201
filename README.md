# Java5 — Docker và triển khai VPS qua Git

Ứng dụng Spring Boot 4.1.1, Java 21, Maven Wrapper, Thymeleaf và SQL Server.
Giữ nguyên cách đóng gói WAR: Dockerfile multi-stage tự build WAR và chạy bằng `java -jar`.
VPS không cần cài Java/Maven. Không dùng Docker Hub cho image ứng dụng.

Luồng: **Local → Git public → VPS clone → Compose build → Nginx → HTTPS**.

## 1. File đã chuẩn bị

| File | Mục đích |
|---|---|
| `Dockerfile` | Build mã nguồn, chạy Java bằng user không phải root |
| `compose.yaml` | SQL Server → tạo DB → Spring Boot; lưu DB và ảnh bằng volume |
| `.env.example` | Mẫu biến môi trường, không chứa mật khẩu thật |
| `.gitignore` | Loại `.env`, ảnh upload, dữ liệu backup và output build khỏi Git |
| `.dockerignore` | Chỉ cho phép mã nguồn, POM và Maven Wrapper vào build context |
| `deploy/init-db.sql` / `init-db.sh` | Kiểm tra `DB_NAME`, tạo database theo `.env` nếu chưa tồn tại |
| `deploy/nginx.conf` | Reverse proxy đến `127.0.0.1:8080` |

`application.properties` nhận `DB_URL`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD` từ môi trường.
Tên database khai báo một lần trong `.env`: `DB_NAME=ecommerce_db`. Compose dùng giá trị này cho cả JDBC URL và script tạo database; khi chạy Eclipse, `DB_NAME` mặc định là `ecommerce_db` nếu chưa đặt.
Tên database bắt đầu bằng chữ hoặc `_`, chỉ gồm chữ ASCII, số và `_`, tối đa 128 ký tự. Đổi `DB_NAME` sẽ chọn/tạo database khác, không đổi tên hay di chuyển dữ liệu database cũ.
Nếu chạy lại bằng Eclipse, khai báo `DB_PASSWORD` trong Run Configuration → Environment.
Docker Compose tự truyền các biến này, không cần sửa file Java.

## 2. Chuẩn bị repository public

**Repository cũ có mật khẩu database trong lịch sử commit.** Thay mật khẩu ở SQL Server đã sử dụng mật khẩu đó trước khi public. Sửa file hiện tại không xóa mật khẩu khỏi lịch sử; đổi `.env` cũng không đổi mật khẩu trên database cũ.

Cách đơn giản để public bài workshop: dùng bản mã nguồn sạch không chứa `.git`, tạo repository GitHub **trống** (không khởi tạo README), rồi chạy tại thư mục mã nguồn sạch:

```bash
# LOCAL — chỉ dùng git init khi bắt đầu repository mới.
git init -b main
git add .
git diff --cached --stat
git diff --cached
```

- `git init -b main`: tạo lịch sử Git mới, nhánh `main`.
- `git add .`: đưa file không bị ignore vào staging.
- Hai lệnh `diff`: kiểm tra danh sách và nội dung chuẩn bị commit. Không được có `.env`, ảnh thật, file backup hoặc mật khẩu.

```bash
git commit -m "Configure Java5 Docker deployment"
git remote add origin GIT_REPO_URL
git push -u origin main
git tag v1.0.0
git push origin v1.0.0
```

Thay `GIT_REPO_URL` bằng URL repository của bạn.
`push -u` đưa nhánh lên Git và đặt upstream; `tag` đánh dấu đúng phiên bản VPS sẽ clone.
Nếu dùng repository cũ, không chạy lại `git init` và không public lịch sử chưa xử lý. Có thể dùng quy trình xóa secret khỏi lịch sử của GitHub, nhưng phải thay mật khẩu trước.

## 3. Thử Docker trên máy local

SQL Server Linux được Microsoft hỗ trợ trên **x86_64**. VPS dùng Ubuntu 24.04 x86_64, tối thiểu đề xuất 2 vCPU / RAM 4 GB / SSD 30 GB.
Trên Mac Apple Silicon, service SQL khai báo `linux/amd64` và cần giả lập của Docker Desktop; việc giả lập không thuộc cấu hình SQL Server được Microsoft hỗ trợ. VPS x86_64 không cần giả lập.

```bash
cp .env.example .env
chmod 600 .env
nano .env
```

`cp` tạo cấu hình riêng; `chmod 600` chỉ chủ file đọc/ghi; `nano` mở file để đặt mật khẩu riêng. Không ghi đè `.env` đang có dữ liệu triển khai.
Đặt `MSSQL_SA_PASSWORD` dài tối thiểu 16 ký tự, có chữ hoa, thường, số và ký tự đặc biệt. Giữ nháy đơn nếu có `$`; tránh ký tự nháy đơn trong mật khẩu.

```bash
docker compose config --quiet
docker compose pull sqlserver db-init
docker compose build app
docker compose up -d --no-build
docker compose ps
docker compose logs --tail=100 app
curl -f http://127.0.0.1:8080/login
```

- `config --quiet`: kiểm tra cấu hình và biến bắt buộc, không in mật khẩu.
- `pull sqlserver db-init`: tải image SQL Server và công cụ khởi tạo từ Microsoft; hai service dùng cùng image.
- `build app`: chạy Maven trong build stage, tạo image ứng dụng từ source hiện tại.
- `up -d --no-build`: chạy nền bằng image đã build. App chỉ chạy khi SQL healthy và `db-init` thoát thành công.
- `ps`: xem trạng thái; `db-init` ở `Exited (0)` là bình thường.
- `logs --tail=100 app`: xem 100 dòng log gần nhất.
- `curl -f`: gọi trang đăng nhập, trả mã lỗi shell nếu HTTP lỗi. Project là ứng dụng HTML, không phải API REST của slide minh họa.

Container chỉ bind port app vào loopback. Trên cùng máy truy cập `http://localhost:8080/login`, `/register` hoặc `/`.
Không có tài khoản admin mặc định và không tự thêm dữ liệu mẫu.
Dữ liệu DB/ảnh có sẵn ở local không tự đi theo Git hay image.

## 4. Chuẩn bị VPS

Cài Docker Engine và Compose plugin theo hướng dẫn chính thức:
<https://docs.docker.com/engine/install/ubuntu/>

Mở cổng SSH thực tế, TCP 80 và 443 trong firewall của nhà cung cấp. Domain trỏ DNS A về IP VPS; bỏ AAAA nếu chưa có IPv6 đúng. Với Cloudflare, dùng DNS only khi thực hành cấp chứng chỉ.

```bash
# LOCAL
ssh VPS_USER@VPS_IP
```

`ssh` đăng nhập máy chủ; thay user/IP thật. Các lệnh sau chạy **trên VPS**:

```bash
uname -m
free -h
df -h
sudo apt update
sudo apt install -y git
sudo docker compose version
```

- `uname -m`: phải là `x86_64` cho cấu hình SQL Server được hỗ trợ.
- `free -h` / `df -h`: kiểm tra RAM và đĩa theo đơn vị dễ đọc.
- `apt update`: cập nhật danh sách gói; `install -y git`: cài Git.
- `sudo`: chạy với quyền quản trị; bài này dùng nhất quán cho Docker trên VPS.
- `compose version`: xác nhận Compose plugin đã cài, không dùng lệnh cũ `docker-compose`.

## 5. Clone và chạy lần đầu trên VPS

```bash
mkdir -p ~/java5-deploy
cd ~/java5-deploy
git clone --branch v1.0.0 GIT_REPO_URL app
cd app
cp .env.example .env
chmod 600 .env
nano .env
```

- `mkdir -p`: tạo thư mục nếu chưa có; `cd`: chuyển thư mục làm việc.
- `clone --branch v1.0.0`: tải mã nguồn đúng tag vào `app`. Git ở detached HEAD khi chọn tag; phù hợp máy triển khai chỉ đọc source.
- `.env` nằm trong thư mục clone nhưng bị Git ignore và không vào Docker context. Không chạy `git clean -fdx` vì có thể xóa file này.
- Trong `.env`, đặt `APP_VERSION=1.0.0`, `DB_NAME=ecommerce_db` và mật khẩu SQL **riêng cho VPS**. File không được commit.

```bash
sudo docker compose config --quiet
sudo docker compose pull sqlserver db-init
sudo docker compose build app
sudo docker compose up -d --no-build
sudo docker compose ps
sudo docker compose logs --tail=100 app
curl -f http://127.0.0.1:8080/login
```

Ý nghĩa giống các lệnh local ở mục 3. Tất cả chạy trong `~/java5-deploy/app`.
Kết quả cần đạt: SQL Server `healthy`, `db-init` thoát 0, app `healthy`, trang `/login` trả HTML thành công.
Nếu build lỗi, dừng tại đó và đọc log; không thay app đang chạy bằng bản build thất bại.
`db-init` chỉ tạo database; Hibernate `update` tạo bảng sau khi app khởi động.

## 6. Nginx, domain và HTTPS

```bash
sudo apt install -y nginx certbot python3-certbot-nginx
sudo systemctl enable --now nginx
sudo cp deploy/nginx.conf /etc/nginx/sites-available/java5
sudo nano /etc/nginx/sites-available/java5
```

`install` cài reverse proxy và công cụ HTTPS; `enable --now` khởi động Nginx và bật tự chạy khi reboot.
`cp` chép mẫu cấu hình; `nano` thay `java5.example.com` bằng domain thật. VPS phải chưa có site khác cùng domain.
`client_max_body_size 500m` khớp giới hạn multipart hiện tại của Java5; có thể giảm cả hai nếu không cần upload lớn.

```bash
sudo ln -s /etc/nginx/sites-available/java5 /etc/nginx/sites-enabled/java5
sudo nginx -t
sudo systemctl reload nginx
getent ahostsv4 java5.example.com
```

- `ln -s`: kích hoạt site bằng symbolic link; chỉ chạy lần đầu. Nếu link đã tồn tại, kiểm tra và dùng lại.
- `nginx -t`: kiểm tra cú pháp; chỉ reload khi báo thành công.
- `reload`: nạp cấu hình mới.
- `getent ahostsv4`: xem IP DNS trả về, phải đúng VPS.

Từ local, thử `curl -i http://java5.example.com/login`. Nếu không thành công, xử lý DNS/firewall/HTTP trước khi cấp HTTPS.

```bash
# VPS — thay domain thật trước khi chạy.
sudo certbot --nginx -d java5.example.com --redirect
sudo certbot renew --dry-run
sudo systemctl enable --now certbot.timer
```

`--nginx` cấp và cài chứng chỉ vào Nginx; `-d` chọn domain; `--redirect` chuyển HTTP sang HTTPS.
`renew --dry-run` thử gia hạn; `certbot.timer` bật lịch kiểm tra gia hạn tự động cho cách cài APT.
Nhập email và đọc điều khoản khi Certbot hỏi.

```bash
# LOCAL
curl -I http://java5.example.com/login
curl -f https://java5.example.com/login
```

`-I` xem header chuyển hướng; `-f` kiểm tra trang HTTPS trả thành công.
Không mở cổng 1433/8080 ra Internet; Nginx gọi ứng dụng qua `127.0.0.1`.

## 7. Cập nhật và rollback

LOCAL, sau khi đã sửa và kiểm tra code:

```bash
git add .
git diff --cached
git commit -m "Update Java5 v1.0.1"
git push origin main
git tag v1.0.1
git push origin v1.0.1
```

VPS:

```bash
cd ~/java5-deploy/app
git status --short
git fetch --tags origin
git switch --detach v1.0.1
nano .env
```

`status` kiểm tra source không có thay đổi chưa lưu; không ép ghi đè nếu có.
`fetch --tags` tải tag; `switch --detach` chọn phiên bản, không sửa source trên VPS.
Trong `.env`, đổi **chỉ** `APP_VERSION=1.0.1`, giữ mật khẩu DB cũ.

```bash
sudo docker compose config --quiet
sudo docker compose build app && \
  sudo docker compose up -d --no-deps --no-build app
sudo docker compose logs --tail=100 app
curl -f http://127.0.0.1:8080/login
```

`&&` chỉ tạo lại app sau khi build thành công; `--no-deps` giữ DB đang chạy; `--no-build` dùng image vừa build.
Kiểm tra cả trang HTTPS, đăng nhập và dữ liệu cũ. Việc thay một container có thể gián đoạn ngắn.

Rollback: `git switch --detach v1.0.0`, sửa `.env` về `APP_VERSION=1.0.0`, sau đó:

```bash
sudo docker image inspect java5-app:1.0.0 > /dev/null
sudo docker compose up -d --no-deps --no-build app
```

Lệnh `inspect` xác nhận image cũ còn trên VPS. Nếu không có, chạy `sudo docker compose build app` từ tag cũ và chỉ `up` khi build thành công.
Rollback image không hoàn tác schema/dữ liệu. Không đổi SQL image hoặc database trong demo cập nhật ứng dụng.

## 8. Dữ liệu, ảnh upload và xử lý lỗi

```bash
sudo docker compose stop
sudo docker compose start
```

`stop` dừng nhưng giữ container; `start` chạy lại container cũ.

```bash
sudo docker compose down
sudo docker compose up -d --no-build
```

`down` xóa container/network nhưng giữ named volume; `up` tạo lại và dùng dữ liệu cũ.
**Không dùng `down -v` nếu còn cần DB hoặc ảnh: tùy chọn này xóa cả hai volume.** Volume không thay thế backup ngoài VPS.
Tên project cố định `java5`; thay tên project sẽ dùng tập volume khác.

```bash
sudo docker compose logs --tail=100 sqlserver db-init app
sudo docker compose ps -a
sudo docker stats --no-stream
sudo nginx -t
sudo tail -n 100 /var/log/nginx/error.log
```

- `logs`: xem SQL có khởi động được không, script tạo DB có lỗi không, app có kết nối được không.
- `ps -a`: bao gồm container đã thoát; `db-init Exited (0)` đúng thiết kế.
- `stats --no-stream`: chụp nhanh CPU/RAM.
- `nginx -t` / `tail`: kiểm tra cấu hình và 100 dòng lỗi reverse proxy.

| Lỗi | Cách xử lý |
|---|---|
| Missing password/version | Tạo `.env`, điền biến rồi chạy lại `config --quiet` |
| SQL unhealthy / login failed | Kiểm tra log, RAM, mật khẩu; `.env` mới không đổi mật khẩu volume cũ |
| Build Maven lỗi | Kiểm tra mạng đến Maven Central và log build; không cần Maven trên VPS |
| Git tag không tồn tại | Push tag từ local rồi `fetch --tags` |
| 502 | Thử `curl 127.0.0.1:8080/login`, đọc log app trước |
| Mất ảnh sau update | Kiểm tra volume `uploads`, đường dẫn `/app/images-upload`; không bake ảnh vào image |
| Không có dữ liệu local trên VPS | DB mới là bình thường; cần backup/restore riêng nếu muốn di chuyển dữ liệu |

## 9. Giới hạn của bài học

Cấu hình dùng SQL Server Developer (học tập/thử nghiệm), tài khoản `sa`, chứng chỉ database tự ký và Hibernate `update` để phù hợp project hiện tại. Dùng thật cần tài khoản DB riêng, migration, backup/restore và xác thực chứng chỉ.

**Code đăng nhập hiện tại lưu mật khẩu người dùng dạng nguyên văn và tin vai trò từ cookie do trình duyệt gửi lên.** Người dùng có thể sửa cookie để vượt kiểm tra admin. Chỉ dùng dữ liệu giả cho lab; cần sửa xác thực/phân quyền phía server và hash mật khẩu trước khi mở ứng dụng cho người dùng thật. Thiết lập Docker không sửa logic nghiệp vụ này.

Build Docker cố ý dùng `-DskipTests`: test `contextLoads` cần SQL Server. Build thành công không đồng nghĩa đã kiểm thử nghiệp vụ. Đọc biên bản kiểm tra bàn giao để biết phần đã được chạy thực tế.

## Tài liệu chính thức

- Docker Engine Ubuntu: https://docs.docker.com/engine/install/ubuntu/
- Docker Compose services/dependencies: https://docs.docker.com/reference/compose-file/services/
- Compose build: https://docs.docker.com/reference/compose-file/build/
- Spring Boot executable WAR: https://docs.spring.io/spring-boot/how-to/deployment/traditional-deployment.html
- SQL Server container: https://learn.microsoft.com/en-us/sql/linux/quickstart-install-connect-docker?view=sql-server-ver16
- Xử lý secret trong Git history: https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/removing-sensitive-data-from-a-repository

# Biên bản kiểm tra Docker Java5 — 15/10/2026

Đã thiết lập trực tiếp trong project Java5. Giữ Java 21, Spring Boot 4.1.1 và WAR.

## Đã kiểm tra thực tế

- Maven Wrapper `package -DskipTests`: build WAR thành công.
- Docker multi-stage build: thành công, image `java5-app:1.0.0` trên máy kiểm tra ARM64.
- Docker Compose config hợp lệ; thiếu mật khẩu trong `.env.example` sẽ yêu cầu cấu hình trước khi chạy.
- SQL Server 2022 amd64 chạy bằng giả lập trên Docker Desktop: healthy.
- Service `db-init`: tạo database `ecommerce_db` thành công, exit 0.
- Spring Boot: healthy, kết nối SQL Server và tạo bảng.
- GET `/`, `/login`, `/register`: HTTP 200, nội dung HTML.
- Đăng ký tài khoản giả, đăng nhập: thành công, redirect 302 và tạo cookie.
- Container app chạy UID 10001; ghi được vào volume upload và đọc file qua HTTP.
- Sau `compose down` rồi `up`: hai bản ghi thử và file upload thử còn nguyên.
- `.env`, `images-upload`, `target`, `.DS_Store` bị Git ignore; Docker context chỉ gồm source, POM, Maven Wrapper.
- Bản ZIP public không chứa `.git`, `.env`, ảnh upload, target hoặc mật khẩu DB cũ.

Các container và volume thử nghiệm thuộc project riêng `java5-docker-check` đã được dọn. Không xóa dữ liệu/volume có sẵn của bạn. Image build vẫn còn để dùng lại.

## Những phần chưa xác minh

- Chưa chạy trên VPS x86_64 thật, chưa cấp HTTPS cho domain thật.
- Chưa chạy toàn bộ bài kiểm thử JUnit; build image bỏ qua test phụ thuộc DB. Đã kiểm tra tích hợp HTTP và database thật như trên.
- Chưa kiểm tra toàn bộ nghiệp vụ sản phẩm/admin. Các thay đổi sẵn có ở controller, entity và template sản phẩm được giữ nguyên.
- Không thay đổi lịch sử Git cũ và không push lên GitHub.

## Trước khi public

Mật khẩu DB cũ xuất hiện trong một phiên bản file trong lịch sử Git. Đổi mật khẩu ở SQL Server đã dùng mật khẩu đó. Dùng bản ZIP sạch để tạo repository mới; không đẩy nguyên lịch sử Git cũ khi chưa xử lý secret.

File `.env` tại project gốc chứa mật khẩu riêng, quyền 600, không nằm trong ZIP. Bản ZIP có `.env.example` trống; mỗi VPS phải tự tạo `.env` và đặt mật khẩu riêng.

Ứng dụng hiện lưu mật khẩu người dùng dạng nguyên văn và tin cookie ROLE do trình duyệt gửi. Đây là lỗi của mã nghiệp vụ hiện tại, chưa được sửa trong phạm vi Docker. Dùng dữ liệu giả cho workshop; sửa xác thực/phân quyền và hash mật khẩu trước khi dùng cho người dùng thật.

Xem README.md trong ZIP để có từng lệnh VPS kèm giải thích, domain/HTTPS, cập nhật và rollback.

## Cập nhật DB_NAME qua môi trường

- `.env` và `.env.example` có `DB_NAME=ecommerce_db`.
- Compose dùng cùng `DB_NAME` cho JDBC URL và service khởi tạo.
- `init-db.sh` kiểm tra tên trước khi truyền biến `DB_NAME` cho sqlcmd.
- Đã chạy thật với tên `java5_env_check`: tạo database thành công, chạy lại không lỗi.
- Đã kiểm tra hai tên database qua Compose config và chặn tên chứa dấu nháy/ký tự đặc biệt hoặc quá dài.
- Maven package sau thay đổi: thành công (bỏ qua JUnit).
- Thử nghiệm dùng project/volume riêng `java5-env-check`, đã dọn sau kiểm tra.

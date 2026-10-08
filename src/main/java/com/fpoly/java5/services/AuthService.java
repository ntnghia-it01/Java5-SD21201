package com.fpoly.java5.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.java5.beans.LoginBean;
import com.fpoly.java5.beans.RegisterBean;
import com.fpoly.java5.entities.UserEntity;
import com.fpoly.java5.jpas.UserJPA;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {
	
	@Autowired
	UserJPA userJPA;
	
	@Autowired
	HttpServletResponse response;
	
	public void register(RegisterBean bean) {
//		Kiểm tra email có tồn tại không?
		Optional<UserEntity> userOptional = userJPA.checkEmailExist(bean.getEmail());
//		userOptional.isPresent() == true => UserEntity có giá trị  != null
//		userOptional.isPresent() == flase => UserEntity không có giá trị  == null 
		if(userOptional.isPresent()) {
			throw new RuntimeException("Email đã tồn tại!");
		}
		
//		Sau khi 2 TH trên thoả điều kiện (Không tồn tại)
//		Thực hiện insert dữ liệu vào db
		
//		Convert dữ liệu từ bean => Entity
//		Lưu vào db
		UserEntity userEntity = new UserEntity();
		userEntity.setName(bean.getName());
		userEntity.setEmail(bean.getEmail());
		userEntity.setPassword(bean.getPassword());
		userEntity.setPhone(bean.getPhone());
		userEntity.setAddress("");
		userEntity.setRole(1);
		userEntity.setActive(true);
		
		try {
//			Hàm này vừa thực hiện insert và update
//			Nếu entity có id => update
//			Nếu entity không có id => insert
			userJPA.save(userEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Có lỗi khi đăng ký tài khoản");
		}
	}
	
	public void login(LoginBean bean) {
		Optional<UserEntity> userOptional = userJPA.checkEmailExist(bean.getEmail());
		
		if(!userOptional.isPresent()) {
			throw new RuntimeException("Email hoặc mật khẩu không đúng!");
		}
		
		UserEntity userEntity = userOptional.get();
		
		if(!bean.getPassword().equals(userEntity.getPassword())) {
			throw new RuntimeException("Email hoặc mật khẩu không đúng!");
		}
		
//		Sau khi đăng nhập thành công
//		Lưu user id và role vào cookie
//		Dùng để kiểm tra quyền truy cập vào các trang của website
//		HttpServletResponse
//		Chuyển int => String 
		int maxAgeCookie = 60 * 60 * 24 * 7 ;// 7d
		Cookie userIdCookie = new Cookie("USER_ID", String.valueOf(userEntity.getId()));
		userIdCookie.setPath("/");
		// Không có dòng này path sẽ set cho url hiện tại (/login)
		// Khi đó chỉ có trang /login mới dùng được cookie này
		// Thời hạn của cookie => 7d 
		userIdCookie.setMaxAge(maxAgeCookie);
		
		Cookie roleCookie = new Cookie("ROLE", String.valueOf(userEntity.getRole()));
		roleCookie.setPath("/");
		roleCookie.setMaxAge(maxAgeCookie);
		
//		Thêm cookie vào browser
		response.addCookie(userIdCookie);
		response.addCookie(roleCookie);
	}
}

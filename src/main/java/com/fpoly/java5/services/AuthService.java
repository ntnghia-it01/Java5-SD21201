package com.fpoly.java5.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.java5.beans.LoginBean;
import com.fpoly.java5.beans.RegisterBean;
import com.fpoly.java5.entities.UserEntity;
import com.fpoly.java5.jpas.UserJPA;

@Service
public class AuthService {
	
	@Autowired
	UserJPA userJPA;
	
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
	}
}

package com.fpoly.java5.components;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fpoly.java5.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor{
	
//	Hàm này sẽ được kích hoạt sau khi user gửi request và trước khi controller thực thi
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
//		Lấy giá trị cookie user id và role => dùng request
//		Lấy url mà user muốn truy cập
//		Kiểm tra:
//		- Nếu user id hoặc role  == null 
//			=> dùng response chuyển về login return false
//		- Nếu role == 1 mà url /admin/**
//			=> dùng response chuyển về login return false
//		- Nếu role == 2 mà url /user/**
//			=> dùng response chuyển về login return false
		
		
		try {
			int userId = Integer.parseInt(Utils.getCookieByName("USER_ID", request));
			int role = Integer.parseInt(Utils.getCookieByName("ROLE", request));
			
//			/product
//			/admin/product-form
//			/login
			String path = request.getServletPath();
			
			if(path.startsWith("/admin/") && role != 2) {
				throw new RuntimeException();
			}
			
			if(path.startsWith("/user/") && role != 1) {
				throw new RuntimeException();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/login");
			return false;
		}
		
		
//		Trả về true => Sẽ đi tiếp đến controller
//		Trả về false => Sẽ bị chặn lại (Sai vai trò, chưa đăng nhập)
		return true;
	}
	
	
//	Hàm này sẽ được kích hoạt khi controller chạy xong và trước khi render giao diện 
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
//	Hàm này sẽ được kích hoạt khi html render thành công và trước khi hiển thị lên giao diện
//	người dùng  
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}

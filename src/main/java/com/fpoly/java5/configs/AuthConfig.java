package com.fpoly.java5.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fpoly.java5.components.AuthInterceptor;

@Configuration
public class AuthConfig implements WebMvcConfigurer{

	@Autowired
	AuthInterceptor interceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor)
			.addPathPatterns("/admin/**", "/user/**");
		
//		Giỏ hàng: user/cart
//		Đơn hàng của user: user/orders
	}
}

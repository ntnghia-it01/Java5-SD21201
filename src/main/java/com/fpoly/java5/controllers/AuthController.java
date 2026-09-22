package com.fpoly.java5.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fpoly.java5.beans.RegisterBean;

import jakarta.validation.Valid;

@Controller
public class AuthController {

	@GetMapping("/register")
	public String registerUI(Model model) {
		model.addAttribute("bean", new RegisterBean()); 
//		<=>@ModelAttribute(name = "bean") RegisterBean bean
		
		return "register.html";
	}
	
	@PostMapping("/register")
	public String handleRegister(Model model,
			@ModelAttribute(name = "bean") @Valid RegisterBean bean, // Hiển thị lỗi ở html 
			Errors errors) { // Dùng để lấy lỗi xử lý ở java 
		
//		errors.hasErrors() == false => Không lỗi
		if(!errors.hasErrors()) {
//			Không có lỗi
//			Xử lý sau khi form không có lỗi 
		}
		
		return "register.html";
	}
}

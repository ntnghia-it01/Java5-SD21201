package com.fpoly.java5.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fpoly.java5.entities.CartItemEntity;
import com.fpoly.java5.entities.UserEntity;
import com.fpoly.java5.jpas.UserJPA;
import com.fpoly.java5.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserCartController {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserJPA userJPA;
	
	@GetMapping("/user/cart")
	public String cartUI(Model model) {
//		Lấy user id ở cookie
		
		int userId = Integer.parseInt(Utils.getCookieByName("USER_ID", request));
		
		Optional<UserEntity> userOptional = userJPA.findById(userId);
		
		List<CartItemEntity> cartItemEntities = userOptional
				.get()
				.getCartEntity()
				.getCartItemEntities();
		
		model.addAttribute("carts", cartItemEntities);
		
//		HTML này chưa có 
		return "cart.html";
	}
}

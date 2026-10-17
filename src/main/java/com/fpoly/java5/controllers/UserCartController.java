package com.fpoly.java5.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fpoly.java5.entities.CartItemEntity;
import com.fpoly.java5.entities.UserEntity;
import com.fpoly.java5.jpas.UserJPA;
import com.fpoly.java5.services.CartServices;
import com.fpoly.java5.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserCartController {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserJPA userJPA;
	
	@Autowired
	private CartServices cartServices;
	
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
	
	@PostMapping("/user/add-to-cart")
	public String handleAddToCart(Model model,
			@RequestParam(name = "id") int prodId) {
		try {
			cartServices.addToCart(prodId);
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorCart", e.getMessage());
		}
		
		return "redirect:/user/cart";
	}
	
	@PostMapping("/user/increase-cart-item")
	public String handleIncreaseCartItem(Model model,
			@RequestParam(name = "id") int cartItemId) {
		try {
			cartServices.increaseCartItem(cartItemId);
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorCart", e.getMessage());
		}
		
		return "redirect:/user/cart";
	}
	
	@PostMapping("/user/decrease-cart-item")
	public String handleDecreaseCartItem(Model model,
			@RequestParam(name = "id") int cartItemId) {
		try {
			cartServices.decreaseCartItem(cartItemId);
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorCart", e.getMessage());
		}
		
		return "redirect:/user/cart";
	}
	
	@PostMapping("/user/remove-cart-item")
	public String handleRemoveCartItem(Model model,
			@RequestParam(name = "id") int cartItemId) {
		try {
			cartServices.removeCartItem(cartItemId);
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorCart", e.getMessage());
		}
		
		return "redirect:/user/cart";
	}
}

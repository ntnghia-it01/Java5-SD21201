package com.fpoly.java5.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fpoly.java5.beans.OrderCreateBean;
import com.fpoly.java5.entities.OrderEntity;
import com.fpoly.java5.services.OrderServices;
import com.fpoly.java5.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class UserOrderController {

//	Đặt hàng POST
//	Thông báo trạng thái đơn hàng GET (Đặt thành công hoặc thất bại)
//	Danh sách đơn hàng GET 
//	Chi tiết đơn hàng GET 
//	Huỷ đơn POST
//	Xác nhận hoàn thành POST
	
	@Autowired
	private OrderServices orderServices;
	
	@GetMapping("/user/order-success")
	public String orderSuccessUI(Model model,
			@RequestParam(name = "orderId") Optional<String> orderOptional) {
//		Dùng để hiển thị thông tin đơn hàng đã đặt thành công
//		Sau khi ấn đặt hàng
		
//		Trang này sẽ nhận vào order id nếu đặt hàng thành công 
//		Kiểm tra order id hiện tại có phải đơn hàng của user hay không?
		
		try {
			OrderEntity orderEntity = orderServices
					.getOrderById(orderOptional);
			
		}catch(Exception e) {
			e.printStackTrace();
//			nếu có lỗi xảy ra chuyển về trang danh sách đơn hàng
			return "redirect:/user/orders";
		}
		
		return "order-success.html";
	}
	
	@PostMapping("/user/create-order")
	public String handleCreateOrder(Model model,
			@ModelAttribute(name = "bean") @Valid OrderCreateBean bean,
			Errors errors) {
//		Cho người dùng đặt toàn bộ sản phẩm trong giỏ hàng 
//		Sau khi đặt xong trừ số lượng sản phẩm 
//		Xoá giỏ hàng 
		if(!errors.hasErrors()) {
//			Không có lỗi ở form 
			
			try {
				int orderId = orderServices.createOrder(bean);
				return "redirect:/user/order-success?orderId=" + orderId;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
//		chuyển về trang checkout nếu có lỗi xảy ra
		return "redirect:/user/create-order";
	}
}

package com.fpoly.java5.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserOrderController {

//	Đặt hàng POST
//	Thông báo trạng thái đơn hàng GET (Đặt thành công hoặc thất bại)
//	Danh sách đơn hàng GET 
//	Chi tiết đơn hàng GET 
//	Huỷ đơn POST
//	Xác nhận hoàn thành POST
	
	@GetMapping("/user/order-success")
	public String orderSuccessUI() {
		
		return "order-success.html";
	}
	
	@PostMapping("/user/create-order")
	public String handleCreateOrder() {
		
		return 
	}
}

package com.fpoly.java5.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.java5.beans.OrderCreateBean;
import com.fpoly.java5.entities.CartItemEntity;
import com.fpoly.java5.entities.OrderEntity;
import com.fpoly.java5.entities.OrderItemEntity;
import com.fpoly.java5.entities.UserEntity;
import com.fpoly.java5.jpas.CartJPA;
import com.fpoly.java5.jpas.OrderItemJPA;
import com.fpoly.java5.jpas.OrderJPA;
import com.fpoly.java5.jpas.UserJPA;
import com.fpoly.java5.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class OrderServices {
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private OrderJPA orderJPA;
	
	@Autowired
	private UserJPA userJPA;
	
	@Autowired
	private OrderItemJPA orderItemJPA;
	
	@Autowired
	private CartJPA cartJPA;
	
	public OrderEntity getOrderById(Optional<String> orderOptional) {
//		Kiểm tra orderID có null hay không?
		if(!orderOptional.isPresent()) {
			throw new RuntimeException();
		}
		
		int orderId = Integer.parseInt(orderOptional.get());
		
//		Kiểm tra order id hiện tại có phải đơn hàng của user hay không?
		int userId = Integer.parseInt(Utils.getCookieByName("USER_ID", request));
		
		Optional<OrderEntity> orderEntityOptional = orderJPA.findById(orderId);
		
		if(!orderEntityOptional.isPresent()) {
			throw new RuntimeException();
		}
		
		OrderEntity orderEntity = orderEntityOptional.get();
//		Kiểm tra order id hiện tại có phải đơn hàng của user hay không?
		if(orderEntity.getUserEntity().getId() != userId) {
			throw new RuntimeException();
		}
		
		return orderEntity;
	}
	
//	Trả về id của đơn hàng vừa tạo
	public int createOrder(OrderCreateBean bean) {
//		insert tạo đơn hàng 
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setStatus(1);
		orderEntity.setShippingName(bean.getName());
		orderEntity.setShippingPhone(bean.getPhone());
		orderEntity.setShippingAddress(bean.getAddress());
		orderEntity.setNote(bean.getNote());
		orderEntity.setCreatedDate(LocalDateTime.now());
		
		int userId = Integer.parseInt(Utils.getCookieByName("USER_ID", request));
		Optional<UserEntity> userOptional = userJPA.findById(userId);
		orderEntity.setUserEntity(userOptional.get());
		
//		Tổng tiền đơn hàng
//		- Lấy được danh sách sản phẩm trong giỏ hàng
//		- Tính tổng tiền của mỗi sản phẩm giá (Lấy trong sản phẩm) * số lượng
		int totalAmount = 0;
		for(CartItemEntity cartItemEntity : userOptional.get().getCartEntity().getCartItemEntities()) {
			totalAmount += (cartItemEntity.getQuantity() * cartItemEntity.getProductEntity().getPrice());
		}
		orderEntity.setTotalAmount(totalAmount);

//		Thông tin đơn hàng
		OrderEntity orderEntitySave = orderJPA.save(orderEntity);
		
//		insert chi tiết đơn hàng
		for(CartItemEntity cartItemEntity : userOptional.get().getCartEntity().getCartItemEntities()) {
			OrderItemEntity orderItemEntity = new OrderItemEntity();
			orderItemEntity.setPrice(cartItemEntity.getProductEntity().getPrice());
			orderItemEntity.setQuantity(cartItemEntity.getQuantity());
			orderItemEntity.setTotalPrice(cartItemEntity.getProductEntity().getPrice() * cartItemEntity.getQuantity());
			orderItemEntity.setOrderEntity(orderEntitySave);
			orderItemEntity.setProductEntity(cartItemEntity.getProductEntity());
			orderItemJPA.save(orderItemEntity);
		}
//		delete giỏ hàng
		cartJPA.delete(userOptional.get().getCartEntity());
		
		return orderEntitySave.getId();
	}
}

package com.fpoly.java5.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.java5.entities.CartEntity;
import com.fpoly.java5.entities.CartItemEntity;
import com.fpoly.java5.entities.ProductEntity;
import com.fpoly.java5.entities.UserEntity;
import com.fpoly.java5.jpas.CartItemJPA;
import com.fpoly.java5.jpas.CartJPA;
import com.fpoly.java5.jpas.ProductJPA;
import com.fpoly.java5.jpas.UserJPA;
import com.fpoly.java5.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CartServices {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserJPA userJPA;
	
	@Autowired
	private CartJPA cartJPA;
	
	@Autowired
	private ProductJPA productJPA;
	
	@Autowired
	private CartItemJPA cartItemJPA;
	
//	Dùng GET với url /user/add-to-cart 
	public void addToCart(int prodId) {
//		Kiểm tra user có giỏ hàng chưa?
//		- Nếu chưa có thì thực hiện tạo giỏ hàng
		
		int userId = Integer.parseInt(Utils.getCookieByName("USER_ID", request));
		Optional<UserEntity> userOptional = userJPA.findById(userId);
		
		CartEntity cartEntityDB;
		
		if(userOptional.get().getCartEntity() == null) {
//			Tạo giỏ hàng 
			CartEntity cartEntity = new CartEntity();
			cartEntity.setUserEntity(userOptional.get());
			
			cartEntityDB = cartJPA.save(cartEntity);
		}else {
//			Đã có giỏ hàng
			cartEntityDB = userOptional.get().getCartEntity();
		}
		
//		Đã có giỏ hàng của user
		
//		Kiểm tra sản phẩm có tồn tại trong db không?
//		Kiểm tra sản phẩm có bị khoá không?
		
		Optional<ProductEntity> prodOptional = productJPA.findById(prodId);
		if(!prodOptional.isPresent()) {
			throw new RuntimeException("Sản phẩm không tồn tại");
		}
		
		ProductEntity productEntity = prodOptional.get();
		
		if(!productEntity.isActive()) {
			throw new RuntimeException("Sản phẩm hiện không khả dụng");
		}
		
//		Cách 1: Viết lệnh SQL để kiểm tra sản phẩm trong giỏ hàng 
//		SELECT * FROM cart_items WHERE cart_id = ? AND prod_id = ?
//		Cách 2: Lấy danh sách sản phẩm trong giỏ hàng và dùng for duyệt để ktra 
		
		Optional<CartItemEntity> cartItemOptional = 
				cartItemJPA.getCartItemByProdIdAndCartId(cartEntityDB.getId(), prodId);
		
		if(!cartItemOptional.isPresent()) {
			if(productEntity.getQuantity() < 1) {
				throw new RuntimeException("Sản phẩm tạm hết hàng");
			}
			
			CartItemEntity cartItemEntity = new CartItemEntity();
			cartItemEntity.setCartEntity(cartEntityDB);
			cartItemEntity.setProductEntity(productEntity);
			cartItemEntity.setQuantity(1);
			
			cartItemJPA.save(cartItemEntity);
		}else{
			CartItemEntity cartItemEntity = cartItemOptional.get();
			
			if(productEntity.getQuantity() < cartItemEntity.getQuantity() + 1) {
				throw new RuntimeException("Sản phẩm không đủ số lượng");
			}
			
			cartItemEntity.setQuantity(cartItemEntity.getQuantity() + 1);
			
			cartItemJPA.save(cartItemEntity);
		}
		
//		Sản phẩm này có trong giỏ hàng của user chưa?
//		- Nếu chưa có thực hiện gán sản phẩm và giỏ hàng
//		- Nếu đã có thực hiện cập nhật số lượng sản phẩm + 1
		
//		Trước khi tương tác vào DB => Kiểm tra số lượng sản phẩm
//		cần thêm vào giỏ hàng có đủ với số lượng sản phẩm đang có không?
		
		
//		Khi thêm sản phẩm vào giỏ hàng có cần trừ số lượng
//		ở sản phẩm trong kho không? => Không trừ trong kho 
	}
	
//	Tăng số lượng sản phẩm ở giỏ hàng 
	public void increaseCartItem(int cartItemId) {
//		Lấy user id từ cookie
		int userId = Integer.parseInt(Utils.getCookieByName("USER_ID", request));
//		Lấy thông tin user từ database bằng user id lấy được ở cookie
		Optional<UserEntity> userOptional = userJPA.findById(userId);
		
//		Để thực hiện được chức năng tăng sản phẩm này thì user chắc chắn có giỏ hàng
		
//		Chắc chắn có giá trị (Không thể null)
		CartEntity cartEntity = userOptional.get().getCartEntity();
		
//		Lấy thông tin của cart item từ id
//		=> So sánh id giỏ hàng của cart item và id giỏ hàng của user
//		=> Nếu trùng khớp => Cho tăng số lượng
//		=> Nếu không trùng => Báo "Sản phẩm trong giỏ hàng không thuộc sở hữu của user hiện tại"
		
//		Lấy thông tin của cartItem từ id
		Optional<CartItemEntity> cartItemOptional = cartItemJPA.findById(cartItemId);
		
//		Cart  Item không tồn tại 
		if(!cartItemOptional.isPresent()) {
			throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng!");
		}
		
		CartItemEntity cartItemEntity = cartItemOptional.get();
		
//		Cart Item không thuộc quyền sở hữu của user đang đăng nhập
		if(cartItemEntity.getCartEntity().getId() != cartEntity.getId()) {
			throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng!");
		}
		
//		Kiểm tra số lượng sản phẩm trong kho còn khả dụng không?
		if(cartItemEntity.getQuantity() + 1 > cartItemEntity.getProductEntity().getQuantity()) {
			throw new RuntimeException("Sản phẩm không đủ số lượng");
		}
		
		cartItemEntity.setQuantity(cartItemEntity.getQuantity() + 1);
		cartItemJPA.save(cartItemEntity);
	}
	
//	Giảm số lượng sản phẩm ở giỏ hàng
	public void decreaseCartItem(int cartItemId) {
		int userId = Integer.parseInt(Utils.getCookieByName("USER_ID", request));
		Optional<UserEntity> userOptional = userJPA.findById(userId);
		CartEntity cartEntity = userOptional.get().getCartEntity();

		Optional<CartItemEntity> cartItemOptional = cartItemJPA.findById(cartItemId);
		
		if(!cartItemOptional.isPresent()) {
			throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng!");
		}
		
		CartItemEntity cartItemEntity = cartItemOptional.get();
		
		if(cartItemEntity.getCartEntity().getId() != cartEntity.getId()) {
			throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng!");
		}
		 
		if(cartItemEntity.getQuantity() - 1 == 0) {
			cartItemJPA.deleteById(cartItemId);
		}else {
			cartItemEntity.setQuantity(cartItemEntity.getQuantity() - 1);
			cartItemJPA.save(cartItemEntity);
		}
	}
}

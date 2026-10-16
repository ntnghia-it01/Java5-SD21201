package com.fpoly.java5.jpas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fpoly.java5.entities.CartItemEntity;

public interface CartItemJPA extends JpaRepository<CartItemEntity, Integer>{

	@Query(value = "SELECT * FROM cart_items WHERE cart_id = ?1 AND product_id = ?2", nativeQuery = true)
	Optional<CartItemEntity> getCartItemByProdIdAndCartId(int cartId, int prodId);
}

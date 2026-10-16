package com.fpoly.java5.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "cart_items")
public class CartItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_item_id", nullable = false)
	private int id;
	
	@Column(name = "quantity", nullable = false)
	private int quantity = 1;
	
	@ManyToOne
	@JoinColumn(name = "cart_id")
	private CartEntity cartEntity;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private ProductEntity productEntity;
}


//CREATE TABLE [dbo].[cart_items] (
//	    [cart_item_id] INT IDENTITY (1, 1) NOT NULL,
//	    [cart_id]      INT NOT NULL,
//	    [product_id]   INT NOT NULL,
//	    [quantity]     INT DEFAULT ((1)) NOT NULL,
//	    PRIMARY KEY CLUSTERED ([cart_item_id] ASC),
//	    CONSTRAINT [ck_cart_items_quantity] CHECK ([quantity]>(0)),
//	    CONSTRAINT [fk_cart_items_carts] FOREIGN KEY ([cart_id]) REFERENCES [dbo].[carts] ([cart_id]) ON DELETE CASCADE,
//	    CONSTRAINT [fk_cart_items_products] FOREIGN KEY ([product_id]) REFERENCES [dbo].[products] ([product_id]),
//	    CONSTRAINT [uq_cart_items_product] UNIQUE NONCLUSTERED ([cart_id] ASC, [product_id] ASC)
//	);
//

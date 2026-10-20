package com.fpoly.java5.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "order_items")
public class OrderItemEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private int id;
	
	@Column(name = "price", nullable =  false)
	private int price;
	
	@Column(name = "quantity", nullable =  false)
	private int quantity;
	
	@Column(name = "total_price", nullable =  false)
	private int totalPrice;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private OrderEntity orderEntity;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private ProductEntity productEntity;
}


//CREATE TABLE [dbo].[order_items] (
//	    [order_item_id] INT             IDENTITY (1, 1) NOT NULL,
//	    [order_id]      INT             NOT NULL,
//	    [product_id]    INT             NOT NULL,
//	    [price]         DECIMAL (18, 2) NOT NULL,
//	    [quantity]      INT             NOT NULL,
//	    [total_price]   AS              ([price]*[quantity]) PERSISTED,
//	    PRIMARY KEY CLUSTERED ([order_item_id] ASC),
//	    CONSTRAINT [ck_order_items_price] CHECK ([price]>=(0)),
//	    CONSTRAINT [ck_order_items_quantity] CHECK ([quantity]>(0)),
//	    CONSTRAINT [fk_order_items_orders] FOREIGN KEY ([order_id]) REFERENCES [dbo].[orders] ([order_id]) ON DELETE CASCADE,
//	    CONSTRAINT [fk_order_items_products] FOREIGN KEY ([product_id]) REFERENCES [dbo].[products] ([product_id])
//	);


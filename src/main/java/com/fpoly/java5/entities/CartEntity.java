package com.fpoly.java5.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "carts")
public class CartEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_id", nullable = false)
	private int id;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity userEntity;
	
	@OneToMany(mappedBy = "cartEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<CartItemEntity> cartItemEntities;
}


//CREATE TABLE [dbo].[carts] (
//	    [cart_id] INT IDENTITY (1, 1) NOT NULL,
//	    [user_id] INT NOT NULL,
//	    PRIMARY KEY CLUSTERED ([cart_id] ASC),
//	    CONSTRAINT [fk_carts_users] FOREIGN KEY ([user_id]) REFERENCES [dbo].[users] ([user_id]),
//	    CONSTRAINT [uq_carts_user] UNIQUE NONCLUSTERED ([user_id] ASC)
//	);
//

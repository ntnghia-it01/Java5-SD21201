package com.fpoly.java5.entities;

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
@Table(name = "products")
public class ProductEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id", nullable = false)
	private int id;
	
	@Column(name = "product_name", nullable = false, columnDefinition = "NVARCHAR (200)")
	private String name;
	
	@Column(name = "description", nullable = false, columnDefinition = "NVARCHAR (MAX)")
	private String desc;
	
	@Column(name = "price", nullable = false)
	private double price;
	
	@Column(name = "quantity", nullable = false)
	private int quantity = 0;
	
	@Column(name = "image_url", nullable = true, length = 500)
	private String imageUrl;
	
	@Column(name = "is_active", nullable = true)
	private boolean active = true;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private CategoryEntity categoryEntity;
}


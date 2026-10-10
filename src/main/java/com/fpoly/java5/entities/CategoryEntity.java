package com.fpoly.java5.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "categories")
public class CategoryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", nullable = false)
	private int id;
	
	@Column(name = "category_name", nullable = false, columnDefinition = "NVARCHAR (100)")
	private String name;
	
	@Column(name = "is_active", nullable = false)
	private boolean active = true;
	
	@OneToMany(mappedBy = "categoryEntity")
	private List<ProductEntity> productEntities;
}


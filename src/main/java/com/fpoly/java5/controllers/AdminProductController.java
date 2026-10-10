package com.fpoly.java5.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.fpoly.java5.beans.ProductFormBean;
import com.fpoly.java5.entities.CategoryEntity;
import com.fpoly.java5.jpas.CategoryJPA;
import com.fpoly.java5.services.ImageUploadServices;
import com.fpoly.java5.services.ProductServices;

import jakarta.validation.Valid;

@Controller
public class AdminProductController {
	
	@Autowired 
	private ImageUploadServices imageUploadServices;
	
	@Autowired
	private CategoryJPA categoryJPA;
	
	@Autowired
	private ProductServices productServices;

	@GetMapping("/admin/product-form")
	public String productFormUI(Model model) {
		
		model.addAttribute("bean", new ProductFormBean());
		
		return "product-form.html";
	}
	
	@PostMapping("/admin/product-form")
	public String handleProductForm(Model model,
			@ModelAttribute(name = "bean") @Valid ProductFormBean bean,
			Errors errors) {
		if(!errors.hasErrors() && bean.getImageError().equals("")) {
			try {
				productServices.addProduct(bean);
			} catch (Exception e) {
				model.addAttribute("productError", e.getMessage());
			}
		}
		
		return "product-form.html";
	}
	
//	Khi Controller hiện tại được kích hoạt
//	Thì các hàm có gán @ModelAttribute sẽ được chạy để gửi dữ liệu qua html
	@ModelAttribute("categories")
	public List<CategoryEntity> getCategoryEntities(){
		List<CategoryEntity> categoryEntities = categoryJPA.findAll();

		return categoryEntities;
	}
}

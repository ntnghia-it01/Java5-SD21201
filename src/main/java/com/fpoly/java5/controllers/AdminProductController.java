package com.fpoly.java5.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fpoly.java5.beans.ProductFormBean;
import com.fpoly.java5.entities.CategoryEntity;

import jakarta.validation.Valid;

@Controller
public class AdminProductController {

	@GetMapping("/admin/product-form")
	public String productFormUI(Model model) {
		
		model.addAttribute("bean", new ProductFormBean());
		
		return "product-form.html";
	}
	
	@PostMapping("/admin/product-form")
	public String handleProductForm(Model model,
			@ModelAttribute(name = "bean") @Valid ProductFormBean bean,
			Errors errors) {
		
//		Kiểm tra lỗi ở form có tồn tại không?
//		bean.getImageError().equals("") => Ảnh không lỗi
//		!errors.hasErrors() => Dữ liệu ở form không lỗi
//		Mọi thứ bên trong src muốn ở website thấy => Được thực thi
		if(!errors.hasErrors() && bean.getImageError().equals("")) {
			
		}
		
		return "product-form.html";
	}
	
//	Khi Controller hiện tại được kích hoạt
//	Thì các hàm có gán @ModelAttribute sẽ được chạy để gửi dữ liệu qua html
	@ModelAttribute("categories")
	public List<CategoryEntity> getCategoryEntities(){
		List<CategoryEntity> categoryEntities = new ArrayList<CategoryEntity>();
		categoryEntities.add(new CategoryEntity(1, "Danh muc 1"));
		categoryEntities.add(new CategoryEntity(2, "Danh muc 2"));
		categoryEntities.add(new CategoryEntity(3, "Danh muc 3"));
		categoryEntities.add(new CategoryEntity(4, "Danh muc 4"));
		categoryEntities.add(new CategoryEntity(5, "Danh muc 5"));
		
		return categoryEntities;
	}
}

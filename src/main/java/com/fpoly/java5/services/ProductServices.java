package com.fpoly.java5.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.java5.beans.ProductFormBean;
import com.fpoly.java5.entities.CategoryEntity;
import com.fpoly.java5.entities.ProductEntity;
import com.fpoly.java5.jpas.CategoryJPA;
import com.fpoly.java5.jpas.ProductJPA;

@Service
public class ProductServices {
	
	@Autowired
	private ImageUploadServices imageUploadServices;
	
	@Autowired
	private CategoryJPA categoryJPA;
	
	@Autowired
	private ProductJPA productJPA;
	
	public void addProduct(ProductFormBean bean) {
//		Lưu ảnh vào project => lấy url 
		String imageUrl = imageUploadServices.save(bean.getImage());
		if(imageUrl == null) {
			throw new RuntimeException("Lưu ảnh không thành công");
		}
//		Chuyển dữ liệu từ bean qua entity 
		ProductEntity productEntity = new ProductEntity();
		productEntity.setName(bean.getName());
		productEntity.setDesc(bean.getDesc());
		productEntity.setPrice(bean.getPrice());
		productEntity.setQuantity(bean.getQuantity());
		productEntity.setImageUrl(imageUrl);
		productEntity.setActive(bean.getStatus() == 2 ? true : false);
		
//		bean chỉ có id của category
//		=> Cần truy vấn thông tin danh mục từ db
		Optional<CategoryEntity> catOptional = categoryJPA.findById(bean.getCategory());
		if(!catOptional.isPresent()) {
			throw new RuntimeException("Danh mục không tồn tại");
		}
		productEntity.setCategoryEntity(catOptional.get());
//		Lưu db
		productJPA.save(productEntity);
	}
}

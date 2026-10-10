package com.fpoly.java5.beans;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductFormBean {
	@NotBlank(message = "Tên sản phẩm không được bỏ trống")
	private String name;
	@NotBlank(message = "Mô tả không được bỏ trống")
	@Length(min = 20, message = "Mô tả phải có tối thiểu 20 ký tự")
	private String desc;
	@Min(value = 10000, message = "Giá thấp nhất là 10.000 VNĐ")
	private int price;
	@Min(value = 0, message = "Số lượng không âm")
	private int quantity;
	@Min(value = 1, message = "Danh mục bắt buộc chọn")
	private int category; // id danh mục trong db
//	Chưa có anotation hỗ trợ
	private MultipartFile image;
	@Range(min = 1, max = 2, message = "Trạng thái bắt buộc chọn")
	private int status;
	
	public String getImageError() {		
		if(image == null) {
			return "Ảnh sản phẩm là bắt buộc";
		}
		
		double maxSize = 1024 * 1024 * 5; // => 5MB 
		
		if(!image.getContentType().startsWith("image/")) {
			return "File tải lên phải là ảnh";
		}
		
		if(image.getSize() > maxSize) {
			return "Kích thước tải lên tối đa 5MB mỗi ảnh";
		}
		
		
		return "";
	}
}

//Tên không bỏ trống
//Mô tả phải có ít nhất 20 ký tự
//Giá > 10.000 VNĐ
//Số lượng > 0
//Danh mục bắt buộc chọn
//Kích thước tối đa 5MB
//Trạng thái bắt buộc chọn 
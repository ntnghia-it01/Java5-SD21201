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
	private List<MultipartFile> images;
	@Range(min = 1, max = 2, message = "Trạng thái bắt buộc chọn")
	private int status;
	
	public String getImageError() {
//		Viết if else để kiểm tra
//		- Mỗi sản phẩm lưu được tối đa 5 ảnh
//		- Chỉ cần 1 File tải lên không phải ảnh => Lỗi 
//		- Chỉ cần 1 File tải lên quá 5MB => Lỗi
//		Nếu có lỗi return về chuỗi nội dung lỗi
//		Không có lỗi return về null
		
		if(images == null) {
			return "";
		}
		
		if(images.size() == 0) {
			return "Ảnh sản phẩm là bắt buộc";
		}
		
		if(images.size() > 5) {
			return "Mỗi sản phẩm lưu được tối đa 5 ảnh";
		}
		
		double maxSize = 1024 * 1024 * 5; // => 5MB 
		for(MultipartFile file : images) {
//			file.getContentType()
//			image/png, image/jpg, image/webp,.... 
			if(!file.getContentType().startsWith("image/")) {
				return "File tải lên phải là ảnh";
			}
			
			if(file.getSize() > maxSize) {
				return "Kích thước tải lên tối đa 5MB mỗi ảnh";
			}
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
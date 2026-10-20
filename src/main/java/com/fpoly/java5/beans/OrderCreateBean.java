package com.fpoly.java5.beans;

import org.hibernate.annotations.Parent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderCreateBean {
	
	@NotBlank(message = "Tên nhận hàng không rỗng")
	private String name;
	
	@NotBlank(message = "Số điện thoại nhận hàng không rỗng")
	@Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại không đúng định dạng")
	private String phone;
	
	@NotBlank(message = "Địa chỉ nhận hàng không rỗng")
	private String address;
	
	private String note;
}

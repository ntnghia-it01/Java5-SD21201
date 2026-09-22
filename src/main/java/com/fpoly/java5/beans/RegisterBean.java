package com.fpoly.java5.beans;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterBean {
	@NotBlank(message = "Tên tài khoản không được bỏ trống")
	@Length(min = 4, message = "Tên tài khoản tối thiểu 4 ký tự")
	private String username;
	@Length(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
	private String password;
	@NotBlank(message = "Họ và tên không được bỏ trống")
	private String name;
	@NotBlank(message = "Email không được để trống")
	@Email(message = "Email không đúng định dạng")
	private String email;
	@Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại không đúng định dạng")
	private String phone;
}

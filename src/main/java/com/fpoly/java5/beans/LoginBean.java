package com.fpoly.java5.beans;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginBean {
	@NotBlank(message = "Email không được bỏ trống")
	@Email(message = "Email không đúng định dạng")
	private String email;
	
	@NotBlank(message = "Mật khẩu không được bỏ trống")
	private String password;
}

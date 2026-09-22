package com.fpoly.java5.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
	
//	Tạo 1 trang giao diện
//	Với url là /
//	Và html là file index.html
//	Tên của url phải là duy nhất trong cả project kèm theo phương thức (GET, POST, PUT, DELETE)
//	@GetMapping(name = "/") // => GET
//	@PostMapping(name = "/") // => POST
//	@PutMapping(name = "/") // => PUT
//	@DeleteMapping(name = "/") // => DELETE
	
//	Khi controller được khởi tạo
//	=> Sẽ tìm các đối tượng tưng ứng để gán giá trị vào 
	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;
	
	
//	@RequestParam ràng buộc chặt chẽ hơn
//	Nếu có khai báo @RequestParam 
//	=> Nhưng ở url gọi không truyền các giá trị này lên
//	=> phương thức này sẽ không được chạy 
	@GetMapping(name = "/")
	public String home(Model model,
//			default required = true
//			required = false => key class này có thể truyền lên hay không cũng được
//			Nếu không gửi lên className == null;
			@RequestParam(name = "class", defaultValue = "abc", required = false) String className,
			@RequestParam(name = "point", defaultValue = "0", required = false) double pointQuery) {
		
		model.addAttribute("message", String.format("Lop: %s, Diem: %.2f", className, pointQuery + 1));
		
//		Làm sao để lấy được giá trị của class 
		
//		String className = request.getParameter("class");
		
//		Lấy giá trị của point ra và + thêm 1
//		Hiển thị tên lớp và điểm ở message
//		String pointQuery = request.getParameter("point");
//		try {
//			double point = Double.parseDouble(pointQuery) + 1;
//			model.addAttribute("message", String.format("Lop: %s, Diem: %.2f", className, point));
//		} catch (Exception e) {
//			
//		}
		
		return "index.html";
	}
}

package com.fpoly.java5.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUploadServices {
	private static final String UPLOAD_DIR = "images-upload";

//	Trả về vị trí lưu ảnh + tên ảnh nếu lưu thành công 
	public String save(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return null;
		}

		// Kiểm tra loại file
		String contentType = file.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) {
			throw new RuntimeException("Only image files are allowed");
		}

		try {
			// Tạo thư mục nếu chưa tồn tại
			Files.createDirectories(Paths.get(UPLOAD_DIR));

			// Lấy phần mở rộng của file png, jpg, webp
			String originalName = file.getOriginalFilename();
			String extension = originalName.substring(originalName.lastIndexOf("."));

			// Tạo tên file: Thời gian hiên tại đổi qua ms + phần mở rộng 
			String fileName = String.format("%d%s", new Date().getTime(), extension);
			// Tạo file ảnh rỗng trong thư mục chỉ định 
			Path targetPath = Paths.get(UPLOAD_DIR, fileName);

			// Thực hiện copy nội dung ở file upload vào file vừa tạo
			// StandardCopyOption.REPLACE_EXISTING => Nếu trùng tên sẽ thực hiện ghi đè
			Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

			return fileName;

		} catch (IOException e) {
			throw new RuntimeException("Cannot store image file", e);
		}
	}

}
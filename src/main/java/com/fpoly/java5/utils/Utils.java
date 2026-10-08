package com.fpoly.java5.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class Utils {
	public static String getCookieByName(String key, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		
		if(cookies == null) return null;
		
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(key)) {
				return cookie.getValue();
			}
		}
		
		return null;
	}
}

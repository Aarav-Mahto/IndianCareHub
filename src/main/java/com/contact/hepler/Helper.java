package com.contact.hepler;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

public interface Helper {

	public String uploadImage(MultipartFile partFile);
	
	public String getImageUrl();
	
	public boolean deleteFile(String imageFileName);
	
	public Date currentDate();
	
	public String dateFormat(String dateStr);
	
	public String imagePath(String image);
	
	public boolean isNullOrEmpty(String... fields);
	
	public boolean isNullOrEmptyList(List<?>... lists);
	
	public boolean isNullOrEmptyMap(Map<?,?>... maps);
	
	public void setMsgSession(HttpSession session, String msg, String color);
	
	public void validateSession(HttpSession session);
	
	public String SeoKeywords(String orgName, String sortName, String otherName);
	
	public String seoDesc(List<String> nm, List<String> dept);
	
	//public String pageTitle(List<String> nm, String sortName, int id);
	
	public Map<Integer, List<String>> socialLinksFormater(Map<String,String> socialMedia);
}

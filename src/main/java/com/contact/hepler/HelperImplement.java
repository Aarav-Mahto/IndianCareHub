package com.contact.hepler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.MultipartFile;

import com.contact.controller.AdminController;

import jakarta.servlet.http.HttpSession;

public class HelperImplement implements Helper {

	public static final Logger logger = Logger.getLogger(AdminController.class.getName());
	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public String uploadImage(MultipartFile partFile) {
		if (partFile != null && !partFile.isEmpty()) {
			try {
				Path rootClassPath = Paths.get(resourceLoader.getResource("classpath:/").getURI()).toAbsolutePath()
						.getParent().getParent().getParent();
				Path targetPath = rootClassPath.resolve("Contact_Logo");

				if (Files.exists(targetPath) || Files.isDirectory(targetPath)) {
					Path imageFilePath = targetPath.resolve(partFile.getOriginalFilename());
					Files.copy(partFile.getInputStream(), imageFilePath, StandardCopyOption.REPLACE_EXISTING);
					return partFile.getOriginalFilename();
				} else {
					logger.warning(
							"Error: The 'Contact_Logo' Folder or directory does not exist or is not a directory.");
					return "";
				}

			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		} else {
			return "";
		}
	}

	@Override
	public String getImageUrl() {
		try {
			Path rootClassPath = Paths.get(resourceLoader.getResource("classpath:/").getURI()).toAbsolutePath()
					.getParent().getParent().getParent();
			String projectName = rootClassPath.getFileName().toString();
			System.out.println(projectName + File.separator + "Contact_Logo");
			return projectName + File.separator + "Contact_Logo";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean deleteFile(String imageFileName) {
		try {
			Path rootClassPath = Paths.get(resourceLoader.getResource("classpath:/").getURI()).toAbsolutePath()
					.getParent().getParent().getParent();
			Path targetPath = rootClassPath.resolve("Contact_Logo");
			if (Files.exists(targetPath) || Files.isDirectory(targetPath)) {
				Path imageFilePath = targetPath.resolve(imageFileName);
				Files.delete(imageFilePath);
				logger.info("File " + imageFileName + " deleted successfully.");
				return true;
			} else {
				logger.warning("File " + imageFileName + " does not exist or is not a file.");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Date currentDate() {
		LocalDate localDate = LocalDate.now();
		Date date = Date.valueOf(localDate);
		return date;
	}

	@Override
	public String dateFormat(String dateStr) {
		LocalDate date = LocalDate.parse(dateStr);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		String formattedDate = date.format(formatter);
		return formattedDate;
	}

	@Override
	public String imagePath(String image) {
		String imagepath = File.separator + "Contact_Logo" + File.separator + image;
		return imagepath;
	}

	@Override
	public boolean isNullOrEmpty(String... fields) {// Similar to 'String[] fields'
		for (String field : fields) {
			if (field == null || field.trim().isEmpty() || field.trim() == "") {
				return true;
			}
		}
		return false;
	}

	@Override
	public void setMsgSession(HttpSession session, String msg, String color) {
		session.setAttribute("msg", msg);
		session.setAttribute("color", color);
	}

	@Override
	public void validateSession(HttpSession session) {
		if (session.getAttribute("msg") != null) {
			session.removeAttribute("msg");
		}
		if (session.getAttribute("color") != null) {
			session.removeAttribute("color");
		}
	}

	@Override
	public String SeoKeywords(String orgName, String sortName, String otherName) {
		String seoKeyword = "";
		if (orgName.equalsIgnoreCase(otherName) && sortName.equalsIgnoreCase(otherName))
			seoKeyword = orgName + " Contact Number, " + orgName + " Customer Care Number, " + orgName
					+ " Helpline Number, "
					+ orgName + " Telephone Directory, " + orgName + " Helpline Email, " + orgName + " Address, "
					+ sortName + " Toll Free Number, " + sortName + " Customer Care Email, " + sortName
					+ " Service Center Number, "
					+ sortName + " Enquiry Number, " + sortName + " Contact Details";

		else if (orgName.equalsIgnoreCase(otherName) || sortName.equalsIgnoreCase(otherName)
				|| orgName.equalsIgnoreCase(sortName)) {
			if (orgName.equalsIgnoreCase(otherName))
				seoKeyword = orgName + " Contact Number, " + sortName + " Contact Number, " + orgName
						+ " Customer Care Number, "
						+ sortName + " Customer Care Number, " + orgName + " Helpline Number, " + sortName
						+ " Helpline Number, "
						+ orgName + " Telephone Directory, " + sortName + " Telephone Directory, " + sortName
						+ " Toll Free Number, "
						+ sortName + " Customer Care Email, " + sortName + " Service Center Number" + sortName
						+ " Enquiry Number, "
						+ sortName + " Contact Details";

			else if (sortName.equalsIgnoreCase(otherName))
				seoKeyword = orgName + " Contact Number, " + sortName + " Contact Number, " + orgName
						+ " Customer Care Number, "
						+ sortName + " Customer Care Number, " + orgName + " Helpline Number, " + sortName
						+ " Helpline Number, "
						+ orgName + " Telephone Directory, " + sortName + " Telephone Directory, " + sortName
						+ " Toll Free Number, "
						+ sortName + " Customer Care Email, " + sortName + " Service Center Number, " + sortName
						+ " Enquiry Number, "
						+ sortName + " Contact Details";

			else
				seoKeyword = sortName + " Contact Number, " + otherName + " Contact Number, " + sortName
						+ " Customer Care Number, "
						+ otherName + " Customer Care Number, " + sortName + " Helpline Number, " + otherName
						+ " Helpline Number, "
						+ sortName + " Telephone Directory, " + otherName + " Telephone Directory, " + sortName
						+ " Toll Free Number, "
						+ sortName + " Customer Care Email, " + sortName + " Service Center Number, " + sortName
						+ " Enquiry Number, "
						+ sortName + " Contact Details";

		} else
			seoKeyword = orgName + " Contact Number, " + sortName + " Contact Number, " + otherName
					+ " Contact Number, "
					+ orgName + " Customer Care Number, " + sortName + " Customer Care Number, " + otherName
					+ " Customer Care Number, "
					+ orgName + " Helpline Number, " + sortName + " Helpline Number, " + otherName
					+ " Helpline Number, "
					+ orgName + " Telephone Directory, " + sortName + " Telephone Directory, " + otherName
					+ " Telephone Directory, "
					+ sortName + " Toll Free Number, " + sortName + " Customer Care Email, " + sortName
					+ " Service Center Number, "
					+ sortName + " Enquiry Number, " + sortName + " Contact Details";
		return seoKeyword;
	}

	@Override
	public String seoDesc(List<String> nm, List<String> dept) {
		String pageDesc = "";
		String patternString = "^[+\\d\\-\\s()\\.\\[\\]/,]+$";
		Pattern pattern = Pattern.compile(patternString);
		for (int i = 0; i < nm.size(); i++) {
			Matcher matcher = pattern.matcher(nm.get(i));
			if (matcher.find()) {
				if (pageDesc.length() < 300) {
					pageDesc = pageDesc + dept.get(i) + ": " + nm.get(i) + ", ";
				} else {
					break;
				}
			}
		}
		if (pageDesc.length() < 200) {
			for (int i = 0; i < nm.size(); i++) {
				Matcher matcher = pattern.matcher(nm.get(i));
				if (!matcher.find()) {
					if (pageDesc.length() < 200) {
						pageDesc = pageDesc + dept.get(i) + ": " + nm.get(i) + ", ";
					} else {
						break;
					}
				}
			}
		}
		return pageDesc;
	}

	// @Override
	// public String pageTitle(List<String> nm, String sortName, int id) {
	// 	// page Title
	// 	String pageTitle = "Customer Care No.";
	// 	String firstNumber = "××××××××××";
	// 	String patternString = "^[+\\d\\-\\s()\\.\\[\\]/,]+$";
	// 	Pattern pattern = Pattern.compile(patternString);
	// 	for (int i = 0; i < nm.size(); i++) {
	// 		Matcher matcher = pattern.matcher(nm.get(i));
	// 		if (matcher.find()) {
	// 			firstNumber = nm.get(i);
	// 		}
	// 	}

	// 	if (id <= 500) {
	// 		pageTitle = sortName + " Contact No." + firstNumber;
	// 	} else {
	// 		pageTitle = sortName + " Customer Care No." + firstNumber;
	// 	}
	// 	return null;
	// }

	@Override
	public Map<Integer, List<String>> socialLinksFormater(Map<String, String> sociaMedia) {
		Map<Integer, List<String>> tempMap = new LinkedHashMap<>();
		int i=0;
		for(Map.Entry<String, String> entry : sociaMedia.entrySet()){
			List<String> tempList = new ArrayList<>();
			tempList.add(entry.getKey());
			tempList.add(entry.getValue());
			tempList.add(socialLinksName(entry.getKey()));
			tempMap.put(i, tempList);
			i++;
		}
		return tempMap;
	}
	
	private String socialLinksName(String socialName) {
		
		switch (socialName) {
			case "whatsapp": {
				return "ri-whatsapp-line";
			}
			case "facebook": {
				return "ri-facebook-fill";
			}
			case "instagram": {
				return "ri-instagram-line";
			}
			case "twitter": {
				return "ri-twitter-x-line";
			}
			case "linkedin": {
				return "ri-linkedin-fill";
			}
			case "youtube": {
				return "ri-youtube-line";
			}
			case "telegram": {
				return "ri-telegram-2-line";
			}
			case "threads": {
				return "ri-threads-line";
			}
			case "faq": {
				return "ri-questionnaire-line";
			}
			default:
				return null;
		}
	}

	@Override
	public boolean isNullOrEmptyList(List<?>... lists) {
		if (lists == null) {
			return true;
		}
		for (List<?> list : lists) {
			if (list == null || list.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isNullOrEmptyMap(Map<?, ?>... maps) {
		for (Map<?, ?> map : maps) {
			if (map == null || map.isEmpty()) {
				return true;
			}
		}
		return false;
	}

}

package com.contact.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.contact.dao.DAOService;
import com.contact.entity.Contact;
import com.contact.entity.QuestionAnchor;
import com.contact.hepler.Helper;

@Controller
public class ClientPageController {

	public static final Logger logger = Logger.getLogger(ClientPageController.class.getName());

	@Autowired
	private DAOService daoService;
	@Autowired
	private Helper helper;

	@GetMapping({ "/", "/index", "/homepage" })
	public String getHomePage() {
		return "index";
	}

	@PostMapping("/searchItemClicked")
	public ResponseEntity<String> redirectPage(@RequestParam("sid") String id, Model model) {
		String redirectUrl = "/contact/" + id;
		return ResponseEntity.ok(redirectUrl);

	}

	@GetMapping("/contact/{id}")
	public String getDynamicPage(@PathVariable("id") String url, Model model) {
		long startTime = System.currentTimeMillis();
		if (url == null || url.trim().isEmpty() || url.trim().matches("^[0-9]+$")) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page Not Found");
		}
		Map<String, Object> database = daoService.pageLoading(url.trim());
		if(helper.isNullOrEmptyMap(database)){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page Not Found");
		}
		
		QuestionAnchor questionAnchor = (QuestionAnchor)database.get("questionAnchor");
		if(questionAnchor != null){
			Map<Integer, List<String>> anchorMap = questionAnchor.getAnchorMapList();
			Map<Integer, List<String>> questionMap = questionAnchor.getQnMapList();
			model.addAttribute("anchorMap",anchorMap);
			model.addAttribute("questionMap",questionMap);
		}
		else{
			model.addAttribute("anchorMap",Collections.emptyMap());
			model.addAttribute("questionMap",Collections.emptyMap());
		}
		if(database.get("randomEntries") != null){
			Map<String, Object> randomEntries = Collections.emptyMap();
			randomEntries = (Map<String, Object>)database.get("randomEntries");
			model.addAttribute("randomEntries",randomEntries);
		}
		
		Contact contact = (Contact)database.get("contact");
		if (contact != null) {
			Map<Integer, List<String>> mediaLink = helper.socialLinksFormater(contact.getMedia_links());
			
			Map<String, String> headings = contact.getSeoHeading();
			Map<String, String> threeNum = new LinkedHashMap<>();
			Map<String, String> allNum = new LinkedHashMap<>();
			Map<String, String> allEmail = new LinkedHashMap<>();
			String description = "";

			int i = 0;
			Map<Integer, List<String>> dept_num = contact.getDept_num();
			Pattern pattern = Pattern.compile("^[+\\d\\-\\s()\\.\\[\\]/,]+$");
			for (Map.Entry<Integer, List<String>> entry : dept_num.entrySet()) {
				List<String> val = entry.getValue();
				if (pattern.matcher(val.get(3)).find()) {
					String cleanedNum = val.get(3).replaceAll("[\\s\\-()]+", " ");
					allNum.put(val.get(2), cleanedNum);
					if (description.length() < 200) {
						description = description + entry.getValue().get(2) + ": " + cleanedNum + " , ";
					}
					if (i < 3) {
						threeNum.put(val.get(2), cleanedNum);
						i++;
					}
				} else {
					allEmail.put(val.get(2), val.get(3));
				}
			}
			if (description.length() < 200) {
				for (String st : allEmail.keySet()) {
					if (description.length() < 200) {
						description = description + st + ": " + allEmail.get(st) + ", ";
					} else
						break;
				}
			}
			String lastUpdatedDate = contact.getAllFields().get("lastUpdated") != null
					? contact.getAllFields().get("lastUpdated")
					: contact.getAllFields().get("orgEntryDate");
			contact.getAllFields().put("lastUpdated", helper.dateFormat(lastUpdatedDate));
			// ================================================================================================================
			if (allNum.size() <= 4) {
				threeNum = Collections.emptyMap();
			}
			if (allEmail.size() <= 0) {
				allEmail = Collections.emptyMap();
			}
			if (mediaLink.size() <= 0) {
				mediaLink = Collections.emptyMap();
			}
			// Full Url Of This Page
			String fullUrl = "https://www.indiancarehub.com/contact/"+ contact.getAllFields().get("pageUrl");
			contact.getAllFields().put("fullUrl", fullUrl);

			contact.getAllFields().put("description", description);
			model.addAttribute("allFields", contact.getAllFields());
			model.addAttribute("allNum", allNum);
			model.addAttribute("allEmail", allEmail);
			model.addAttribute("threeNum", threeNum);
			model.addAttribute("mediaLinks", mediaLink);
			model.addAttribute("headings", headings);
			contact = null;
			allNum = Collections.emptyMap();
			description = "";
			allEmail = Collections.emptyMap();
			headings = Collections.emptyMap();
			mediaLink = Collections.emptyMap();
			threeNum = Collections.emptyMap();
			
			long endTime = System.currentTimeMillis(); // Record end time
			long duration = endTime - startTime; // Calculate elapsed time
			logger.info("Execution Time: " + duration + " ms");

			return "contact-info";
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page Not Found");
		}

	}

	@GetMapping("/terms")
	public String termsPage() {
		return "/OTHER/terms";
	}

	@GetMapping("/hyperlink_policy")
	public String hyperlink_policyPage() {
		return "/OTHER/hyperlink_policy";
	}

	@GetMapping("/privacy_policy")
	public String privacy_policyPage() {
		return "/OTHER/privacy_policy";
	}

	@GetMapping("/cookie-policy")
	public String cookie_policyPage() {
		return "/OTHER/cookies_policy";
	}

	@GetMapping("/disclimer")
	public String disclimerPage() {
		return "/OTHER/disclimer";
	}

	@GetMapping("/addContactManually")
	public String addContactManuallyPage() {
		return "/OTHER/add_contact_manually";
	}

	@GetMapping("/contactUsPage")
	public String myContactUsPage() {
		return "/OTHER/contact";
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public String handleInvalidId() {
		return "redirect:/";
	}

	@PostMapping("/searchSuggession")
	@ResponseBody
	public String suggessionPage(@RequestParam("character") String character) {
		if(character != null || !character.trim().equals("")){
			String result = daoService.jsonResponse(character);
			return result;
		}
		return null;
	}

	@GetMapping("/error")
	public String handleError() {
		return "error"; // Refers to error.html in the templates folder
	}

	/*
	 * @GetMapping("/addSeo")
	 * public String addSeo() {
	 * int c = daoService.addSeo();
	 * int t = daoService.addTitle();
	 * int d = daoService.addDescription();
	 * System.out.println("Total : "+c+" Row Updated");
	 * System.out.println("Total : "+t+" Row Updated");
	 * System.out.println("Total : "+d+" Row Updated");
	 * return "dashboard";
	 * }
	 */
	@GetMapping("/csrf")
	public ResponseEntity<Map<String, String>> getCsrfToken(CsrfToken token) {
		Map<String, String> csrfMap = new HashMap<>();
		csrfMap.put("token", token.getToken());
		csrfMap.put("headerName", token.getHeaderName());
		return ResponseEntity.ok(csrfMap); // Return a ResponseEntity with the CSRF token
	}

	@GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
	public String sitemapPage(Model model) {
		List<Map<String, String>> list = daoService.newSitemap();
		model.addAttribute("sitemap", list);
		return "/OTHER/sitemap";
	}
}

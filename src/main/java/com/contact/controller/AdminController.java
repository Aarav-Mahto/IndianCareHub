package com.contact.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.contact.dao.DAOService;
import com.contact.entity.Contact;
import com.contact.entity.QuestionAnchor;
import com.contact.entity.Register;
import com.contact.hepler.Helper;
import com.contact.security.AuthenticationDone;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	public static final Logger logger = Logger.getLogger(AdminController.class.getName());

	@Autowired
	private Contact contact;

	@Autowired
	private QuestionAnchor questionAnchor;

	@Autowired
	private DAOService daoService;

	@Autowired
	private AuthenticationDone authenticationDone;
	Map<String, String> userCredential = Collections.emptyMap();

	@Autowired
	private Helper helper;

	@Autowired
	HttpSession session;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void userSession() {
		this.userCredential = authenticationDone.getUserInfoSession();
		session.setAttribute("fullName", userCredential.get("name"));
		session.setAttribute("userEmail", userCredential.get("email"));
		session.setAttribute("accessType", userCredential.get("accessType"));
	}

	@PostMapping("/keep-alive")
	@ResponseBody
    public String keepAlive(HttpSession session) {
        return "Session is alive";
    }

	@GetMapping("/btnNavigate/{view}")
	public String btnNavigateX(@PathVariable("view") String view) {
		helper.validateSession(session);
		return "redirect:/" + view;
	}

	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}

	@GetMapping("/insert")
	public String insertPage(Model model) {
		model.addAttribute("view", "insertion");
		model.addAttribute("industry", daoService.AllSeoedIndustry());
		return "dashboard";
	}

	@GetMapping("/update")
	public String updatePage(Model model) {
		model.addAttribute("view", "searchForUpdate");
		return "dashboard";// "/CRUD/update";
	}

	@GetMapping("/delete")
	public String deletePage(Model model) {
		model.addAttribute("view", "searchForDelete");
		return "dashboard";// "/CRUD/delete";
	}

	@GetMapping("/register")
	public String registerPage(Model model) {
		helper.validateSession(session);
		Register register = new Register();
		model.addAttribute("view", "registerPage");
		model.addAttribute("register", register);
		return "dashboard";
	}

	// ===============Seo Keywords==============
	@GetMapping("/seo_keywords")
	public String seoPage(Model model) {
		model.addAttribute("view", "seo_keywords");
		return "dashboard";
	}

	@GetMapping("/seo_heading")
	public String seoHeading(Model model) {
		model.addAttribute("view", "seo_heading");
		return "dashboard";
	}

	// ====================================
	@PostMapping("/insertData")
	public String insertData(
			@RequestParam("org_full_name") String orgFullName,
			@RequestParam("org_alias_name") String orgAliasName,
			@RequestParam("pageUrl") String pageUrl,
			@RequestParam("page_title") String pageTitle, // Edited
			@RequestParam("org_logo") MultipartFile orgLogo,
			@RequestParam("About_Company") String aboutCompany,
			@RequestParam("org_address") String orgAddress,
			@RequestParam("Web_Link") String websiteLink,
			@RequestParam("industryName") String industryName,
			@RequestParam(value = "Department_Name", required = false) List<String> departName,
			@RequestParam(value = "PhoneEmail", required = false) List<String> departNumber,
			@RequestParam(value = "socialMediaName", required = false) List<String> socialMediaName,
			@RequestParam(value = "socialMediaLinks", required = false) List<String> socialMediaLinks,

			@RequestParam(value = "anchorName", required = false) List<String> anchorName,
			@RequestParam(value = "anchorLink", required = false) List<String> anchorLink,
			@RequestParam(value = "question", required = false) List<String> question,
			@RequestParam(value = "answer", required = false) List<String> answer

	) {
		helper.validateSession(session);
		userCredential = authenticationDone.getUserInfoSession();
		// =========== Question And Anchor Links==============================
		if (!helper.isNullOrEmptyList(anchorName, anchorLink)) {
			for (int i = 0; i < anchorName.size(); i++) {
				if (!helper.isNullOrEmpty(anchorName.get(i), anchorLink.get(i))) {
					questionAnchor.getLink().put(anchorName.get(i).trim(), anchorLink.get(i).trim());
				}
			}
		}
		if (!helper.isNullOrEmptyList(question, answer)) {
			for (int i = 0; i < question.size(); i++) {
				if (!helper.isNullOrEmpty(question.get(i), answer.get(i))) {
					questionAnchor.getQuestion().put(question.get(i).trim(), answer.get(i).trim());
				}
			}
		}
		// ===================================================================
		contact.setOrg_Full_Name(orgFullName);
		contact.setOrg_Short_Name(orgAliasName);
		contact.setPageUrl(pageUrl);
		contact.setPageTitle(pageTitle.trim());
		contact.setOrg_Icon(orgLogo.getOriginalFilename());
		contact.setAbout_Company(aboutCompany);
		contact.setAddress(orgAddress);
		contact.setWeb_Link(websiteLink);
		contact.setNm(departNumber);
		contact.setDept(departName);
		contact.setIndustryName(industryName);

		// ================= Social Media Links ====================
		Map<String, String> name_links = Collections.emptyMap();
		if (!helper.isNullOrEmptyList(socialMediaName, socialMediaLinks)) {
			if (daoService.isUrl(pageUrl.trim())) {
				helper.setMsgSession(session, "'" + pageUrl.trim() + "' URL Already Exist!", "darkred");
				return "redirect:/insert";
			}
			name_links = new LinkedHashMap<>();
			for (int i = 0; i < socialMediaName.size(); i++) {
				if (socialMediaName.get(i) != null && socialMediaLinks.get(i) != null
						&& !socialMediaLinks.get(i).equals("")) {
					name_links.put(socialMediaName.get(i), socialMediaLinks.get(i));
				} else {
					logger.warning(socialMediaName.get(i) + " link is not provided!");
				}
			}
		}
		if (name_links != null || !name_links.isEmpty()) {
			contact.setMedia_links(name_links);
			name_links = Collections.emptyMap();
		}
		// ==================================================================
		Map<String, String> name_number = new LinkedHashMap<>();
		for (int i = 0; i < departName.size(); i++) {
			name_number.put(departName.get(i), departNumber.get(i));
		}

		contact.setAllFields(name_number);
		name_number = null;
		contact.setPartFile(orgLogo);
		String getName = userCredential != null ? userCredential.get("name") : "No Name";
		contact.setEntered_By(getName);
		contact.setEntry_Date(helper.currentDate());
		contact.setUpdated_by("");
		contact.setUpdated(null);

		String imgUpload = helper.uploadImage(orgLogo);
		if (imgUpload != null || !(imgUpload.isEmpty())) {
			int result = daoService.insertContact(contact, questionAnchor);
			if (result > 0) {
				helper.setMsgSession(session, result + " Contact Inserted!", "darkgreen");
			} else {
				helper.setMsgSession(session,
						"Error! Check The Application Console (Press Back Button for Entered Data)", "darkred");
			}
		} else {
			helper.setMsgSession(session, "Error! During Uploading Image", "darkred");
		}
		return "redirect:/insert";
	}

	@PostMapping("/updateFound_Post")
	public String updateFound_PostPage(@RequestParam("searchId") String searchId) {
		helper.validateSession(session);
		return "redirect:/updateFound?searchId=" + searchId;
	}

	@GetMapping("/updateFound")
	public String updateFoundPage1(@RequestParam("searchId") String searchId, Model model) {
		helper.validateSession(session);
		userCredential = authenticationDone.getUserInfoSession();
		model.addAttribute("industry", daoService.AllSeoedIndustry());

		if (searchId != null ||!searchId.trim().equals("") || !(searchId.length() < 1)) {
			int sid = Integer.parseInt(searchId);
			if (sid > 0) {
				Map<String, Object> result = daoService.searchId(sid);
				Contact contact = (Contact) result.get("contact");
				QuestionAnchor qn_link = (QuestionAnchor) result.get("questionAnchor");
				if (qn_link != null) {
					model.addAttribute("anchorLink", qn_link.getAnchorMapList());
					model.addAttribute("questionAns", qn_link.getQnMapList());
					model.addAttribute("socialLinksForUpdate", qn_link.getSocialLinkUpdate());
				}
				if (contact != null) {
					if (contact.getDept_num() == null || contact.getDept_num().isEmpty()) {
						model.addAttribute("view", "searchForUpdate");
						helper.setMsgSession(session, "Found Data is Currupted!", "darkred");
						return "dashboard";
					}
					model.addAttribute("contact", contact);
					Map<String, String> allDetails = contact.getAllFields();
					Map<Integer, List<String>> dept_num = contact.getDept_num();
					model.addAttribute("allDetails", allDetails);
					model.addAttribute("dept_num", dept_num);
					//
					//session.setAttribute("old_Img_url", allDetails.get("org_old_logo"));
					contact.setOrg_Icon(allDetails.get("org_old_logo"));

					model.addAttribute("view", "updateFound");
					helper.setMsgSession(session, "Data Found Edit Now!", "darkgreen");
					return "dashboard";
				} else {
					model.addAttribute("view", "searchForUpdate");
					helper.setMsgSession(session, "Data Not Present!", "darkred");
					return "dashboard";
				}

			} else {
				model.addAttribute("view", "searchForUpdate");
				helper.setMsgSession(session, "May be Query not executed!", "darkred");
				return "dashboard";
			}
		} else {
			model.addAttribute("view", "searchForUpdate");
			helper.setMsgSession(session, "Not a valid Search Id!", "darkred");
			return "dashboard";
		}

	}

	@PostMapping("/updateFoundUpdateNow")
	public String updateFoundUpdateNowData(
			@RequestParam("Entry_no") String Entry_no,
			@RequestParam("update_org_name") String orgFullName,
			@RequestParam("update_org_alias") String orgAliasName,
			@RequestParam("update_pageTitle") String pageTitle,
			@RequestParam("pageUrl") String pageUrl,

			@RequestParam("update_org_logo") MultipartFile orgLogo,
			@RequestParam("orgLogoName") String orgLogoName,
			@RequestParam("update_About_Company") String aboutCompany,
			@RequestParam("update_org_address") String orgAddress,
			@RequestParam("update_Web_Link") String websiteLink,
			@RequestParam("industryName") String industryName,

			@RequestParam(value = "socialSno", required = false) List<String> socialSno,
			@RequestParam(value = "socialCompanyId", required = false) List<String> socialCompanyId,
			@RequestParam(value = "socialMediaName", required = false) List<String> socialMediaName,
			@RequestParam(value = "socialMediaLinks", required = false) List<String> socialMediaLinks,

			@RequestParam(value = "anchorSno", required = false) List<String> anchorSno,
			@RequestParam(value = "anchorCompanyId", required = false) List<String> anchorCompanyId,
			@RequestParam(value = "anchorName", required = false) List<String> anchorName,
			@RequestParam(value = "anchorLink", required = false) List<String> anchorLink,

			@RequestParam(value = "qaSno", required = false) List<String> qaSno,
			@RequestParam(value = "qaCompanyId", required = false) List<String> qaCompanyId,
			@RequestParam(value = "question", required = false) List<String> question,
			@RequestParam(value = "answer", required = false) List<String> answer,

			@RequestParam(value = "record_no", required = false) List<String> record_no,
			@RequestParam(value = "company_id", required = false) List<String> company_id,
			@RequestParam(value = "Department_Name", required = false) List<String> departName,
			@RequestParam(value = "PhoneEmail", required = false) List<String> departNumber) {
		helper.validateSession(session);
		userCredential = authenticationDone.getUserInfoSession();
		if (helper.isNullOrEmpty(orgFullName, orgAliasName, pageTitle, pageUrl, aboutCompany, orgAddress, websiteLink)) {
			helper.setMsgSession(session, "* Every Fields are mandatory!", "darkred");
			return "redirect:/updateFound?searchId=" + Entry_no;
		}
		if (helper.isNullOrEmptyList(departName, departNumber)) {
			helper.setMsgSession(session, "Please Enter Some Contact Number or Email!", "darkred");
			return "redirect:/updateFound?searchId=" + Entry_no;
		}

		// =========== Question And Anchor Links==============================
		questionAnchor.setSocialLinkUpdate(qn_anchor_map(socialSno, socialCompanyId, socialMediaName, socialMediaLinks));
		questionAnchor.setAnchorMapList(qn_anchor_map(anchorSno, anchorCompanyId, anchorName, anchorLink));
		questionAnchor.setQnMapList(qn_anchor_map(qaSno, qaCompanyId, question, answer));
		contact.setDept_num(qn_anchor_map(record_no, company_id, departName, departNumber));

		contact.setSno(Integer.parseInt(Entry_no));
		contact.setOrg_Full_Name(orgFullName);
		contact.setOrg_Short_Name(orgAliasName);
		contact.setPageTitle(pageTitle);
		contact.setNm(departNumber);
		contact.setDept(departName);
		contact.setAbout_Company(aboutCompany);
		contact.setAddress(orgAddress);
		contact.setWeb_Link(websiteLink);
		contact.setIndustryName(industryName);
		contact.setPageUrl(pageUrl);

		String userMail = userCredential != null ? userCredential.get("userEmail") : "NotAMail@Id.Found";
		contact.setUpdated_by(userMail);
		contact.setUpdated(helper.currentDate());

		int result = 0;
		if (orgLogo == null || orgLogo.isEmpty() || orgLogo.getSize() <= 0) {
			contact.setOrg_Icon(orgLogoName);
		}
		result = daoService.updateContact(contact, questionAnchor);
		if (result > 0) {
			String imgUpload = null;
			boolean delete_Image = false;
			if (orgLogo == null || orgLogo.isEmpty() || orgLogo.getSize() <= 0) {
				helper.setMsgSession(session, result + " Contact Updated in Entry No. "+Entry_no+" (But You didn't input Image file)!", "green");
				return "redirect:/update";
			} else {
				contact.setPartFile(orgLogo);
				contact.setOrg_Icon(orgLogo.getOriginalFilename());
				imgUpload = helper.uploadImage(orgLogo);
				delete_Image = helper.deleteFile(orgLogoName);

				if (delete_Image && !helper.isNullOrEmpty(imgUpload)) {
					helper.setMsgSession(session, result + " Contact And Image Updated in Entry No. "+Entry_no+"!", "darkgreen");
				}
				if (delete_Image && helper.isNullOrEmpty(imgUpload)) {
					helper.setMsgSession(session,
							result + " Contact Updated & Old Image Deleted But New Image Not Uploaded in Entry No. "+Entry_no+"!", "hotpink");
				}
				if (!delete_Image && !helper.isNullOrEmpty(imgUpload)) {
					helper.setMsgSession(session,
							result + " Contact Updated & New Image Uploaded But Old Image Not Deleted in Entry No. "+Entry_no+"!", "hotpink");
				}
				if (!delete_Image && helper.isNullOrEmpty(imgUpload)) {
					helper.setMsgSession(session, result + " Contact Updated But Image Not Updated in Entry No. "+Entry_no+"!", "green");
				}
				return "redirect:/update";
			}
		} else {
			helper.setMsgSession(session, "Error! During Updating Image in Entry No. "+Entry_no+"", "darkred");
			return "redirect:/updateFound?searchId=" + Entry_no;
		}

	}

	private Map<Integer, List<String>> qn_anchor_map(List<String> Sno, List<String> CompanyId, List<String> question,
			List<String> answer) {
		if (!helper.isNullOrEmptyList(Sno, CompanyId, question, answer)) {
			Map<Integer, List<String>> tempMap = new LinkedHashMap<>();
			for (int i = 0; i < question.size(); i++) {
				List<String> tempList = new ArrayList<>();
				if (!helper.isNullOrEmpty(question.get(i), answer.get(i))) {
					tempList.add(Sno.get(i));
					tempList.add(CompanyId.get(i));
					tempList.add(question.get(i).trim());
					tempList.add(answer.get(i).trim());
				}
				tempMap.put(i, tempList);
				tempList = Collections.emptyList();
			}
			return tempMap;
		}
		return Collections.emptyMap();
	}

	// ===========Delete Operation =========================

	@PostMapping("/deleteFound_Post")
	public String deleteFound_PostPage(@RequestParam("deleteId") String searchId) {
		helper.validateSession(session);
		return "redirect:/deleteFound?deleteId=" + searchId;
	}

	@GetMapping("/deleteFound")
	public String deleteFoundPage(@RequestParam("deleteId") String searchId, Model model) {
		helper.validateSession(session);
		if (!searchId.trim().equals("") && searchId.trim() != "" && searchId.trim() != null
				&& !searchId.trim().equals(null)) {
			int sid = Integer.parseInt(searchId);
			if (sid > 0) {
				Map<String, Object> result = daoService.searchId(sid);
				Contact contact = (Contact) result.get("contact");
				if (contact != null) {
					model.addAttribute("contact", contact);
					Map<String, String> allDetails = contact.getAllFields();
					Map<Integer, List<String>> dept_num = contact.getDept_num();
					model.addAttribute("allDetails", allDetails);
					model.addAttribute("dept_num", dept_num);

					model.addAttribute("view", "deleteFound");
					helper.setMsgSession(session, "Delete Permanently!", "darkgreen");
					return "dashboard";
				} else {
					model.addAttribute("view", "searchForDelete");
					helper.setMsgSession(session, "Error! Contact not found", "darkred");
					return "dashboard";
				}
			} else {
				helper.setMsgSession(session, "Enter a valid search id!", "darkred");
				model.addAttribute("view", "searchForDelete");
				return "dashboard";
			}
		} else {
			helper.setMsgSession(session, "Enter a valid search id!", "darkred");
			model.addAttribute("view", "searchForDelete");
			return "dashboard";
		}

	}

	@PostMapping("/deleteFoundDeleteNow")
	public String deleteFoundDeleteNow(@RequestParam("Entry_no") String Sno,
			@RequestParam("imageName") String imageName, Model model) {
		helper.validateSession(session);
		int Entry_no = Integer.parseInt(Sno);
		if (Entry_no > 0) {
			boolean deleteing = daoService.deleteEntry(Entry_no);
			boolean deleteImage = helper.deleteFile(imageName);
			if (deleteing) {
				if (deleteImage) {
					helper.setMsgSession(session, "Data Deleted Successfully", "darkgreen");
					model.addAttribute("view", "searchForDelete");
					return "redirect:/delete";
				} else {
					helper.setMsgSession(session, "Data Deleted But Image Not Deleted(Error)!", "yellow");
					model.addAttribute("view", "deleteFound");
					return "redirect:/deleteFound?deleteId=" + Entry_no;
				}

			} else if (deleteImage) {
				if (deleteing) {
					helper.setMsgSession(session, "Data Deleted Successfully", "darkgreen");
					model.addAttribute("view", "searchForDelete");
					return "redirect:/delete";
				} else {
					model.addAttribute("view", "deleteFound");
					helper.setMsgSession(session, "Image(logo) Deleted But Data Not Deleted!", "yellow");
					return "redirect:/deleteFound?deleteId=" + Entry_no;
				}

			} else {
				model.addAttribute("view", "deleteFound");
				helper.setMsgSession(session, "Image(logo) Deleted But Data Not Deleted!", "darkred");
				return "redirect:/deleteFound?deleteId=" + Entry_no;
			}
		} else {
			System.out.println("Something went wrong with table id!");
			model.addAttribute("view", "deleteFound");
			helper.setMsgSession(session, "Image(logo) Deleted But Data Not Deleted!", "darkred");
			return "redirect:/deleteFound?deleteId=" + Entry_no;
		}
	}

	@GetMapping("/showDB")
	public String showDBPage(Model model) {
		List<Map<String, String>> list = daoService.showDB();
		helper.validateSession(session);
		if (list.size() == 0) {
			helper.setMsgSession(session, "No Record Fetched!", "red");
			model.addAttribute("view", "showDB");
			return "dashboard";
		}
		model.addAttribute("list", list);
		model.addAttribute("view", "showDB");
		return "dashboard";
	}
	@GetMapping("/showDB_ByOrder/{order}")
	public String showDB_ByOrder(@PathVariable("order") String order ,Model model) {
		List<Map<String, String>> list = daoService.showDB_ByOrder(order);
		helper.validateSession(session);
		if (list.size() == 0) {
			helper.setMsgSession(session, "No Record Fetched!", "red");
			model.addAttribute("view", "showDB");
			return "dashboard";
		}
		model.addAttribute("list", list);
		model.addAttribute("view", "showDB");
		return "dashboard";
	}

	@PostMapping("/showAbout")
	@ResponseBody
	public String showAbout(@RequestParam("postId") String postid) {
		helper.validateSession(session);
		int searchId = Integer.parseInt(postid);
		String about = daoService.loadAboutOrgOfId(searchId);
		if (about != null && about != "") {
			return about;
		}
		return "No About Available For This Contact!";
	}

	@PostMapping("/showContacts")
	@ResponseBody
	public ResponseEntity<Map<Integer, List<String>>> showContacts(@RequestParam("postId") String postid) {
		helper.validateSession(session);
		int searchId = Integer.parseInt(postid);
		Map<Integer, List<String>> listMap = daoService.loadAllContactsOfId(searchId);
		if (listMap != null && !listMap.isEmpty()) {
			return new ResponseEntity<>(listMap, HttpStatus.OK);
		}
		return null;
	}

	// ================================ Seo Headings
	// ====================================================

	@PostMapping("/insertSeoHeading")
	public String insertSeoData(
			@RequestParam("Selected_Industry") String Industry,
			@RequestParam("heading1") String heading1,
			@RequestParam("heading2") String heading2,
			@RequestParam("heading3") String heading3,
			@RequestParam("heading4") String heading4) {

		// if(Industry != null && Industry.equalsIgnoreCase("test")){
		// helper.setMsgSession(session, "Failled! Select a Category!","darkred");
		// return "redirect:/seo_heading";
		// }
		if (!(helper.isNullOrEmpty(Industry, heading1, heading2, heading3, heading4))) {
			Map<String, String> temp = new LinkedHashMap<>();
			temp.put("Industry", Industry.trim());
			temp.put("heading1", heading1.trim());
			temp.put("heading2", heading2.trim());
			temp.put("heading3", heading3.trim());
			temp.put("heading4", heading4.trim());

			int inserting = daoService.insertIndustrySeo(temp);

			if (inserting > 0) {
				helper.setMsgSession(session, "Data Inserted Successfully", "darkgreen");
			} else {
				helper.setMsgSession(session, "Failled!", "darkred");
			}
		} else {
			helper.setMsgSession(session, "Failled! All Fields Are Mandatory!", "darkred");
		}
		return "redirect:/seo_heading";
	}

	// ===========Register Now ======================================
	@PostMapping("/registerNow")
	public String registerNow(@ModelAttribute("register") Register register, RedirectAttributes redirectAttributes) {
		helper.validateSession(session);
		redirectAttributes.addFlashAttribute("register", register);
		return "redirect:/registerData";
	}

	@GetMapping("/registerData")
	public String registerDataNow(@ModelAttribute("register") Register register, Model model) {
		helper.validateSession(session);
		userCredential = authenticationDone.getUserInfoSession();

		if (helper.isNullOrEmpty(register.getUnique_id(), register.getFullName(), register.getEmail_id(),
				register.getPhone(), register.getAddress(), register.getUser_Type(), register.getPassword(),
				register.getCPassword())) {
			helper.setMsgSession(session, "*Every Fields are Mandatory!", "darkred");
			model.addAttribute("view", "registerPage");
			model.addAttribute("register", register);
			return "dashboard";
		}
		if (daoService.registrationIdExist(register.getUnique_id())) {
			helper.setMsgSession(session, register.getUnique_id() + " Already Exist!", "darkred");
			model.addAttribute("view", "registerPage");
			model.addAttribute("register", register);
			return "dashboard";
		}
		if (daoService.registrationEmailIdExist(register.getEmail_id())) {
			helper.setMsgSession(session, register.getEmail_id() + " Already Exist!", "darkred");
			model.addAttribute("view", "registerPage");
			model.addAttribute("register", register);
			return "dashboard";
		}
		if (register.getPassword() != null && register.getCPassword() != null
				&& !register.getPassword().equals(register.getCPassword())) {
			helper.setMsgSession(session, "Password is differ from Confirm Password!", "darkred");
			model.addAttribute("view", "registerPage");
			model.addAttribute("register", register);
			return "dashboard";
		}

		String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=])[A-Za-z0-9@#$%^&+=]*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(register.getPassword());
		if (!matcher.matches()) {
			helper.setMsgSession(session,
					"Password Must Contains at least one, Uppercae, Lowercase, 0-9 digit, Special Character!",
					"darkred");
			model.addAttribute("view", "registerPage");
			model.addAttribute("register", register);
			return "dashboard";
		}

		if (register.getUnique_id().length() <= 5) {
			helper.setMsgSession(session, "Unique Id Must be at least 6 characters.", "darkred");
			model.addAttribute("view", "registerPage");
			model.addAttribute("register", register);
			return "dashboard";
		}
		if (register.getPhone().length() < 10 || register.getPhone().length() > 13) {
			helper.setMsgSession(session, "Enter a valid phone number!", "darkred");
			model.addAttribute("view", "registerPage");
			model.addAttribute("register", register);
			return "dashboard";
		}

		String pass = passwordEncoder.encode(register.getPassword());
		register.setPassword(pass);
		register.setDate(helper.currentDate());
		register.setRegistered(userCredential.get("name"));
		try {

			Register duplicateId = daoService.findUserId(register.getUnique_id());
			if (duplicateId != null) {
				helper.setMsgSession(session, register.getUnique_id() + " Unique Id Already Exist!", "darkred");
				model.addAttribute("view", "registerPage");
				model.addAttribute("register", register);
				return "dashboard";
			}
			Register duplicateEmail = daoService.findUserEmail(register.getEmail_id());
			if (duplicateEmail != null) {
				helper.setMsgSession(session, register.getEmail_id() + " Email Id Already Exist!", "darkred");
				model.addAttribute("view", "registerPage");
				model.addAttribute("register", register);
				return "dashboard";
			}

			int result = daoService.registration(register);
			if (result > 0) {
				helper.setMsgSession(session, "Registration Successfully Done.", "darkgreen");
				return "redirect:/register";
			} else {
				helper.setMsgSession(session, "Registration Failled (database insertion error)", "darkred");
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("view", "registerPage");
			helper.setMsgSession(session, "Exception Occurred! Check log.", "darkred");
			model.addAttribute("register", register);
			return "dashboard";
		}

		model.addAttribute("view", "registerPage");
		model.addAttribute("register", register);
		return "dashboard";
	}


	///=========== Show Database ====================
	@GetMapping("/databasePage/{order}")
	public String showDatabase(@PathVariable("order") String order, Model model) {
		List<Map<String, String>> list = daoService.database(order);
		helper.validateSession(session);
		if (list.size() == 0) {
			helper.setMsgSession(session, "No Record Fetched!", "red");
			model.addAttribute("view", "showDB");
			return "dashboard";
		}
		model.addAttribute("list", list);
		model.addAttribute("view", "showDB");
		return "showdata";
	}
	@PostMapping("/showDbSearch")
	public ResponseEntity<List<Map<String, String>>> suggessionPage(@RequestParam("character") String character) {
		System.out.println(character);
		if(character != null || !character.trim().equals("")){
			List<Map<String, String>> result = daoService.databaseSearch(character);
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.ok(Collections.emptyList());
	}

}

// name_links = new LinkedHashMap<>();
// for (int i = 0; i < socialMediaName.size(); i++) {
// if (socialMediaName.get(i) != null && socialMediaLinks.get(i) != null
// && !socialMediaLinks.get(i).equals("")) {
// if (socialMediaName.get(i).equalsIgnoreCase("faq")) {
// String links = "<a href='" + socialMediaLinks.get(i)
// + "' target='_blank' rel='noopener noreferrer'>𝙁𝘼𝙌𝙨</a>";
// name_links.put(socialMediaName.get(i), links);
// } else {
// String links = "<a href='" + socialMediaLinks.get(i)
// + "' target='_blank' rel='noopener noreferrer'><span>'" +
// socialMediaName.get(i)
// + "'</span><i class='" + helper.socialLinksFormater(socialMediaName.get(i))
// + "'></i></a>";
// name_links.put(socialMediaName.get(i), links);
// }
// } else {
// logger.warning(socialMediaName.get(i) + " link is not provided!");
// }
// }

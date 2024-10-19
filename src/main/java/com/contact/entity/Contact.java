package com.contact.entity;

import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class Contact {

	private int Sno;
	private String Org_Full_Name;
	private String Org_Short_Name;
	private String pageUrl;
	private String pageTitle;
	private String Org_Icon;
	private String About_Company;
	private String Address;
	private String Web_Link;
	private Date Entry_Date;
	private String Entered_By;
	private Date Updated;
	private String Updated_by;
	private String industryName;
	
	//new
	private String SeoKeywords;
	private String pageDesc;
	
	//New List
	private List<String> nm;
	private List<String> dept;
	
	private MultipartFile partFile;
	
	private Map<String, String> allFields = new LinkedHashMap<>();
	private Map<String, String> media_links = new LinkedHashMap<>();
	private Map<String, String> seoHeading = new LinkedHashMap<>();

	// private Map<String, String> media_links = new LinkedHashMap<>();
	// private Map<Integer, List<String>> mediaLinksMapList = new LinkedHashMap<>();
	
	//private Map<String, String> numbers = new LinkedHashMap<>();
	
	private Map<Integer, List<String>> dept_num = new LinkedHashMap<>();
	
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Map<Integer, List<String>> getDept_num() {
		return dept_num;
	}

	public void setDept_num(Map<Integer, List<String>> dept_num) {
		this.dept_num = dept_num;
	}
	
	public int getSno() {
		return Sno;
	}

	public void setSno(int sno) {
		Sno = sno;
	}

	public String getOrg_Full_Name() {
		return Org_Full_Name;
	}

	public void setOrg_Full_Name(String org_Full_Name) {
		Org_Full_Name = org_Full_Name;
	}

	public String getOrg_Short_Name() {
		return Org_Short_Name;
	}

	public void setOrg_Short_Name(String org_Short_Name) {
		Org_Short_Name = org_Short_Name;
	}

	public String getOrg_Icon() {
		return Org_Icon;
	}

	public void setOrg_Icon(String org_Icon) {
		Org_Icon = org_Icon;
	}

	public String getAbout_Company() {
		return About_Company;
	}

	public void setAbout_Company(String about_Company) {
		About_Company = about_Company;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getWeb_Link() {
		return Web_Link;
	}

	public void setWeb_Link(String web_Link) {
		Web_Link = web_Link;
	}

	public Date getEntry_Date() {
		return Entry_Date;
	}

	public void setEntry_Date(Date entry_Date) {
		Entry_Date = entry_Date;
	}

	public String getEntered_By() {
		return Entered_By;
	}

	public void setEntered_By(String entered_By) {
		Entered_By = entered_By;
	}

	public Date getUpdated() {
		return Updated;
	}

	public void setUpdated(Date updated) {
		Updated = updated;
	}

	public String getUpdated_by() {
		return Updated_by;
	}

	public void setUpdated_by(String updated_by) {
		Updated_by = updated_by;
	}

	public MultipartFile getPartFile() {
		return partFile;
	}

	public void setPartFile(MultipartFile partFile) {
		this.partFile = partFile;
	}

	//new variable
	
	public String getSeoKeywords() {
		return SeoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		SeoKeywords = seoKeywords;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getPageDesc() {
		return pageDesc;
	}

	public void setPageDesc(String pageDesc) {
		this.pageDesc = pageDesc;
	}

	public List<String> getNm() {
		return nm;
	}

	public void setNm(List<String> nm) {
		this.nm = nm;
	}

	public List<String> getDept() {
		return dept;
	}

	public void setDept(List<String> dept) {
		this.dept = dept;
	}

	public Map<String, String> getMedia_links() {
		return media_links;
	}

	public void setMedia_links(Map<String, String> media_links) {
		this.media_links = media_links;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public Map<String, String> getAllFields() {
		return allFields;
	}

	public void setAllFields(Map<String, String> allFields) {
		this.allFields = allFields;
	}

	public Map<String, String> getSeoHeading() {
		return seoHeading;
	}

	public void setSeoHeading(Map<String, String> seoHeading) {
		this.seoHeading = seoHeading;
	}

	
	
	
}




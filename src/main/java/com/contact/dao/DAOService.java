package com.contact.dao;


import java.util.List;
import java.util.Map;

import com.contact.entity.Contact;
import com.contact.entity.QuestionAnchor;
import com.contact.entity.Register;

public interface DAOService {

	public int insertContact(Contact contact, QuestionAnchor questionAnchor);
	
	public int updateContact(Contact contact, QuestionAnchor questionAnchor);
	
	public Map<String, Object> searchId(int searchId);
	
	public Map<String,Object> pageLoading(String url);
	
	public boolean deleteEntry(int Entry_no);
	
	public List<Map<String, String>> showDB();
	public List<Map<String, String>> showDB_ByOrder(String order);
	public List<Map<String, String>> database(String order);
	public List<Map<String, String>> databaseSearch(String charct);
	
	public String loadAboutOrgOfId(int id);
	
	public Map<Integer, List<String>> loadAllContactsOfId(int id);
	
	public boolean registrationIdExist(String rid);
	
	public boolean registrationEmailIdExist(String rid);
	
	public int registration(Register register);
	
	public Register findUserId(String uid);
	
	public Register findUserEmail(String email);
	
	public String jsonResponse(String character);
	
	//public List<Map<String, String>> sitemap();
	/*
	public int addSeo();
	
	public int addTitle();
	
	public int addDescription();
	*/
	public int insertIndustrySeo(Map<String,String> headings);
	public List<String> AllSeoedIndustry();

	public boolean isUrl(String url);

	public List<Map<String,String>> newSitemap();
	
}


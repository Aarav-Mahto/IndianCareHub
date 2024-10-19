package com.contact.entity;

import java.sql.Date;

public class Register {

	private String Unique_id;
	private String FullName; 
	private String Email_id;
	private String Phone;
	private String Address;
	private String User_Type;
	private String Password;
	private String CPassword;
	private Date date; 

	private String registered;
	
	public Register() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUnique_id() {
		return Unique_id;
	}

	public void setUnique_id(String unique_id) {
		Unique_id = unique_id;
	}

	public String getFullName() {
		return FullName;
	}

	public void setFullName(String fullName) {
		FullName = fullName;
	}

	public String getEmail_id() {
		return Email_id;
	}

	public void setEmail_id(String email_id) {
		Email_id = email_id;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getUser_Type() {
		return User_Type;
	}

	public void setUser_Type(String user_Type) {
		User_Type = user_Type;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}
	
	public String getCPassword() {
		return CPassword;
	}

	public void setCPassword(String cPassword) {
		CPassword = cPassword;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRegistered() {
		return registered;
	}

	public void setRegistered(String registered) {
		this.registered = registered;
	}
	
	
}

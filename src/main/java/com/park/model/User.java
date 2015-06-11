package com.park.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class User {

	@JsonIgnore
	private int Id;
	private String userName;
	private String number;
	private String passwd;
	
	public String getUserName() {
		return userName;
	}
	public String getNumber() {
		return number;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	
	
}

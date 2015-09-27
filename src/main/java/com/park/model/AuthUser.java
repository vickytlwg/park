package com.park.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class AuthUser {

	@JsonIgnore
	private int Id;
	private String username;
	private String password;
	
	public int getId() {
		return Id;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public void setId(int id) {
		Id = id;
	}
	public void setUsername(String userName) {
		this.username = userName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}

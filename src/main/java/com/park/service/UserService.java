package com.park.service;

import java.util.List;

import com.park.model.User;

public interface UserService {

	List<User> getUsers();
	
	String insertUser(User userItem);
	
	String getUserPassword(String userName);
	
	public boolean userExistByUserName(String userName);
	
	public boolean userExistByNumber(String number);
}

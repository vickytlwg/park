package com.park.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.park.model.User;

@Repository
public interface UserDAO {

	List<User> getUsers();
	
	int insertUser(User userItem);
	
	String getUserPassword(String userName);
	
	int getUserCountByUserName(String userName);
	
	int getUserCountByNumber(String number);
	
}

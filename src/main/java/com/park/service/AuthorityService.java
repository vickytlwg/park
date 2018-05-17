package com.park.service;

import java.util.List;

import com.park.model.AuthUser;

public interface AuthorityService {

	boolean checkUserAccess(String username, String password);
	
	AuthUser getUserByUsername(String username);
	
	int getUserCount();
	
	public List<String> getOwnUserParkName(int userId);
	
	public List<AuthUser> getUsers();
	
	public List<AuthUser> getUsersByCount(int start,int count);
	
	public String getUserPasswd(String username);
	
	public AuthUser getUser(String username);
	
	public int insertUser(AuthUser user,  List<Integer> parkIds);
	
	public int deleteUser(int id);
	
	public int updateUser(AuthUser user, List<Integer> parkIds);
}

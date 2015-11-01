package com.park.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.park.model.AuthUser;

@Repository
public interface AuthorityDAO {
	
	public List<AuthUser> getUsers();
	public String getUserPasswd(String username);
	
	public AuthUser getUser(String username);
}

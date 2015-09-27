package com.park.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityDAO {
	
	public String getUserPasswd(String username);
}

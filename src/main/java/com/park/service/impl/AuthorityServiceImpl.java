package com.park.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.park.dao.AuthorityDAO;
import com.park.model.AuthUser;
import com.park.service.AuthorityService;

@Transactional
@Service
public class AuthorityServiceImpl implements AuthorityService{

	@Autowired
	private AuthorityDAO authDao;
	
	@Override
	public boolean checkUserAccess(String username, String password) {
		AuthUser user = authDao.getUser(username);
		if(user == null)
			return false;
		return password.equals(user.getPassword());
	}

}

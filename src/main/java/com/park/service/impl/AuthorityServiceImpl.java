package com.park.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.park.dao.AuthorityDAO;
import com.park.service.AuthorityService;

@Transactional
@Service
public class AuthorityServiceImpl implements AuthorityService{

	@Autowired
	private AuthorityDAO authDao;
	
	@Override
	public boolean checkUserAccess(String username, String password) {
		String pwd = authDao.getUserPasswd(username);
		return password.equals(pwd);
	}

}

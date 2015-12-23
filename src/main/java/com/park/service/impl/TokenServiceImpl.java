package com.park.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.park.dao.TokenDAO;
import com.park.service.TokenService;

@Transactional
@Service
public class TokenServiceImpl implements TokenService{
	
	@Autowired
	private TokenDAO tokenDAO;

	@Override
	public boolean validToken(String token) {
		if(tokenDAO.getTokenCount(token) > 0)
			return true;
		return false;
	}

}

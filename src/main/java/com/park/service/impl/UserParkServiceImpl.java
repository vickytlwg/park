package com.park.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.park.dao.UserParkDAO;
import com.park.service.UserParkService;

@Transactional
@Service
public class UserParkServiceImpl implements UserParkService{

	@Autowired
	private UserParkDAO userParkDAO; 
	
	@Override
	public List<Integer> getOwnParkId(int userId) {
		
		return userParkDAO.getOwnParkId(userId);
	}

}

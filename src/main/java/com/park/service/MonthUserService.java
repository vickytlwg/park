package com.park.service;

import java.util.List;

import com.park.model.Monthuser;

public interface  MonthUserService {

	    int deleteByPrimaryKey(Integer id);

	    int insert(Monthuser record);

	    int insertSelective(Monthuser record);

	    Monthuser selectByPrimaryKey(Integer id);
	    
	    int updateByPrimaryKey(Monthuser record);
	    
	    int updateByPrimaryKeySelective(Monthuser record);
	    
	    int getCount();
	    
	    List<Monthuser> getByStartAndCount(int start,int count);
	    
	    List<Monthuser> getByCardNumber(String cardNumber);
	    	    
}

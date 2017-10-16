package com.park.service;

import java.time.Month;
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
	    
	    int getCountByParkId(int parkId);
	    
	    List<Monthuser> getByStartAndCount(int start,int count);
	    
	    List<Monthuser> getByStartAndCountOrder(int start,int count,int type);
	    
	    List<Monthuser> getByUsernameAndPark(String username,int parkId);
	    
	    List<Monthuser> getByCardNumber(String cardNumber);
	    
	    List<Monthuser> getByParkIdAndCount(int parkId,int start,int count);
	    
	    List<Monthuser> getByParkIdAndCountOrder(int parkId,int start,int count,int type);
	    	    
}

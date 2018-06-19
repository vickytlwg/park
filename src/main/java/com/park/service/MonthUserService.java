package com.park.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

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
	    
	    List<Monthuser> getByParkAndPort(int parkId,String portNumber);
	    
	    List<Monthuser> getByUserNameAndParkAndPort(String username,int parkId,String portNumber);
	    
	    List<Monthuser> getByCarnumberAndPark(String carnumber,int parkId);
	    
	    List<Monthuser> getByCardNumber(String cardNumber);
	    
	    List<Monthuser> getByParkIdAndCount(int parkId,int start,int count);
	    
	    List<Monthuser> getByParkIdAndCountOrder(int parkId,int start,int count,int type);
	   
	    Map<String, Object> statisticsInfo(int parkId,int type);
	    
	    
}

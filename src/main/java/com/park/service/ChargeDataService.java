package com.park.service;

import com.park.model.ChargedataPark;

public interface ChargeDataService {

	 	int deleteByPrimaryKey(Integer id);

	    int insert(ChargedataPark record);

	    int insertSelective(ChargedataPark record);

	    ChargedataPark selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(ChargedataPark record);

	    int updateByPrimaryKey(ChargedataPark record);
	    
	    void generateTable(int parkId);
}

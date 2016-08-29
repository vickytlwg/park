package com.park.service;

import java.util.List;

import com.park.model.Feeoperator;

public interface FeeOperatorService {
	int deleteByPrimaryKey(Integer id);

    int insert(Feeoperator record);

    int insertSelective(Feeoperator record);

    Feeoperator selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Feeoperator record);

    int updateByPrimaryKey(Feeoperator record);
    
    int getCount();
    
    List<Feeoperator> getByStartAndCount(int start,int count);
}

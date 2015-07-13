package com.park.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.park.model.BusinessCarport;
import com.park.model.BusinessCarportDetail;


@Repository
public interface BusinessCarportDAO {

	public List<BusinessCarport> getBusinessCarports();
	
	public BusinessCarport getBusinessCarportById(int id);
	
	public BusinessCarport getBusinessCarportByMacId(int macId);
	
	public int getParkLeftPort(int parkId);
	
	public int getBusinessCarportCount();
	
	public List<BusinessCarportDetail> getBusinessCarportDetail(@Param("low")int low, @Param("count")int count);
	
	public int updateBusinessCarportStatus(int macId, int status);
	
	public int insertBusinessCarport(BusinessCarport businessCarport);
	
	public int updateBusinessCarport(BusinessCarport businessCarport);
	
	public int deleteBusinessCarport(int id);
}

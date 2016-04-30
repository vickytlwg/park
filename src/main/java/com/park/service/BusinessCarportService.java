package com.park.service;

import java.util.List;

import com.park.model.BusinessCarport;
import com.park.model.BusinessCarportDetail;
import com.park.model.CarportStatusDetail;

public interface BusinessCarportService {

	public List<BusinessCarport> getBusinessCarports();
	
	public BusinessCarport getBusinessCarportById(int id);
	
	public int getBusinessCarportCount(Integer parkId);
	
	public List<BusinessCarportDetail> getBusinessCarportDetail(int low, int count, Integer parkId);
	
	public int insertBusinessCarport(BusinessCarport businessCarport);
	
	public int updateBusinessCarport(BusinessCarport businessCarport);
	
	public int updateBusinessCarportStatus(String mac, int status);
	
	public int deleteBusinessCarport(int id);
	
	public int getCarportStatusDetailCount();
	
	public List<CarportStatusDetail> getCarportStatusDetailByCarportId(int carportId);
	
	public List<CarportStatusDetail> getLimitCarportStatusDetail(int start, int len);
	
	
	public List<CarportStatusDetail> getDetailByCarportId(int carportId);
	
	public CarportStatusDetail getLatestDetailByCarportId(int carportId);
	
}

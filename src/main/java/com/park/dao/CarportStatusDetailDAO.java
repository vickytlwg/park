package com.park.dao;

import java.util.List;

import com.park.model.CarportStatusDetail;

public interface CarportStatusDetailDAO {
	
	public int count();
	public List<CarportStatusDetail> get();
	
	public List<CarportStatusDetail> limitGet(int start, int len);
	
	public List<CarportStatusDetail> getDetailByCarportId(int carportId);
	
	public CarportStatusDetail getLatestDetailByCarportId(int carportId);
	
	public int insert(CarportStatusDetail detail);
	
	public int update(CarportStatusDetail detail);
	

}

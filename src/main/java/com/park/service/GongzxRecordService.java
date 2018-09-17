package com.park.service;

import java.util.List;

import com.park.model.GongzxRecord;
import com.park.model.PosChargeData;

public interface GongzxRecordService {
	public int gongcount();
	
	public List<GongzxRecord> getByParkAuthority(String userName);
	
	public List<GongzxRecord> getPageByParkId(int parkId,int start,int count);
	
	public List<GongzxRecord> getByCarNumber(String carNumber);
	
	public List<GongzxRecord> getByCarNumberAndPark(String carNumber,int parkId);
	
	public List<GongzxRecord> getByParkName(String parkName);
}

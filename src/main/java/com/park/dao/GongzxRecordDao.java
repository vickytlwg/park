package com.park.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.park.model.GongzxRecord;
import com.park.model.PosChargeData;

@Repository
public interface GongzxRecordDao {
	public int gongcount();
	
	public List<GongzxRecord> getByParkAuthority(String userName);
	
	public List<GongzxRecord> getPageByParkId(@Param("parkId")int parkId,@Param("start")int start,@Param("count")int count);
	
	public List<GongzxRecord> getByCarNumber(@Param("carNumber")String carNumber);
	
	public List<GongzxRecord> getByCarNumberAndPark(@Param("carNumber")String carNumber,@Param("parkId")int parkId);
	
	public List<GongzxRecord> getByParkName(@Param("parkName")String parkName);
	
	public int update(GongzxRecord record);
	public int insert(GongzxRecord record);
}

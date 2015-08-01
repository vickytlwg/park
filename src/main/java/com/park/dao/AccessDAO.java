package com.park.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.park.model.Access;
import com.park.model.AccessDetail;

@Repository
public interface AccessDAO {
	
	public List<Access> getAccesses();
	

	public List<Map<String, Object>> getHourCountByPark(@Param("parkId")int parkId, @Param("date")String date);

	public List<Map<String, Object>> getHourCountByChannel(@Param("parkId")int parkId, @Param("date")String date);

	public List<Map<String, Object>> getMonthCountByPark(@Param("parkId")int parkId, @Param("year")int year);

	public List<Map<String, Object>> getMonthCountByChannel(@Param("parkId")int parkId, @Param("year")int year);
	
	public List<Map<String, Object>> getChannelHourCount(@Param("macId")int macId, @Param("date")String date);
	
	public List<Map<String, Object>> getChannelMonthCount(@Param("macId")int macId, @Param("year")int year);
	
	
	public int insertAccess(Access item);
	
	public int getAccessCount();
	
	public int getParkAccessCount(int parkId);
	
	public int updateAccess(Access access);
	
	public int deleteAccess(int id);
	
	public List<AccessDetail> getAccessDetail(@Param("low")int low, @Param("count")int count);
	
	public List<AccessDetail> getParkAccessDetail(@Param("low")int low, @Param("count")int count, @Param("parkId")int parkId);

}

package com.park.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.park.model.Access;
import com.park.model.AccessDetail;

@Repository
public interface AccessDAO {
	
	//public List<Access> getAccesses();
	

	public List<Map<String, Object>> getHourCountByPark(@Param("parkId")int parkId, @Param("date")String date,@Param("table")String table);

	public List<Map<String, Object>> getHourCountByChannel(@Param("parkId")int parkId, @Param("date")String date,@Param("table")String table);

	public List<Map<String, Object>> getDayCountByPark(@Param("parkId")int parkId, @Param("date")String date, @Param("table")String table);

	//public List<Map<String, Object>> getMonthCountByChannel(@Param("parkId")int parkId, @Param("year")int year);
	
	public List<Map<String, Object>> getChannelHourCount(@Param("macId")int macId, @Param("date")String date,@Param("table")String table);
	
	//public List<Map<String, Object>> getChannelMonthCount(@Param("macId")int macId, @Param("year")int year);
	
	public int getParkIdByChanelId(int channelId);
	
	public int insertAccess(@Param("item")Access item,@Param("table")String table);
	
	//public int getAccessCount();
	
	public int getParkAccessCount(@Param("parkId")int parkId, @Param("table")String table);
	
	//public int updateAccess(@Param("access")Access access,@Param("table")String table);
	
	//public int deleteAccess(@Param("id")int id,@Param("table")String table);
	
	//public List<AccessDetail> getAccessDetail(@Param("low")int low, @Param("count")int count);
	
	public List<AccessDetail> getParkAccessDetail(@Param("low")int low, @Param("count")int count, @Param("parkId")int parkId, @Param("table")String table);
	public int getAllAccessCount(@Param("xmo")int xmo,@Param("ymonth")int ymonth);
}

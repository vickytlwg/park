package com.park.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.park.model.Park;

@Repository
public interface ParkDAO {

	public List<Park> getParks();
	
	public Park getParkById(@Param("id")int id);
	
	public int nameToId(String name);
	
	public List<Park> getParkByName(String name);
	
	public List<Park> getParkDetail(@Param("low")int low, @Param("count")int count);
	
	public int getParkCount();
	
	public int insertPark(Park park);
	
	public int insertParkList(List<Park> parks);
	
	public int updatePark(Park park);
	
	public int updateLeftPortCount(@Param("id")int parkId, @Param("portLeftCount") int leftPortCount);
	
	public int deletePark(int id);
}

package com.park.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.park.model.Park;

public interface ParkService {
	
	public List<Park> getParks();
	
	public int nameToId(String name);
	
	public List<Park> getParkByName(String name);
	
	public List<Park> getParkDetail(int low, int count);
	
	public int getParkCount();
	
	public String insertPark(Park park);
	
	public String insertParkList(List<Park> parks);
	
	public String updatePark(Park park);
	
	public int updateLeftPortCount(int parkId, int leftPortCount);
	
	public String deletePark(int Id);

}

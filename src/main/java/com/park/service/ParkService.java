package com.park.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.park.model.Park;
import com.park.model.ParkNews;

public interface ParkService {
	
	public List<Park> filterPark(List<Park> parks, String username);
	
	public List<Park> filterPark(List<Park> parks, int userId);
	
	public List<Park> getParks();
	
	public List<Park> getNearParks(double longitude, double latitude, double radius);
	
	public Park getParkById(int id);
	
	public int nameToId(String name);
	
	public List<Park> getParkByName(String name);
	
	public List<Park> getParkDetail(int low, int count);
	
	public List<Park> getParkDetailByKeywords(String keywords);
	
	public int getParkCount();
	
	public String insertPark(Park park);
	
	public String insertParkList(List<Park> parks);
	
	public String updatePark(Park park);
	
	public int updateLeftPortCount(int parkId, int leftPortCount);
	
	public String deletePark(int Id);
	
	public List<ParkNews> getSearchParkLatestNews(double longitude, double latitude, double radius, int offset, int pageSize);

	public int insertParkNews(ParkNews parkNews);
	
	public Park getLastPark();
}

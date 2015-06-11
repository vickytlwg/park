package com.park.service;

import java.util.List;

import com.park.model.Park;

public interface ParkService {
	
	public List<Park> getParks();
	
	public String insertPark(Park park);
	
	public String insertParkList(List<Park> parks);
	
	public String updatePark(Park park);
	
	public String deletePark(int Id);

}

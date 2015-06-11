package com.park.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.park.model.Park;

@Repository
public interface ParkDAO {

	public List<Park> getParks();
	
	public int insertPark(Park park);
	
	public int insertParkList(List<Park> parks);
	
	public int updatePark(Park park);
	
	public int deletePark(int Id);
}

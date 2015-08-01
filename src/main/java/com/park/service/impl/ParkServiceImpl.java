package com.park.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.park.dao.ParkDAO;
import com.park.model.Park;
import com.park.service.ParkService;

@Transactional
@Service
public class ParkServiceImpl implements ParkService{
	
	@Autowired
	private ParkDAO parkDAO;
	
	public List<Park> getParks(){
		return parkDAO.getParks();
		
	}
	
	@Override
	public Park getParkById(int id) {
		return parkDAO.getParkById(id);
	}
	
	public int nameToId(String name){
		return parkDAO.nameToId(name);
	}
	
	public List<Park> getParkByName(String name){
		return parkDAO.getParkByName(name);
	}
	
	public int getParkCount(){
		return parkDAO.getParkCount();
	}
	
	public String insertPark(Park park){
		Map<String, Object> map = new HashMap<String, Object>();
		if(parkDAO.insertPark(park) > 0){
			map.put("status", "1001");
			map.put("message", "insert success");
		}else{
			map.put("status", "1002");
			map.put("message", "insert fail");
		}
		return new Gson().toJson(map);
	}
	
	public String insertParkList(List<Park> parks){
		Map<String, Object> map = new HashMap<String, Object>();
		int sum = 0;
		for (Park park : parks) {
			sum += parkDAO.insertPark(park);
		}
		if(sum == parks.size()){
			map.put("status", "1001");
			map.put("message", "insert success");
		}else{
			map.put("status", "1002");
			map.put("message",  sum + " success " + parks.size() + " fail");
		}
		return new Gson().toJson(map);
		
	}
	
	public String updatePark(Park park){
		Map<String, Object> map = new HashMap<String, Object>();
		if(parkDAO.updatePark(park) > 0){
			map.put("status", "1001");
			map.put("message", "update success");
		}else{
			map.put("status", "1002");
			map.put("message", "update fail");
		}
		return new Gson().toJson(map);
		
	}
	
	@Override
	public int updateLeftPortCount(int parkId, int leftPortCount) {
		return parkDAO.updateLeftPortCount(parkId, leftPortCount);
	}
	
	public String deletePark(int Id){
		Map<String, Object> map = new HashMap<String, Object>();
		if(parkDAO.deletePark(Id) > 0){
			map.put("status", "1001");
			map.put("message", "delete success");
		}else{
			map.put("status", "1002");
			map.put("message", "delete fail");
		}
		return new Gson().toJson(map);		
	}

	@Override
	public List<Park> getParkDetail(int low, int count) {
		
		return parkDAO.getParkDetail(low, count);
	}



	

}

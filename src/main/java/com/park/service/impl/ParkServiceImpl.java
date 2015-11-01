package com.park.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.park.dao.AuthorityDAO;
import com.park.dao.ParkDAO;
import com.park.model.AuthUser;
import com.park.model.AuthUserRole;
import com.park.model.Park;
import com.park.service.ParkService;
import com.park.service.UserParkService;
import com.park.service.Utility;

@Transactional
@Service
public class ParkServiceImpl implements ParkService{
	
	@Autowired
	private ParkDAO parkDAO;
	
	@Autowired
	private AuthorityDAO authDAO;
	
	@Autowired
	private UserParkService userParkService;
	
	public List<Park> getParks(){
		return parkDAO.getParks();
		
	}
	
	
	@Override
	public List<Park> filterPark(List<Park> parks, String username) {
		if(parks == null)
			return null;
		 AuthUser authUser = authDAO.getUser(username);
		 if(authUser == null)
			return null;
		else if(authUser.getRole() == AuthUserRole.ADMIN.getValue())
			return parks;
		else
			return filterPark(parks, authUser.getId());
	}

	@Override
	public List<Park> filterPark(List<Park> parks, int userId) {
		if(parks == null)
			return parks;
		
		List<Integer> filterParkIds = userParkService.getOwnParkId(userId);
		Set<Integer> filterParkIdSet = new HashSet<Integer>(filterParkIds);
		
		List<Park> resultParks = new ArrayList<Park>();
		for(Park park : parks){
			if(filterParkIdSet.contains(park.getId()))
				resultParks.add(park);
		}
		
		return resultParks;
	}
	
	
	
	public List<Park> getNearParks(double longitude, double latitude, double radius){
		List<Park> parks = this.getParks();
		List<Park> nearParks = new ArrayList<Park>();
		for(int i = 0; i < parks.size(); i++){
			Park tempPark = parks.get(i);
			
			double distance = Utility.GetDistance(latitude, longitude, tempPark.getLatitude(), tempPark.getLongitude());
			if(distance < radius){
				nearParks.add(tempPark);
			}
		}
		return nearParks;
	}
	
	@Override
	public Park getParkById(int id) {
		return parkDAO.getParkById(id);
	}
	
	public int nameToId(String name){
		return parkDAO.nameToId(name);
	}
	
	public List<Park> getParkByName(String name){
		// use like function to find park
		return parkDAO.getParkByName("%" + name + "%");
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

	@Override
	public List<Park> getParkDetailByKeywords(String keywords) {
		// TODO Auto-generated method stub
		return parkDAO.getParkDetailByKeywords(keywords);
	}





	

}

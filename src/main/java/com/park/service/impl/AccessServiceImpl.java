package com.park.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.park.dao.AccessDAO;
import com.park.dao.HardwareDAO;
import com.park.model.Access;
import com.park.model.AccessDetail;
import com.park.model.ChannelType;
import com.park.service.AccessService;

@Transactional
@Service
public class AccessServiceImpl implements AccessService{
	
	@Autowired
	private AccessDAO accessDAO;
	
	@Autowired
	private HardwareDAO hardwareDAO;
	
	@Override
	public List<Access> getAccesses(){
		return accessDAO.getAccesses();
	}
	
	@Override
	public List<AccessDetail> getAccessDetail(int low, int count) {
		return accessDAO.getAccessDetail(low, count);
	}

	@Override
	public int getAccessCount() {
		return accessDAO.getAccessCount();
	}

	
	@Override
	public Map<String, Map<Integer, Integer>> getHourCountByPark(int parkId, String date) {
		List<Map<String, Object>> rets = accessDAO.getHourCountByPark(parkId, date);
		Map<String, Map<Integer, Integer>> body = new HashMap<String, Map<Integer, Integer>>();
		
		Map<Integer, Integer> exitMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> entranceMap = new HashMap<Integer, Integer>();
		for(int i = 0 ; i < rets.size(); i++){
			Map<String, Object> item = rets.get(i);
			
			if((int)item.get("channelFlag") == ChannelType.EXIT.getValue()){
				 exitMap.put((int)item.get("hour"), Integer.parseInt(item.get("count").toString()));
			}else{
				entranceMap.put((int)item.get("hour"), Integer.parseInt(item.get("count").toString()));
			}
			
		}
		
		body.put("exit", exitMap);
		body.put("entrance", entranceMap);
		return body;
	}

	@Override
	public Map<String, Map<Integer, Integer>> getHourCountByChannel(int parkId, String date) {
		
		List<Map<String, Object>> rets = accessDAO.getHourCountByChannel(parkId, date);
		Map<String, Map<Integer, Integer>> body = new HashMap<String, Map<Integer, Integer>>();
		
		for(int i = 0 ; i < rets.size(); i++){
			Map<String, Object> item = rets.get(i);
			int macId = (int)item.get("macId");
			String mac = hardwareDAO.getHardwareById(macId).getMac();
			if(!body.containsKey(mac)){
				body.put(mac, new HashMap<Integer, Integer>());
			}
			Map<Integer, Integer> macMap = body.get(mac);
			macMap.put((int)item.get("hour"), Integer.parseInt(item.get("count").toString()));
				
		}
		
		return body;
	}

	@Override
	public Map<String, Map<Integer, Integer>> getMonthCountByPark(int parkId, int year) {
		
		List<Map<String, Object>> rets =accessDAO.getMonthCountByPark(parkId, year);
		Map<String, Map<Integer, Integer>> body = new HashMap<String, Map<Integer, Integer>>();
		
		Map<Integer, Integer> exitMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> entranceMap = new HashMap<Integer, Integer>();
		for(int i = 0 ; i < rets.size(); i++){
			Map<String, Object> item = rets.get(i);
			
			if((int)item.get("channelFlag") == ChannelType.EXIT.getValue()){
				 exitMap.put((int)item.get("month"), Integer.parseInt(item.get("count").toString()));
			}else{
				entranceMap.put((int)item.get("month"), Integer.parseInt(item.get("count").toString()));
			}
			
		}
		body.put("exit", exitMap);
		body.put("entrance", entranceMap);
		return body;
	}

	@Override
	public Map<String, Map<Integer, Integer>> getMonthCountByChannel(int parkId, int year) {
		List<Map<String, Object>> rets = accessDAO.getMonthCountByChannel(parkId, year);
		Map<String, Map<Integer, Integer>> body = new HashMap<String, Map<Integer, Integer>>();
		
		for(int i = 0 ; i < rets.size(); i++){
			Map<String, Object> item = rets.get(i);
			int macId = (int)item.get("macId");
			String mac = hardwareDAO.getHardwareById(macId).getMac();
			if(!body.containsKey(mac)){
				body.put(mac, new HashMap<Integer, Integer>());
			}
			Map<Integer, Integer> macMap = body.get(mac);
			macMap.put((int)item.get("month"), Integer.parseInt(item.get("count").toString()));
		}
		return body;
	}
	
	
	@Override
	public Map<Integer, Integer> getChannelHourCount(int macId, String date) {
		List<Map<String, Object>> rets = accessDAO.getChannelHourCount(macId, date);
		Map<Integer, Integer> body = new HashMap<Integer, Integer>();
		
		for(int i = 0 ; i < rets.size(); i++){
			Map<String, Object> item = rets.get(i);
			body.put((int)item.get("hour"), Integer.parseInt(item.get("count").toString()));
		}
		return body;
	}

	@Override
	public Map<Integer, Integer> getChannelMonthCount(int macId, int year) {
		List<Map<String, Object>> rets = accessDAO.getChannelMonthCount(macId, year);
		Map<Integer, Integer> body = new HashMap<Integer, Integer>();
		
		for(int i = 0 ; i < rets.size(); i++){
			Map<String, Object> item = rets.get(i);
			body.put((int)item.get("month"), Integer.parseInt(item.get("count").toString()));
		}
		return body;
	}
	
	
	@Override
	public String insertAccess(Access item){
		Map<String, Object> map = new HashMap<String, Object>();
		if(accessDAO.insertAccess(item) > 0){
			map.put("status", "1001");
			map.put("message", "insert success");
		}else{
			map.put("status", "1002");
			map.put("message", "insert fail");
		}
		return new Gson().toJson(map);
	}

	@Override
	public String insertAccessList(List<Access> accesses){
		Map<String, Object> map = new HashMap<String, Object>();
		int sum = 0;
		for(Access access: accesses){
			sum += accessDAO.insertAccess(access);
		}
		if(sum == accesses.size()){
			map.put("status", "1001");
			map.put("message", "insert success");
		}else{
			map.put("status", "1002");
			map.put("message",  sum + " success " + accesses.size() + " fail");
		}
		return new Gson().toJson(map);
	}
	
	@Override
	public String updateAccess(Access access){
		Map<String, Object> map = new HashMap<String, Object>();
		if(accessDAO.updateAccess(access) > 0){
			map.put("status", "1001");
			map.put("message", "update success");
		}else{
			map.put("status", "1002");
			map.put("message", "update fail");
		}
		return new Gson().toJson(map);
	}
	
	@Override
	public String deleteAccess(int Id){
		Map<String, Object> map = new HashMap<String, Object>();
		if(accessDAO.deleteAccess(Id)> 0){
			map.put("status", "1001");
			map.put("message", "delete success");
		}else{
			map.put("status", "1002");
			map.put("message", "delete fail");
		}
		return new Gson().toJson(map); 
	}


}

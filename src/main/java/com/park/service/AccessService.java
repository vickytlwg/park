package com.park.service;

import java.util.List;
import java.util.Map;

import com.park.model.Access;
import com.park.model.AccessDetail;

public interface AccessService {
	
	public List<Access> getAccesses();
	
	public String insertAccess(Access item,String table);
	
	public int getAccessCount(Integer parkId);
	
	
	public Map<String, Map<Integer, Integer>> getHourCountByPark(int parkId, String date,String table);
	
	public Map<String, Map<Integer, Integer>> getHourCountByChannel(int parkId, String date);
	
	public Map<String, Map<Integer, Integer>> getMonthCountByPark(int parkId, int year);
	
	public Map<String, Map<Integer, Integer>> getMonthCountByChannel(int parkId, int year);
	
	public Map<Integer, Integer> getChannelHourCount(int macId, String date);
	
	public Map<Integer, Integer> getChannelMonthCount(int macId, int year);
	
	public int getParkIdByChanellId(int channelId);
	public String insertAccessList(List<Access> accesses,String table);
	
	public String updateAccess(Access access,String table);
	
	public String deleteAccess(int Id,String table);
	
	public List<AccessDetail> getAccessDetail(int low, int count,Integer parkId,String table);

}

package com.park.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.park.dao.AccessDAO;
import com.park.model.Access;
import com.park.service.AccessService;

@Transactional
@Service
public class AccessServiceImpl implements AccessService{
	
	@Autowired
	private AccessDAO accessDAO;
	
	@Override
	public List<Access> getAccesses(){
		return accessDAO.getAccesses();
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

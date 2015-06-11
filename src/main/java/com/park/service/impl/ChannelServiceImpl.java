package com.park.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.park.dao.ChannelDAO;
import com.park.model.Channel;
import com.park.service.ChannelService;

@Transactional
@Service
public class ChannelServiceImpl implements ChannelService{
	@Autowired
	private ChannelDAO channelDAO;
	
	public List<Channel> getChannels(){
		return channelDAO.getChannels();
	}
	
	public String insertChannel(Channel channel){
		Map<String, Object> map = new HashMap<String, Object>();
		if(channelDAO.insertChannel(channel) > 0){
			map.put("status", "1001");
			map.put("message", "insert success");
		}else{
			map.put("status", "1002");
			map.put("message", "insert fail");
		}
		return new Gson().toJson(map);
	}
	
	public String insertChannelList(List<Channel> channels){
		Map<String, Object> map = new HashMap<String, Object>();
		int sum = 0;
		for(Channel channel : channels){
			sum += channelDAO.insertChannel(channel);
		}
		if(sum == channels.size()){
			map.put("status", "1001");
			map.put("message", "insert success");
		}else{
			map.put("status", "1002");
			map.put("message",  sum + " success " + channels.size() + " fail");
		}
		return new Gson().toJson(map);
	}
	
	public String updateChannel(Channel channel){
		Map<String, Object> map = new HashMap<String, Object>();
		if(channelDAO.updateChannel(channel) > 0){
			map.put("status", "1001");
			map.put("message", "update success");
		}else{
			map.put("status", "1002");
			map.put("message", "update fail");
		}
		return new Gson().toJson(map);
	}
	
	public String deleteChannel(int Id){
		Map<String, Object> map = new HashMap<String, Object>();
		if(channelDAO.deleteChannel(Id) > 0){
			map.put("status", "1001");
			map.put("message", "delete success");
		}else{
			map.put("status", "1002");
			map.put("message", "delete fail");
		}
		return new Gson().toJson(map);
	}

}

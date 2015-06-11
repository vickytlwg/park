package com.park.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.Channel;
import com.park.service.ChannelService;

@Controller
public class ChannelController {
	
	@Autowired
	private ChannelService channelService;
	
	@RequestMapping(value = "/insert/channel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertChannel(@RequestBody Channel channel){
		return channelService.insertChannel(channel);
	}
	
	@RequestMapping(value = "/channel", produces = {"application/json;charset=UTF-8"})
	public String getChannels(ModelMap modelMap, HttpServletRequest request){
		List<Channel> channelList = channelService.getChannels();
		modelMap.put("channels", channelList);					
		return "channelList";
	}
	
	@RequestMapping(value = "/update/channel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updateChannel(@RequestBody Channel channel){
		return channelService.updateChannel(channel);
	}
	
	@RequestMapping(value = "/delete/channel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String deleteChannel(@RequestBody Map<String, Object> channelMap){
		return channelService.deleteChannel((int)channelMap.get("Id"));
	}

}

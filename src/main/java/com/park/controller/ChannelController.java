package com.park.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.logging.Log;
import com.park.model.Channel;
import com.park.model.ChannelDetail;
import com.park.model.Hardware;
import com.park.model.Status;
import com.park.service.ChannelService;
import com.park.service.HardwareService;
import com.park.service.Utility;

@Controller
public class ChannelController {
	
	@Autowired
	private ChannelService channelService;
	@Autowired
	private HardwareService hardwareService;

	
	@RequestMapping(value = "/channel", produces = {"application/json;charset=UTF-8"})
	public String getChannels(ModelMap modelMap, HttpServletRequest request){
		List<Channel> channelList = channelService.getChannels();
		modelMap.put("channels", channelList);					
		return "channel";
	}
	
	@RequestMapping(value = "/getchannelCount", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getchannelCount(){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> body = new HashMap<String, Object>();
		int count = channelService.getchannelCount();
		body.put("count", count);
	
		ret.put("status", "1001");
		ret.put("message", "get channel detail success");
		ret.put("body", Utility.gson.toJson(body));
		
		return Utility.gson.toJson(ret);					
	}
	
	@RequestMapping(value = "/getchannelDetail", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String accessIndex(@RequestParam("low")int low, @RequestParam("count")int count){	
		Map<String, Object> ret = new HashMap<String, Object>();
		
		List<ChannelDetail> channelDetail = channelService.getChannelDetail(low, count);
		if(channelDetail != null){
			ret.put("status", "1001");
			ret.put("message", "get channel detail success");
			ret.put("body", Utility.gson.toJson(channelDetail));
		}else{
			ret.put("status", "1002");
			ret.put("message", "get channel detail fail");
		}
		return Utility.gson.toJson(ret);		
	}
	@RequestMapping(value = "/getParkchannelDetail", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String accessIndexpark(@RequestParam("low")int low, @RequestParam("count")int count,@RequestParam("parkId") Integer parkId){	
		Map<String, Object> ret = new HashMap<String, Object>();
		List<ChannelDetail> channelDetail = channelService.getParkChannelDetail(low, count,parkId);
		if(channelDetail != null){
			ret.put("status", "1001");
			ret.put("message", "get channel detail success");
			ret.put("body", Utility.gson.toJson(channelDetail));
		}else{
			ret.put("status", "1002");
			ret.put("message", "get channel detail fail");
		}
		return Utility.gson.toJson(ret);
		
	}
	@RequestMapping(value = "/insert/channel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertChannel1(@RequestBody Channel channel){
		int macId = channel.getMacId();
		Hardware hardware = hardwareService.getHardwareById(macId);
		if(hardware.getStatus() == Status.USED.getValue())
			return Utility.createJsonMsg("1003", "hard is being used");
		if(channelService.insertChannel(channel)){
			hardwareService.bindHardware(macId);
			return Utility.createJsonMsg("1001", "insert success");
		}else{
			return Utility.createJsonMsg("1002","insert failed");
		}
	}
	
	@RequestMapping(value = "/update/channel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updateChannel(@RequestBody Channel channel){
		
		Channel orignChannel = channelService.getChannelsById(channel.getId());
		int oldMacId = orignChannel.getMacId();
		int newMacId = channel.getMacId();
		
		if(channelService.updateChannel(channel)){
			if(newMacId != oldMacId){
				Hardware hardware = hardwareService.getHardwareById(newMacId);
				if(hardware.getStatus() == Status.USED.getValue())
					return Utility.createJsonMsg("1003", "hardware is being used");
				hardwareService.changeHardwareStatus(oldMacId, Status.UNUSED.getValue());
				hardwareService.changeHardwareStatus(newMacId, Status.USED.getValue());
			}
			return Utility.createJsonMsg("1001", "update success");
		}
		return Utility.createJsonMsg("1002", "update failed");
	}
	
	@RequestMapping(value = "/delete/channel/{Id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String deleteChannel(@PathVariable int Id){
		Channel channel = channelService.getChannelsById(Id);
		hardwareService.changeHardwareStatus(channel.getMacId(), Status.UNUSED.getValue());
		return channelService.deleteChannel(Id);
	}

}

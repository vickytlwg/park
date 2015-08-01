package com.park.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.Access;
import com.park.model.AccessDetail;
import com.park.service.AccessService;
import com.park.service.ChannelService;
import com.park.service.HardwareService;
import com.park.service.ParkService;
import com.park.service.Utility;

@Controller
public class AccessController {

	@Autowired
	private AccessService accessService;
	
	@Autowired
	private HardwareService hardwareService;
	
	@Autowired
	private ParkService parkService;
	
	
	@Autowired
	private ChannelService channelService;
	//private static Log logger = LogFactory.getLog(UserController.class);
	
	
	
	@RequestMapping(value = "/access", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String accessIndex(){	
		return "access";
	}
	
	@RequestMapping(value = "/getAccessDetail", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String accessIndex(@RequestParam("low")int low, @RequestParam("count")int count, @RequestParam(value = "parkId", required = false) Integer parkId){	
		Map<String, Object> ret = new HashMap<String, Object>();
		List<AccessDetail> accessDetail = accessService.getAccessDetail(low, count, parkId);
		if(accessDetail != null){
			ret.put("status", "1001");
			ret.put("message", "get access detail success");
			ret.put("body", Utility.gson.toJson(accessDetail));
		}else{
			ret.put("status", "1002");
			ret.put("message", "get access detail fail");
		}
		return Utility.gson.toJson(ret);
		
	}
	
	@RequestMapping(value = "/getAccessCount", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getAccessCount(@RequestParam(value = "parkId", required = false) Integer parkId){	
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> body = new HashMap<String, Object>();
		int count = accessService.getAccessCount(parkId);
		body.put("count", count);
		
		ret.put("status", "1001");
		ret.put("message", "get access detail success");
		ret.put("body", Utility.gson.toJson(body));
		
		return Utility.gson.toJson(ret);
		
	}
	
	@RequestMapping(value = "/getHourCountByPark", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getHourCountByPark(@RequestBody Map<String, Object> args){
		
		int parkId = -1;
		if(args.containsKey("parkId")){
			parkId = (int)args.get("parkId");
		}else{
			String parkName = (String)args.get("parkName");
			parkId = parkService.nameToId(parkName);
		}
		
		String date = (String)args.get("date");
		Map<String, Map<Integer, Integer>> body = accessService.getHourCountByPark(parkId, date);
		return Utility.createJsonMsg(1001, "get count success", body);
		
	}
	
	@RequestMapping(value = "/getHourCountByChannel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getHourCountByChannel(@RequestBody Map<String, Object> args){	
		int parkId = -1;
		if(args.containsKey("parkId")){
			parkId = (int)args.get("parkId");
		}else{
			String parkName = (String)args.get("parkName");
			parkId = parkService.nameToId(parkName);
		}
		String date = (String)args.get("date");
		Map<String, Map<Integer, Integer>> body = accessService.getHourCountByChannel(parkId, date);
		return Utility.createJsonMsg(1001, "get count success", body);
		
	}
	
	@RequestMapping(value = "/getMonthCountByPark", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getMonthCountByPark(@RequestBody Map<String, Object> args){	
		int parkId = -1;
		if(args.containsKey("parkId")){
			parkId = (int)args.get("parkId");
		}else{
			String parkName = (String)args.get("parkName");
			parkId = parkService.nameToId(parkName);
		}
		int year = (int)args.get("year");
		Map<String, Map<Integer, Integer>> body = accessService.getMonthCountByPark(parkId, year);
		return Utility.createJsonMsg(1001, "get count success", body);
		
	}
	
	@RequestMapping(value = "/getMonthCountByChannel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getMonthCountByChannel(@RequestBody Map<String, Object> args){	
		int parkId = -1;
		if(args.containsKey("parkId")){
			parkId = (int)args.get("parkId");
		}else{
			String parkName = (String)args.get("parkName");
			parkId = parkService.nameToId(parkName);
		}
		int year = (int)args.get("year");
		Map<String, Map<Integer, Integer>> body = accessService.getMonthCountByChannel(parkId, year);
		return Utility.createJsonMsg(1001, "get count success", body);
		
	}
	
	
	@RequestMapping(value = "/getChannelHourCount", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getChannelHourCount(@RequestBody Map<String, Object> args){	
		int macId = -1;
		if(args.containsKey("macId")){
			macId = (int)args.get("macId");
		}else{
			String mac = (String)args.get("mac");
			macId = hardwareService.macToId(mac);
		}
		String date = (String)args.get("date");
		Map<Integer, Integer> body = accessService.getChannelHourCount(macId, date);
		return Utility.createJsonMsg(1001, "get count success", body);
		
	}
	
	
	@RequestMapping(value = "/getChannelMonthCount", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getChannelMonthCount(@RequestBody Map<String, Object> args){	
		int macId = -1;
		if(args.containsKey("macId")){
			macId = (int)args.get("macId");
		}else{
			String mac = (String)args.get("mac");
			macId = hardwareService.macToId(mac);
		}
		int year = (int)args.get("year");
		Map<Integer, Integer> body = accessService.getChannelMonthCount(macId, year);
		return Utility.createJsonMsg(1001, "get count success", body);
		
	}
	
	
	@RequestMapping(value = "/insert/access", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertPark(@RequestBody Map<String, Object> argMap){
		
		String mac = (String)argMap.get("mac");
		Date date = new Date();
		int channelId = channelService.getChannelIdByMac(mac);
		if(channelId < 0){
			return Utility.createJsonMsg("1003","the hardware is not bound") + "\neof\n";
		}
		Access access = new Access();
		access.setDate(date);
		access.setChannelId(channelId);
		return accessService.insertAccess(access) + "\neof\n";
	}

	@RequestMapping(value = "/update/access", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updatePark(@RequestBody Access access){
		return accessService.updateAccess(access);
	}
	
	@RequestMapping(value = "/delete/access/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String deletePark(@PathVariable int id){
		return accessService.deleteAccess(id);
	}
}

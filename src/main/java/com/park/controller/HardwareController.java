package com.park.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.language.Caverphone1;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.Channel;
import com.park.model.Hardware;
import com.park.model.HardwareDetail;
import com.park.model.Status;
import com.park.service.ChannelService;
import com.park.service.HardwareService;
import com.park.service.Utility;

@Controller
public class HardwareController {
	
	@Autowired
	private HardwareService hardwareService;
	@Autowired
	private ChannelService channelService;
	
	@RequestMapping(value = "/hardware", produces = {"application/json;charset=UTF-8"})
	public String hardwares(ModelMap modelMap, HttpServletRequest request){
		return "hardware";
	}
	
	@RequestMapping(value = "/getHardwares", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getHardwares(ModelMap modelMap, HttpServletRequest request){
		Map<String, Object> ret = new HashMap<String, Object>();
		List<Hardware> hardwareList = hardwareService.getHardwares();
		ret.put("status", "1001");
		ret.put("message", "get hardware detail success");
		ret.put("body", hardwareList);
		
		return Utility.gson.toJson(ret);
	}
	
	@RequestMapping(value = "/getUnusedhardwares", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getUnusedHardwares(ModelMap modelMap, HttpServletRequest request){
		Map<String, Object> ret = new HashMap<String, Object>();
		List<Hardware> hardwareList = hardwareService.getUnusedHardwares();
		ret.put("status", "1001");
		ret.put("message", "get hardware detail success");
		ret.put("body", hardwareList);		
		return Utility.gson.toJson(ret);
	}
	@RequestMapping(value="/register/channel",method = RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String registerChannel(@RequestBody Map<String, Object> args){
		String macId=(String)args.get("mac");
		String parkId=(String)args.get("parkId");
		String channelId=(String)args.get("channelId");
		String channelFlag=(String)args.get("channelFlag");
		String hdDescription=(String)args.get("hardwareDescription");
		String channelDescription =(String)args.get("channelDescription");
		Hardware hd=new Hardware();
		hd.setMac(macId);
		if (Integer.parseInt(channelFlag)==2) {
			hd.setType(2);
		} else {
			hd.setType(1);
		}
		hd.setStatus(1);
		hd.setDescription(hdDescription);
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=format.format(date);
		hd.setDate(time);
		Channel chl=new Channel();
		
		if (hardwareService.insertHardware(hd)>0) {
			int macid=hardwareService.macToId(macId);
			chl.setMac(macid);
			chl.setChannelId(Integer.parseInt(channelId));
			chl.setChannelFlag(Integer.parseInt(channelFlag));		
			chl.setParkId(Integer.parseInt(parkId));
			chl.setStatus(1);
			chl.setIsEffective(1);
			chl.setDescription(channelDescription);
			chl.setDate(time);
			Hardware hardware = hardwareService.getHardwareById(macid);
			if(hardware.getStatus() == Status.USED.getValue())
				return Utility.createJsonMsg("msg", "hardare is being used");
			if(channelService.insertChannel(chl)){
				hardwareService.bindHardware(macid);
				return Utility.createJsonMsg("msg", "success");
		} else {
			return Utility.createJsonMsg("msg","register failure");
		}
	}
		else {
			return Utility.createJsonMsg("msg", "insert hardware error");
		}	
	}
	@RequestMapping(value = "/getUnBoundHardwares/{type}", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getUnBoundHardwares(@PathVariable int type){
		List<Hardware> hardwareList = hardwareService.getUnboundHardwares(type);
		return Utility.createJsonMsg("1001", "get hardware detail success", hardwareList);
	}
	
	@RequestMapping(value = "/getHardwareCount", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String gethardwareCount(){
		Map<String, Object> body = new HashMap<String, Object>();
		int count = hardwareService.getHardwareCount();
		body.put("count", count);
		return Utility.createJsonMsg("1001", "get hardware detail success", body);					
	}
	
	@RequestMapping(value = "/getHardwareDetail", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String accessIndex(@RequestParam("low")int low, @RequestParam("count")int count){	
		Map<String, Object> ret = new HashMap<String, Object>();
		List<HardwareDetail> hardwareDetail = hardwareService.getHardwareDetail(low, count);
		if(hardwareDetail != null){
			ret.put("status", "1001");
			ret.put("message", "get hardware detail success");
			ret.put("body", hardwareDetail);
		}else{
			ret.put("status", "1002");
			ret.put("message", "get hardware detail fail");
		}
		return Utility.gson.toJson(ret);
		
	}
	@RequestMapping(value = "/search/hardware", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String searchHardware(@RequestParam("mac")String mac){
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Hardware> hardware= hardwareService.searchHardware(mac);
		if(hardware != null)
		{
			retMap.put("state", "1001");
			retMap.put("message", "search hardware success");
			retMap.put("body", hardware);
		}
		else{
			retMap.put("state", "1002");
			retMap.put("message", "search hardware fail");
		}
		return  Utility.gson.toJson(retMap);
	}
	@RequestMapping(value = "/insert/hardware", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertHardware1(@RequestBody Hardware hardware){
		int ret =  hardwareService.insertHardware(hardware);
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(ret > 0 ){
			retMap.put("status", "1001");
			retMap.put("message", "get hardware detail success");
		}else{
			retMap.put("status", "1002");
			retMap.put("message", "get hardware detail fail");
		}
		return Utility.gson.toJson(retMap);
	}
	@RequestMapping(value = "/register/getInfoByMac", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getInfoByMac(@RequestBody Map<String, Object> argMap)
	{
		String mac = (String)argMap.get("mac");
		Map<String, Object> data=hardwareService.getInfoByMac(mac);
		return  Utility.gson.toJson(data);

	}
	@RequestMapping(value = "/update/hardware", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updateHardware(@RequestBody Hardware hardware){
		int ret = hardwareService.updateHardware(hardware);
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(ret > 0 ){
			retMap.put("status", "1001");
			retMap.put("message", "update hardware success");
		}else{
			retMap.put("status", "1002");
			retMap.put("message", "update hardware fail");
		}
		return Utility.gson.toJson(retMap);
	}
	
	@RequestMapping(value = "/delete/hardware/{Id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String deleteHardware(@PathVariable int Id){
		Map<String, Object> retMap = new HashMap<String, Object>();
		Hardware hardware = hardwareService.getHardwareById(Id);
		if(hardware.getStatus() == Status.USED.getValue()){
			retMap.put("status", "1003");
			retMap.put("message", "delete failed, please unbind the hardware ");
			return Utility.gson.toJson(retMap);
		}
		int ret = hardwareService.deleteHardware(Id);
		
		if(ret > 0 ){
			retMap.put("status", "1001");
			retMap.put("message", "delete hardware success");
		}else{
			retMap.put("status", "1002");
			retMap.put("message", "delete hardware fail");
		}
		return Utility.gson.toJson(retMap);
	}
}

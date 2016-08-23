package com.park.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.park.model.AuthUser;
import com.park.model.AuthUserRole;
import com.park.model.Channel;
import com.park.model.ChannelDetail;
import com.park.model.Hardware;
import com.park.model.Page;
import com.park.model.Status;
import com.park.service.AuthorityService;
import com.park.service.ChannelService;
import com.park.service.HardwareService;
import com.park.service.UserPagePermissionService;
import com.park.service.Utility;

@Controller
public class ChannelController {
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private HardwareService hardwareService;

	@Autowired
	private AuthorityService authService;
	
	@Autowired
	private UserPagePermissionService pageService;
	
	
	@RequestMapping(value = "/channel", produces = {"application/json;charset=UTF-8"})
	public String getChannels(ModelMap modelMap, HttpServletRequest request, HttpSession session){		
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		if(user != null){
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if(user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin=true;
			modelMap.addAttribute("isAdmin", isAdmin);
			
			Set<Page> pages = pageService.getUserPage(user.getId()); 
			for(Page page : pages){
				modelMap.addAttribute(page.getPageKey(), true);
			}
		}		
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
	@RequestMapping(value = "/getChannelDetailByKeywords", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getChannelDetailByKeywords(@RequestBody Map<String, Object> args){	
		Map<String, Object> ret = new HashMap<String, Object>();
		String keywords=(String) args.get("keywords");
		List<ChannelDetail> channelDetail = channelService.getChannelDetailByKeywords(keywords);
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
	@RequestMapping(value="/getChannelDetailByDate",method=RequestMethod.GET,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getChannelDetailByDate(@RequestParam("day")int day){
		Map<String, Object> ret = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String endday=df.format(cal.getTime());
		cal.add(Calendar.DAY_OF_MONTH, day);
		String startday=df.format(cal.getTime());
		List<ChannelDetail> channelDetail = channelService.getChannelDetailByDate(startday, endday);
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
		Integer macId = channel.getMacId();
		Hardware hardware = hardwareService.getHardwareById(macId);
		
		if(hardware!= null && hardware.getStatus() == Status.USED.getValue())
			return Utility.createJsonMsg("1003", "hard is being used");
		if(channelService.insertChannel(channel)){
			if(hardware != null)
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
		Integer oldMacId = orignChannel.getMacId();
		Integer newMacId = channel.getMacId();
		if(newMacId < 0)
			channel.setMac(null);
		
		if(channelService.updateChannel(channel)){
			if(newMacId != oldMacId){
				Hardware hardware = hardwareService.getHardwareById(newMacId);
				if(hardware != null && hardware.getStatus() == Status.USED.getValue())
					return Utility.createJsonMsg("1003", "hardware is being used");
				hardwareService.changeHardwareStatus(oldMacId, Status.UNUSED.getValue());
				if(hardware != null)
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
		if(channel.getMacId()!= null && channel.getMacId() > 0)
			hardwareService.changeHardwareStatus(channel.getMacId(), Status.UNUSED.getValue());
		return channelService.deleteChannel(Id);
	}

}

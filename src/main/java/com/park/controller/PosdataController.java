package com.park.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.AuthUser;
import com.park.model.AuthUserRole;
import com.park.model.Park;
import com.park.model.Posdata;
import com.park.service.AuthorityService;
import com.park.service.ParkService;
import com.park.service.PosdataService;
import com.park.service.Utility;

@Controller
@RequestMapping("/pos")
public class PosdataController {
	
@Autowired
private PosdataService posdataService;
@Autowired
private ParkService parkService;
@Autowired
private AuthorityService authService;

@RequestMapping(value = "/insertChargeDetail", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
@ResponseBody
public String insertPosdata(@RequestBody List<Posdata> posdata ){
	int listnum=posdata.size();
	int num=0;
	for(int i=0;i<listnum;i++)
	{
		num+=posdataService.insert(posdata.get(i));
	}
	
	Map<String, Object> retMap = new HashMap<String, Object>();
	if(num==listnum)
	{
		retMap.put("status", 1001);
		retMap.put("message", "success");
	}
	else
	{
		retMap.put("status", 1002);
		retMap.put("message", "failure");
	}
	return Utility.gson.toJson(retMap);
}
@RequestMapping(value="/getChargeDetail", produces = {"application/json;charset=UTF-8"})
@ResponseBody
public String getChargeDetail(){
	Map<String, Object> retMap = new HashMap<String, Object>();
	List<Posdata> posdatas=posdataService.selectAll();
	if(posdatas!=null)
	{
		retMap.put("status", 1001);
		retMap.put("message", "success");
		retMap.put("body", posdatas);
	}
	else
	{
		retMap.put("status", 1002);
		retMap.put("message", "failure");
	}
	return Utility.gson.toJson(retMap);
}
@RequestMapping(value="/chargeDetail")
public String chargeDetail(){
	return "posChargeDetail";
}
@RequestMapping(value="/carportUsage")
public String carportUsage(ModelMap modelMap, HttpServletRequest request, HttpSession session){
	List<Park> parkList = parkService.getParks();
	
	
	String username = (String) session.getAttribute("username");
	if(username != null)
		parkList = parkService.filterPark(parkList, username);
	modelMap.addAttribute("parks", parkList);
	
	AuthUser user = authService.getUserByUsername(username);
	if(user != null){
		modelMap.addAttribute("user", user);
		boolean isAdmin = false;
		if(user.getRole() == AuthUserRole.ADMIN.getValue())
			isAdmin=true;
		modelMap.addAttribute("isAdmin", isAdmin);
	}
	
	return "carportUsage";
}
@RequestMapping(value="getPosdataCount",method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
@ResponseBody
public String getPosdataCount(){
	Map<String, Object> retMap = new HashMap<String, Object>();
	Integer count=posdataService.getPosdataCount();
	if(count!=null)
	{
		retMap.put("status", 1001);
		retMap.put("message", "success");
		retMap.put("count", count);
	}
	else
	{
		retMap.put("status", 1002);
		retMap.put("message", "failure");
	}
	return Utility.gson.toJson(retMap);
}

@RequestMapping(value="selectPosdataByPage",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String selectPosdataByPage(@RequestBody Map<String,Object> args){
	int low=(Integer)args.get("low");
	int count=(Integer)args.get("count");
	Map<String, Object> retMap = new HashMap<String, Object>();
	List<Posdata> posdatas=posdataService.selectPosdataByPage(low, count);
	if(posdatas!=null)
	{
		retMap.put("status", 1001);
		retMap.put("message", "success");
		retMap.put("body", posdatas);
	}
	else
	{
		retMap.put("status", 1002);
		retMap.put("message", "failure");
	}
	return Utility.gson.toJson(retMap);
}

}

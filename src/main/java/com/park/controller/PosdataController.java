package com.park.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public String chargeDetail(ModelMap modelMap, HttpServletRequest request, HttpSession session){
	String username = (String) session.getAttribute("username");
	AuthUser user = authService.getUserByUsername(username);
	if(user != null){
		modelMap.addAttribute("user", user);
		boolean isAdmin = false;
		if(user.getRole() == AuthUserRole.ADMIN.getValue())
			isAdmin=true;
		modelMap.addAttribute("isAdmin", isAdmin);
	}
	return "posChargeDetail";
}
@RequestMapping(value="/carportUsage")
public String carportUsage(ModelMap modelMap, HttpServletRequest request, HttpSession session){
	List<Park> parkList = parkService.getParks();
	String username = (String) session.getAttribute("username");
	if(username != null)
		parkList = parkService.filterPark(parkList, username);
	List<Park> parkl = new ArrayList<>();
	for (Park park : parkList) {
		if (park.getType()==3) {
			parkl.add(park);
		}
	}
	modelMap.addAttribute("parks", parkl);
	
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

@RequestMapping(value="getParkCharge",method=RequestMethod.GET)
@ResponseBody
public String getParkCharge(@RequestParam("parkId") int parkId, @RequestParam("startDay")String startDay, @RequestParam("endDay")String endDay ){
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	Date parsedStartDay = null;
	try {
		parsedStartDay = sdf.parse(startDay);
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Date parsedEndDay  = null;
	try {
		parsedEndDay = sdf.parse(endDay);
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Map<String, Object> result = posdataService.getParkCharge(parkId, parsedStartDay, parsedEndDay);
	return Utility.createJsonMsg(1001, "success", result);
}

@RequestMapping(value="getCarportCharge",method=RequestMethod.GET)
@ResponseBody
public String getCarportCharge(@RequestParam("carport") int carportId, @RequestParam("startDay")String startDay, @RequestParam("endDay")String endDay ){
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	Date parsedStartDay = null;
	try {
		parsedStartDay = sdf.parse(startDay);
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Date parsedEndDay  = null;
	try {
		parsedEndDay = sdf.parse(endDay);
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Map<String, Object> result = posdataService.getCarportCharge(carportId, parsedStartDay, parsedEndDay);
	return Utility.createJsonMsg(1001, "success", result);
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
@RequestMapping(value="/selectPosdataByCarportAndRange",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String selectPosdataByCarportAndRange(@RequestBody Map<String,Object> args){
	String parkName=(String)args.get("parkName");
	String startDay=(String)args.get("startDay");
	String endDay=(String)args.get("endDay");
	Map<String, Object> retMap = new HashMap<String, Object>();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	Date parsedStartDay = null;
	try {
		parsedStartDay = sdf.parse(startDay + " 00:00:00");
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Date parsedEndDay  = null;
	try {
		parsedEndDay = sdf.parse(endDay + " 00:00:00");
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	List<Posdata> posdatas=posdataService.selectPosdataByCarportAndRange(parkName, parsedStartDay, parsedEndDay);
	retMap.put("status", 1001);
	retMap.put("message", "success");
	retMap.put("body", posdatas);
	return Utility.gson.toJson(retMap);
}
}

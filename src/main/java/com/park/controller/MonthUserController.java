package com.park.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.formula.functions.Index;
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
import com.park.model.Monthuser;
import com.park.model.Monthuserpark;
import com.park.model.Page;
import com.park.service.AuthorityService;
import com.park.service.MonthUserParkService;
import com.park.service.MonthUserService;
import com.park.service.UserPagePermissionService;
import com.park.service.Utility;

@Controller
@RequestMapping("monthUser")
public class MonthUserController {
@Autowired
private MonthUserService monthUserService;
@Autowired
private MonthUserParkService monthUserParkService;
@Autowired
private AuthorityService authService;

@Autowired
private UserPagePermissionService pageService;

@RequestMapping(value="")
public String index(ModelMap modelMap, HttpServletRequest request, HttpSession session){
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
	return "monthUser";
}

@RequestMapping(value="getParkNamesByUserId/{userId}",produces={"application/json;charset=utf-8"})
@ResponseBody
public String getParkNamesByUserId(@PathVariable("userId")int userId){
	Map<String, Object> result=new HashMap<>();
	List<Map<String, Object>> parkNames=monthUserParkService.getOwnParkName(userId);
	result.put("status", 1001);
	result.put("body", parkNames);
	return Utility.gson.toJson(result);
}

@RequestMapping(value="getUsersByParkId/{parkId}",produces={"application/json;charset=utf-8"})
@ResponseBody
public String getUsersByParkId(@PathVariable("parkId")int parkId){
	Map<String, Object> result=new HashMap<>();
	List<Map<String, Object>> users=monthUserParkService.getUsersByParkId(parkId);
	result.put("status", 1001);
	result.put("body", users);
	return Utility.gson.toJson(result);
}

@RequestMapping(value="deletePark/{id}",produces={"application/json;charset=utf-8"})
@ResponseBody
public String deletePark(@PathVariable("id")int id){
	Map<String, Object> result=new HashMap<>();
	int num=monthUserParkService.deleteByPrimaryKey(id);
	if (num==1) {
		result.put("status", 1001);
	}
	else {
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}

@RequestMapping(value="insertPark",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String insertPark(@RequestBody Monthuserpark monthUserPark){
	Map<String, Object> result=new HashMap<>();
	int num= monthUserParkService.insert(monthUserPark);
	if (num==1) {
		result.put("status", 1001);
	}
	else {
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="deletePark",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String deletePark(@RequestBody Monthuserpark monthUserPark){
	Map<String, Object> result=new HashMap<>();
	int num= monthUserParkService.deleteByUserIdAndParkId(monthUserPark);
	if (num==1) {
		result.put("status", 1001);
	}
	else {
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="insert",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String insert(@RequestBody Monthuser monthUser){
	Map<String, Object> result=new HashMap<>();
	int num=monthUserService.insert(monthUser);
	if (num==1) {
		result.put("status", 1001);
	}
	else {
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="delete/{id}",method=RequestMethod.GET,produces={"application/json;charset=utf-8"})
@ResponseBody
public String delete(@PathVariable("id")int id){
	Map<String, Object> result=new HashMap<>();
	int num=monthUserService.deleteByPrimaryKey(id);
	if (num==1) {
		result.put("status", 1001);
	}
	else {
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="update",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String update(@RequestBody Monthuser monthUser){
	Map<String, Object> result=new HashMap<>();
	int num=monthUserService.updateByPrimaryKeySelective(monthUser);
	if (num==1) {
		result.put("status", 1001);
	}
	else {
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="selectByPrimaryKey/{id}",method=RequestMethod.GET,produces={"application/json;charset=utf-8"})
@ResponseBody
public String selectByPrimaryKey(@PathVariable("id")int id){
	Map<String, Object> result=new HashMap<>();
	Monthuser monthUser=monthUserService.selectByPrimaryKey(id);
	if (monthUser!=null) {
		result.put("status", 1001);
		result.put("body", monthUser);
	}
	else{
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="getCount",method=RequestMethod.GET,produces={"application/json;charset=utf-8"})
@ResponseBody
public String getCount(){
	Map<String, Object> result=new HashMap<>();
	int count=monthUserService.getCount();
	result.put("count", count);
	return Utility.gson.toJson(result);
}
@RequestMapping(value="/getByStartAndCount",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String getByStartAndCount(@RequestParam("start")int start,@RequestParam("count")int count){
	Map<String, Object> result=new HashMap<>();
	List<Monthuser> monthusers=monthUserService.getByStartAndCount(start, count);
	if (monthusers!=null) {
		result.put("status", 1001);
		result.put("body", monthusers);
	}
	else {
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}
}
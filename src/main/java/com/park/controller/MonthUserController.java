package com.park.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import com.park.service.HardwareService;
import com.park.service.MonthUserParkService;
import com.park.service.MonthUserService;
import com.park.service.UserPagePermissionService;
import com.park.service.UserParkService;
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
HardwareService hardwareService;
@Autowired
private UserParkService userParkService;

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
@RequestMapping(value="order")
public String indexOrder(ModelMap modelMap, HttpServletRequest request, HttpSession session){
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
	return "monthUserOrder";
}
@RequestMapping(value="getParkNamesByUserId/{userId}",produces={"application/json;charset=utf-8"})
@ResponseBody
public String getParkNamesByUserId(@PathVariable("userId")int userId){
	Map<String, Object> result=new HashMap<>();
//	List<Monthuser> monthusers=monthUserService.get
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
	Monthuserpark monthuserpark=monthUserParkService.selectByPrimaryKey(id);
	monthUserParkService.deleteByPrimaryKey(id);
	int num=monthUserService.deleteByPrimaryKey(monthuserpark.getMonthuserid());
	if (num==1) {
		result.put("status", 1001);
	}
	else {
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="getAmountBarrier",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String getAmountBarrier(@RequestBody Map<String, Object> args){
	String mac=(String) args.get("mac");
	Map<String, Object> result=new HashMap<>();
	List<Map<String, Object>> infos = hardwareService.getInfoByMac(mac);
	Map<String, Object> info = infos.get(0);
	Integer parkId = (Integer) info.get("parkID");
	List<Monthuser> monthusers=monthUserService.getByParkIdAndCountOrder(parkId,0,1000,0);
	List<Monthuser> realMonthusers=new ArrayList<>();
	
	for (Monthuser monthuser : monthusers) {
		if (monthuser.getEndtime().getTime()>new Date().getTime()) {
			realMonthusers.add(monthuser);
		}
	}
	if (!realMonthusers.isEmpty()) {
		result.put("status", 1001);
		result.put("body", Math.ceil(realMonthusers.size()/50));
	}
	else {
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="getDataBarrier",method=RequestMethod.POST,produces={"application/text;charset=utf-8"})
@ResponseBody
public String getDataBarrier(@RequestBody Map<String, Object> args){
	String mac=(String) args.get("mac");
	int order=(int) args.get("order");
	Map<String, Object> result=new HashMap<>();
	List<Map<String, Object>> infos = hardwareService.getInfoByMac(mac);
	Map<String, Object> info = infos.get(0);
	Integer parkId = (Integer) info.get("parkID");
	List<Monthuser> monthusers=monthUserService.getByParkIdAndCountOrder(parkId,0,1000,0);
	List<Monthuser> realMonthusers=new ArrayList<>();
	List<String> data=new ArrayList<>();
	for (Monthuser monthuser : monthusers) {
		if (monthuser.getEndtime().getTime()>new Date().getTime()) {
			realMonthusers.add(monthuser);
		}
	}
	if (realMonthusers.isEmpty()) {
		result.put("status", 1002);
		return Utility.gson.toJson(result);
	}
	StringBuilder carnumber=new StringBuilder();
	for (int i = 50*order; i < 50*order+50; i++) {
		if (i<realMonthusers.size()&&realMonthusers.get(i).getPlatenumber()!=null&&realMonthusers.get(i).getPlatenumber().length()>5) {			
			data.add(realMonthusers.get(i).getPlatenumber().substring(4));			
		}		
	}
	for (int i = 0; i < data.size()-1; i++) {
		carnumber.append(data.get(i));
		carnumber.append("!");
	}
	carnumber.append(data.get(data.size()-1));
	
	result.put("status", 1001);
	result.put("body", carnumber.toString());
	return Utility.gson.toJson(result);
}
@RequestMapping(value="insertPark",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String insertPark(@RequestBody Monthuserpark monthUserPark){
	Map<String, Object> result=new HashMap<>();
	Monthuser monthuser=monthUserService.selectByPrimaryKey(monthUserPark.getMonthuserid());
	monthuser.setParkid(monthUserPark.getParkid());
	monthuser.setId(null);
	int num= monthUserService.insertSelective(monthuser);
	monthUserParkService.insert(monthUserPark);
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
	int num= monthUserService.deleteByPrimaryKey(monthUserPark.getMonthuserid());
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
	List<Monthuser> monthusers=monthUserService.getByCarnumberAndPark(monthUser.getCardnumber(), monthUser.getParkid());
	if (!monthusers.isEmpty()) {
		result.put("status", 1002);
		result.put("message", "用户已存在");
		return Utility.gson.toJson(result);
	}
	int num=monthUserService.insert(monthUser);
	if (num==1) {
		result.put("status", 1001);
		
	}
	else {
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="insertOrder",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String insertOrder(@RequestBody Monthuser monthUser){
	Map<String, Object> result=new HashMap<>();
	monthUser.setType(1);
//	Date startd=monthUser.getStarttime();
//	Date endd=monthUser.getEndtime();
//	if (endd.getTime()-startd.getTime()>1000*60*60*24) {
//		result.put("status", 1002);
//		result.put("message", "时间不能超过24小时");
//		return Utility.gson.toJson(result);
//	}
	int num=monthUserService.insert(monthUser);
	if (num==1) {
		result.put("status", 1001);
		result.put("message", "ok");
	}
	else {
		result.put("status", 1002);
		result.put("message", "failed");
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
		result.put("message", "ok");
	}
	else {
		result.put("status", 1002);
		result.put("message", "failed");
	}
	return Utility.gson.toJson(result);
}

@RequestMapping(value="deleteByNameAndParkOrder",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String deleteByNameAndParkOrder(@RequestBody Map<String, Object> args){
	Map<String, Object> result=new HashMap<>();
	String username = (String) args.get("username");
	int parkid = (int) args.get("parkid");
	List<Monthuser> monthusers=monthUserService.getByUsernameAndPark(username, parkid);
	if (monthusers.isEmpty()) {
		result.put("status", 1002);
		result.put("message", "没有预约");
		return Utility.gson.toJson(result);
	}
	int num=0;
	for (Monthuser monthuser : monthusers) {
		if (monthuser.getType()!=0) {
			 num=monthUserService.deleteByPrimaryKey(monthuser.getId());
		}
	}
	
	if (num==1) {
		result.put("status", 1001);
	}
	else {
		result.put("status", 1002);
		result.put("message", "删除失败");
	}
	return Utility.gson.toJson(result);
}

@RequestMapping(value="deleteByNameAndPark",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String deleteByNameAndPark(@RequestBody Map<String, Object> args){
	Map<String, Object> result=new HashMap<>();
	String username = (String) args.get("username");
	int parkid = (int) args.get("parkid");
	List<Monthuser> monthusers=monthUserService.getByUsernameAndPark(username, parkid);
	if (monthusers.isEmpty()) {
		result.put("status", 1002);
		result.put("message", "没有预约");
		return Utility.gson.toJson(result);
	}
	int num=0;
	for (Monthuser monthuser : monthusers) {
//		if (monthuser.getType()==0) {
			 num=monthUserService.deleteByPrimaryKey(monthuser.getId());
//		}
	}
	
	if (num==1) {
		result.put("status", 1001);
		result.put("message", "ok");
	}
	else {
		result.put("status", 1002);
		result.put("message", "删除失败");
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="deleteByCarnumberAndPark",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String deleteByCarnumberAndPark(@RequestBody Map<String, Object> args){
	Map<String, Object> result=new HashMap<>();
	String carnumber = (String) args.get("carnumber");
	int parkid = (int) args.get("parkid");
	List<Monthuser> monthusers=monthUserService.getByCarnumberAndPark(carnumber, parkid);
	if (monthusers.isEmpty()) {
		result.put("status", 1002);
		result.put("message", "没有预约");
		return Utility.gson.toJson(result);
	}
	int num=0;
	for (Monthuser monthuser : monthusers) {
//		if (monthuser.getType()==0) {
			 num=monthUserService.deleteByPrimaryKey(monthuser.getId());
//		}
	}
	
	if (num==1) {
		result.put("status", 1001);
		result.put("message", "ok");
	}
	else {
		result.put("status", 1002);
		result.put("message", "删除失败");
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
		result.put("message", "ok");
	}
	else {
		result.put("status", 1002);
		result.put("message", "failed");
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="updateOrder",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String updateOrder(@RequestBody Monthuser monthUser){
	Map<String, Object> result=new HashMap<>();
	monthUser.setType(1);
//	Date startd=monthUser.getStarttime();
//	Date endd=monthUser.getEndtime();
//	if (endd.getTime()-startd.getTime()>1000*60*60*24) {
//		result.put("status", 1002);
//		result.put("message", "时间不能超过24小时");
//		return Utility.gson.toJson(result);
//	}
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
@RequestMapping(value="getCountByParkId",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String getCountByParkId(@RequestBody Map<String, Object> args){
	Map<String, Object> result=new HashMap<>();
	int parkId=(int) args.get("parkId");
	int count=monthUserService.getCountByParkId(parkId);
	result.put("count", count);
	return Utility.gson.toJson(result);
}
@RequestMapping(value="getByParkIdAndCount",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String getByParkIdAndCount(@RequestBody Map<String, Object> args){
	Map<String, Object> result=new HashMap<>();
	int parkId=(int) args.get("parkId");
	int start=(int) args.get("start");
	int count=(int) args.get("count");
	List<Monthuser> monthusers=monthUserService.getByParkIdAndCount(parkId, start, count);
	if (monthusers!=null) {
		result.put("status", 1001);
		result.put("body", monthusers);
	}
	else {
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="getByParkIdAndCountOrder",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String getByParkIdAndCountOrder(@RequestBody Map<String, Object> args){
	Map<String, Object> result=new HashMap<>();
	int parkId=(int) args.get("parkId");
	int start=(int) args.get("start");
	int count=(int) args.get("count");
	List<Monthuser> monthusers=monthUserService.getByParkIdAndCountOrder(parkId, start, count,1);
	if (monthusers!=null) {
		result.put("status", 1001);
		result.put("body", monthusers);
	}
	else {
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="/getByStartAndCount",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String getByStartAndCount(@RequestParam("start")int start,@RequestParam("count")int count,HttpSession session){
	Map<String, Object> result=new HashMap<>();
	List<Monthuser> monthusers=monthUserService.getByStartAndCount(start, count);
	String username = (String) session.getAttribute("username");
	AuthUser user = authService.getUserByUsername(username);
	List<Integer> filterParkIds = userParkService.getOwnParkId(user.getId());
	Set<Integer> filterParkIdSet = new HashSet<Integer>(filterParkIds);
	List<Monthuser> fiList=new ArrayList<>();
	if(user.getRole() == AuthUserRole.ADMIN.getValue()){
	}
	else {
	
		for (Monthuser monthuser : monthusers) {
			if(filterParkIdSet.contains(monthuser.getParkid())){
				fiList.add(monthuser);
			}  
		}
		monthusers=fiList;
	}
	if (monthusers!=null) {
		result.put("status", 1001);
		result.put("body", monthusers);
	}
	else {
		result.put("status", 1002);
	}
	return Utility.gson.toJson(result);
}
@RequestMapping(value="/getByStartAndCountOrder",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String getByStartAndCountOrder(@RequestParam("start")int start,@RequestParam("count")int count){
	Map<String, Object> result=new HashMap<>();
	List<Monthuser> monthusers=monthUserService.getByStartAndCountOrder(start, count,1);
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

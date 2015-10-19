package com.park.controller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.Park;
import com.park.service.ParkService;
import com.park.service.Utility;

@Controller
public class ParkController {

	@Autowired
	private ParkService parkService;
	
	private static Log logger = LogFactory.getLog(ParkController.class);
	
	@RequestMapping(value = "/parks", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String parks(ModelMap modelMap, HttpServletRequest request){
		
		return "park";
	}
	
	@RequestMapping(value = "/getPark/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkById(@PathVariable int id, ModelMap modelMap, HttpServletRequest request){
		Park park = parkService.getParkById(id);
	
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("status", 1001);
		ret.put("body", park);
		ret.put("message", "get park success");
		return Utility.gson.toJson(ret);
	}
	
	@RequestMapping(value = "/getParks", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParks(ModelMap modelMap, HttpServletRequest request){
		List<Park> parkList = parkService.getParks();
	
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("status", 1001);
		ret.put("body", parkList);
		ret.put("message", "get park success");
		return Utility.gson.toJson(ret);
	}
	
	@RequestMapping(value = "/getNearParks", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getNearParks(@RequestBody Map<String, Object> args){
		
		double longitude = Double.parseDouble(args.get("longitude").toString());
		double latitude = Double.parseDouble(args.get("latitude").toString());
		double radius = Double.parseDouble(args.get("radius").toString());
		List<Park> parkList = parkService.getNearParks(longitude, latitude, radius);
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("status", 1001);
		ret.put("body", parkList);
		ret.put("message", "get park success");
		return Utility.gson.toJson(ret);
	}
	
	@RequestMapping(value = "/getParkWithName/{name}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkWithName(@PathVariable String name){
		
		logger.info("get park with name: " + name);
		List<Park> parks = parkService.getParkByName(name);
	
		if(parks != null){
			if( parks.size() != 0){
				return Utility.createJsonMsg(1001, "get park success", parks);
			}else{
				logger.debug("park not exist : " + name);
				return Utility.createJsonMsg(1002, "park not exist");
				
			}
				
		}
		return Utility.createJsonMsg(1002, "get park fail");
	}
	
	@RequestMapping(value = "/getParkByName", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkByName(@RequestBody Map<String, Object> args){
		String name = (String)args.get("name");
		logger.info("get park with name: " + name);
		List<Park> parks = parkService.getParkByName(name);
	
		if(parks != null){
			if( parks.size() != 0)
				return Utility.createJsonMsg(1001, "get park success", parks);
			else{
				logger.debug("park not exist : " + name);
				return Utility.createJsonMsg(1002, "park not exist");
			}
				
				
		}
		return Utility.createJsonMsg(1002, "get park fail");
	}
	
	@RequestMapping(value = "/getParkCount", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkCount(){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> body = new HashMap<String, Object>();
		int count = parkService.getParkCount();
		body.put("count", count);
	
		ret.put("status", "1001");
		ret.put("message", "get park detail success");
		ret.put("body", Utility.gson.toJson(body));
		
		return Utility.gson.toJson(ret);					
	}
	
	@RequestMapping(value = "/getParkDetail", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public  String  accessIndex( @RequestBody Map<String, Object> args){
		int low = (int)args.get("low");
		int count = (int)args.get("count");
		Map<String, Object> ret = new HashMap<String, Object>();
		List<Park> parkDetail = parkService.getParkDetail(low, count);
		if(parkDetail != null){
			ret.put("status", "1001");
			ret.put("message", "get park detail success");
			ret.put("body", Utility.gson.toJson(parkDetail));
		}else{
			ret.put("status", "1002");
			ret.put("message", "get park detail fail");
		}
		return Utility.gson.toJson(ret);
		
	}
	@RequestMapping(value = "/search/parkBykeywords", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkByLocationkeywords(@RequestBody Map<String, Object> args){
		String str=(String) args.get("keywords");
		List<Park> parkDetail=parkService.getParkDetailByKeywords(str);
		Map<String, Object> ret = new HashMap<String, Object>();
		if(parkDetail != null){
			ret.put("status", "1001");
			ret.put("message", "get park detail success");
			ret.put("body", Utility.gson.toJson(parkDetail));
		}else{
			ret.put("status", "1002");
			ret.put("message", "get park detail fail");
		}
		return Utility.gson.toJson(ret);
	}
	
	@RequestMapping(value = "/insert/park", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertPark(@RequestBody Park park){
		return parkService.insertPark(park);
	}
	
	@RequestMapping(value = "/update/park", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updatePark(@RequestBody Park park){
		
		return parkService.updatePark(park);
	}
	
	@RequestMapping(value = "/update/parkFields", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updatePark1(@RequestBody Map<String, Object> args){
		if(!args.containsKey("id"))
			return Utility.createJsonMsg("1002", "need park id");
		int parkId = (int) args.get("id");
		Park park = parkService.getParkById(parkId);
		if(park == null){
			return Utility.createJsonMsg("1003", "no this park, cannot update");
		}
		
		Method[] methods = null;
		try {
			methods = Class.forName("com.park.model.Park").getMethods();
		} catch (SecurityException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		for(int i = 0; i < methods.length; i++){
			String methodName = methods[i].getName();
			if(!methodName.substring(0, 3).equals("set"))
				continue;
			String fieldInMethod = methodName.substring(3).toLowerCase();
			if(args.containsKey(fieldInMethod)){
				try {
					methods[i].invoke(park, args.get(fieldInMethod));
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		return parkService.updatePark(park);
	}
	
	
	@RequestMapping(value = "/delete/park/{Id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String deletePark(@PathVariable int Id){
		return parkService.deletePark(Id);
	}
}

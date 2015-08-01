package com.park.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.park.model.Park;
import com.park.service.ParkService;
import com.park.service.Utility;

@Controller
public class ParkController {

	@Autowired
	private ParkService parkService;
	
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
	
	@RequestMapping(value = "/getParkByName", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkByName(@RequestParam("name") String name){
		
		List<Park> parks = parkService.getParkByName(name);
	
		if(parks != null){
			if( parks.size() != 0)
				return Utility.createJsonMsg(1001, "get park success", parks.get(0));
			else
				return Utility.createJsonMsg(1002, "park not exist");
				
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
	
	@RequestMapping(value = "/getParkDetail", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String accessIndex(@RequestParam("low")int low, @RequestParam("count")int count){	
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
	
	@RequestMapping(value = "/update/park1", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updatePark1(@RequestBody Park park){
		return parkService.updatePark(park);
	}
	@RequestMapping(value = "/delete/park/{Id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String deletePark(@PathVariable int Id){
		return parkService.deletePark(Id);
	}
}

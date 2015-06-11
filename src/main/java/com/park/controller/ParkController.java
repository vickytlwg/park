package com.park.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.Park;
import com.park.service.ParkService;

@Controller
public class ParkController {

	@Autowired
	private ParkService parkService;
	
	@RequestMapping(value = "/insert/park", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertPark(@RequestBody Park park){
		return parkService.insertPark(park);
	}
	
	@RequestMapping(value = "/parks", produces = {"application/json;charset=UTF-8"})
	public String getParks(ModelMap modelMap, HttpServletRequest request){
		List<Park> parkList = parkService.getParks();
		modelMap.put("parks", parkList);					
		return "parkList";
	}
	
	@RequestMapping(value = "/update/park", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updatePark(@RequestBody Park park){
		return parkService.updatePark(park);
	}
	@RequestMapping(value = "/delete/park", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String deletePark(@RequestBody Map<String, Object> parkMap){
		return parkService.deletePark((int)parkMap.get("Id"));
	}
}

package com.park.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.park.model.Park;
import com.park.service.ParkService;


@Controller
public class ParkChartController {
	
	@Autowired
	private ParkService parkService;
	
	@RequestMapping(value = "/chart", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String parkChart(ModelMap modelMap, HttpServletRequest request){
		
		List<Park> parkList = parkService.getParks();
		modelMap.addAttribute("parks", parkList);
		return "parkChart";
	}
	@RequestMapping(value="/flow",method = RequestMethod.GET,produces={"application/json;charset=UTF-8"})
	public String parkFlow(ModelMap modelMap, HttpServletRequest request){
		List<Park> parkList = parkService.getParks();
		modelMap.addAttribute("parks", parkList);
		return "flow";
	}
}

package com.park.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.Carport;
import com.park.model.CarportStatusDetail;
import com.park.service.CarportService;
import com.park.service.Utility;

@Controller
public class CarportController {

	@Autowired
	private CarportService carportService;
	
	private static Log logger = LogFactory.getLog(UserController.class);
	
	
	@RequestMapping(value = "/carport/{Id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String carportDetail(@PathVariable int Id, ModelMap modelMap, HttpServletRequest request){
		Carport carport = carportService.getCarportById(Id);
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(carport != null){
			retMap.put("status", "1001");
			retMap.put("message", "get carport success");
			logger.info("get carport success: " + carport.toString());
		}else{
			retMap.put("status", "1002");
			retMap.put("message", "get carport fail");
			logger.info("get carport fail: null" );
		}
		retMap.put("body", carport);
		return Utility.gson.toJson(retMap);
	
		
	}
	
	@RequestMapping(value = "/insert/carport", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertCarport(@RequestBody Carport carport){
		
		
		int ret = carportService.insertCarport(carport);
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		if(ret > 0){
			retMap.put("status", "1001");
			retMap.put("message", "inset success");
			logger.info("insert carport success: " + carport.toString());
		}else{
			retMap.put("status", "1002");
			retMap.put("message", "inset fail");
			logger.info("insert carport fail: " + carport.toString());
		}
		return Utility.gson.toJson(retMap);
		
	}
	
	@RequestMapping(value = "/carports", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String carportsList(HttpServletRequest request){
		List<Carport> carports = carportService.getCarports();
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(carports != null){
			retMap.put("status", "1001");
			retMap.put("message", "get carport success");
			logger.info("get carports success: " + carports.toString());
		}else{
			retMap.put("status", "1002");
			retMap.put("message", "get carport fail");
			logger.info("get carports fail ");
		}
		retMap.put("body", carports);
		return Utility.gson.toJson(retMap);	
	}
	
	@RequestMapping(value="/carportstatus")
	public String carportstatus(){
		return "carportStatusShow";
	}
	
	@RequestMapping(value = "/specify/carports", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getSpecifyCarports(@RequestBody Map<String, Object> args, HttpServletRequest request){
		
		int start = (int)args.get("start");
		int counts = (int)args.get("counts");
		
		String orderInfo = (String)(args.get("sort"));
		String[] orderArray = orderInfo.split(" ");
		
		List<Carport> carports = null;
		
		String queryCondition = " ";
		
		boolean cityCondition = args.containsKey("city");
		if(cityCondition){
			queryCondition += "city=\"";
			queryCondition += (String) args.get("city");
			queryCondition += "\" ";
		}
		
		boolean filterCondition = args.containsKey("filter");
		
		if(filterCondition){	
			String filter = (String)args.get("filter");
			if(cityCondition)
				queryCondition += " and ";		
			if(filter.equals("out")){
				queryCondition += "isout = 1";
			}else if(filter.equals("in")){
				queryCondition += "isout= 0";
			}else if(filter.equals("mine")){
				queryCondition += "uid=";
				queryCondition += (int)args.get("uid");
			}
		}
		
		if(filterCondition || cityCondition){
			carports = carportService.getConditionCarports(start, counts, orderArray[0], orderArray[1], queryCondition);
		}else{
			carports = carportService.getSpecifyCarports(start, counts, orderArray[0], orderArray[1]);
		}
			
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(carports != null){
			retMap.put("status", "1001");
			retMap.put("message", "get carport success");
			logger.info("get carports success: " + carports.toString());
		}else{
			retMap.put("status", "1002");
			retMap.put("message", "get carport fail");
			logger.info("get carports fail ");
		}
		retMap.put("body", carports);
		return Utility.gson.toJson(retMap);	
	}
}

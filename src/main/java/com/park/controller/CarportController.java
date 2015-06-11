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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.park.model.Carport;
import com.park.service.CarportService;

@Controller
public class CarportController {

	@Autowired
	private CarportService carportService;
	
	private static Log logger = LogFactory.getLog(UserController.class);
	
	@RequestMapping(value = "/carport", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String carportDetail(@RequestParam("id") int id, ModelMap modelMap, HttpServletRequest request){
		Carport carport = carportService.getCarportById(id);
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(carport != null){
			retMap.put("status", "1001");
			retMap.put("message", "get carport success");
			logger.info("get carport success: " + carport.toString());
		}else{
			retMap.put("status", "1002");
			retMap.put("message", "get carport fail");
			logger.info("get carport fail: " + carport.toString());
		}
		retMap.put("body", carport);
		return new Gson().toJson(retMap);
	
		
	}
	
	@RequestMapping(value = "/insert/carports", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertCarports(@RequestBody Carport carport){

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
		return new Gson().toJson(retMap);
		
	}
	
	@RequestMapping(value = "/carports", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
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
			logger.info("get carports fail: " + carports.toString());
		}
		retMap.put("body", carports);
		return new Gson().toJson(retMap);	
	}

	
	@RequestMapping(value = "/specify/carports", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public String getSpecifyCarports(@RequestBody Map<String, Object> args, HttpServletRequest request){
		
		int low = (int)args.get("low");
		int high = (int)args.get("high");
		String field = (String)(args.get("field") == null ? "Id" : args.get("field"));
		String order = (String)(args.get("order") == null ? "" : args.get("order"));
		
		List<Carport> carports = carportService.getSpecifyCarports(low, high, field, order);
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(carports != null){
			retMap.put("status", "1001");
			retMap.put("message", "get carport success");
			logger.info("get carports success: " + carports.toString());
		}else{
			retMap.put("status", "1002");
			retMap.put("message", "get carport fail");
			logger.info("get carports fail: " + carports.toString());
		}
		retMap.put("body", carports);
		return new Gson().toJson(retMap);	
	}
}

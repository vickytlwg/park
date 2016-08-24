package com.park.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.service.ChargeService;
import com.park.service.UserPagePermissionService;
import com.pingplusplus.model.Charge;

@Controller
public class ChargeController {
	
	@Autowired
	private ChargeService chargeService;
	
	@Autowired
	private UserPagePermissionService pageService;
	
	private static Log logger = LogFactory.getLog(ChargeController.class);
	
	@RequestMapping(value = "/getCharge", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getCharge(@RequestBody Map<String, Object> argMap, HttpServletRequest request){
		String client_ip = "";
		if (request.getHeader("x-forwarded-for") == null) {
			client_ip =  request.getRemoteAddr();
		}else{
			client_ip = request.getHeader("x-forwarded-for");
		}
			  
		argMap.put("client_ip", client_ip);
		return chargeService.getCharge(argMap);
		
	}
	
	@RequestMapping(value = "/chargeResponse", method = RequestMethod.POST)
	@ResponseBody
	public String chargeResponse(@RequestBody Charge charge){
		
		logger.info("reponse charge: success" );
		return "success";
		/*if(charge.getPaid())
			return "success";
		else{
			//异常处理
			return "fail";
		}*/
			
		
	}

}

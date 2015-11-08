package com.park.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.park.model.BusinessCarport;
import com.park.model.BusinessCarportDetail;
import com.park.model.Status;
import com.park.service.AuthorityService;
import com.park.service.BusinessCarportService;
import com.park.service.HardwareService;
import com.park.service.Utility;

@Controller
public class BusinessCarportController {
	
	@Autowired
	private BusinessCarportService businessCarportService;
	
	@Autowired
	private AuthorityService authService;
	
	@Autowired
	private HardwareService hardwareService;
	
	private static Log logger = LogFactory.getLog(BusinessCarportController.class);
	
	@RequestMapping(value = "/businessCarport", produces = {"application/json;charset=UTF-8"})
	public String getBusinessCarports(ModelMap modelMap, HttpServletRequest request, HttpSession session){
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		if(user != null){
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if(user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin=true;
			modelMap.addAttribute("isAdmin", isAdmin);
		}
		
		return "businessCarport";
	}
	
	@RequestMapping(value = "/getBusinessCarportCount", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getBusinessCarportCount(@RequestParam(value = "parkId", required = false) Integer parkId){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> body = new HashMap<String, Object>();
		int count = businessCarportService.getBusinessCarportCount(parkId);
		body.put("count", count);
	
		ret.put("status", "1001");
		ret.put("message", "get businessCarport detail success");
		ret.put("body", body);
		
		return Utility.gson.toJson(ret);					
	}
	
	@RequestMapping(value = "/getBusinessCarportDetail", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String accessIndex(@RequestParam("low")int low, 
			@RequestParam("count")int count,
			@RequestParam(value = "parkId", required = false) Integer parkId){	
		
		Map<String, Object> ret = new HashMap<String, Object>();
		List<BusinessCarportDetail> businessCarportDetail = businessCarportService.getBusinessCarportDetail(low, count, parkId);
		if(businessCarportDetail != null){
			ret.put("status", "1001");
			ret.put("message", "get businessCarport detail success");
			ret.put("body", businessCarportDetail);
		}else{
			ret.put("status", "1002");
			ret.put("message", "get businessCarport detail fail");
		}
		return Utility.gson.toJson(ret);
		
	}
	
	@RequestMapping(value = "/insert/businessCarport", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertBusinessCarport(@RequestBody BusinessCarport businessCarport){
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		int carportRet = 0;
		int macId = businessCarport.getMacId();
		carportRet =  businessCarportService.insertBusinessCarport(businessCarport);
 		if(carportRet > 0 && hardwareService.bindHardware(macId)){
			retMap.put("status", "1001");
			retMap.put("message", "insert businessCarport success");
			return Utility.gson.toJson(retMap);
			
		}
		retMap.put("status", "1002");
		retMap.put("message", "insert businessCarport fail, mac has already been used");
		return Utility.gson.toJson(retMap);
	}
	
	@RequestMapping(value = "/update/businessCarport", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updateBusinessCarport(@RequestBody BusinessCarport businessCarport){
		Map<String, Object> retMap = new HashMap<String, Object>();
		BusinessCarport oldBusinessCarport = businessCarportService.getBusinessCarportById(businessCarport.getId());
		int ret = businessCarportService.updateBusinessCarport(businessCarport);
		if(ret > 0 ){
			if(oldBusinessCarport.getMacId() != businessCarport.getMacId()){
				hardwareService.changeHardwareStatus(oldBusinessCarport.getMacId(), Status.UNUSED.getValue());
				hardwareService.changeHardwareStatus(businessCarport.getMacId(), Status.USED.getValue());
			}
			retMap.put("status", "1001");
			retMap.put("message", "update businessCarport success");
		}else{
			retMap.put("status", "1002");
			retMap.put("message", "update businessCarport fail, change mac status or update carport fail");
		}
		
		return Utility.gson.toJson(retMap);
	}
	
	@RequestMapping(value = "/update/status/businessCarport", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updateBusinessCarportStatus(@RequestBody Map<String, Object> args){
		Map<String, Object> retMap = new HashMap<String, Object>();
		String mac = (String)args.get("mac");
		int status = Integer.parseInt((String)args.get("status"));
		int ret = businessCarportService.updateBusinessCarportStatus(mac, status);
		logger.info("updateBusinessCarportStatus resutl: " + ret);
		if(ret > 0 ){
			retMap.put("status", "1001");
			retMap.put("message", "update businessCarport success");
		}else{
			retMap.put("status", "1002");
			retMap.put("message", "update businessCarport fail, mac may not be used");
		}
		
		return Utility.gson.toJson(retMap) + "eof\n";
	}
	
	@RequestMapping(value = "/delete/businessCarport/{Id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String deleteBusinessCarport(@PathVariable int Id){
		BusinessCarport carport = businessCarportService.getBusinessCarportById(Id);
		hardwareService.changeHardwareStatus(carport.getMacId(), Status.UNUSED.getValue());
		int ret = businessCarportService.deleteBusinessCarport(Id);
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(ret > 0 ){
			retMap.put("status", "1001");
			retMap.put("message", "delete businessCarport success");
		}else{
			retMap.put("status", "1002");
			retMap.put("message", "delete businessCarport fail");
		}
		return Utility.gson.toJson(retMap);
	}
}

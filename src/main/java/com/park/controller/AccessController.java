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

import com.park.model.Access;
import com.park.service.AccessService;

@Controller
public class AccessController {

	@Autowired
	private AccessService accessService;
	
	@RequestMapping(value = "/insert/access", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertPark(@RequestBody Access access){
		return accessService.insertAccess(access);
	}
	
	@RequestMapping(value = "/access", produces = {"application/json;charset=UTF-8"})
	public String getAccesses(ModelMap modelMap, HttpServletRequest request){
		List<Access> accessList = accessService.getAccesses();
		modelMap.put("accesses", accessList);					
		return "accessList";
	}
	
	@RequestMapping(value = "/update/access", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updatePark(@RequestBody Access access){
		return accessService.updateAccess(access);
	}
	
	@RequestMapping(value = "/delete/access", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String deletePark(@RequestBody  Map<String, Object> accessMap){
		return accessService.deleteAccess((int)accessMap.get("Id"));
	}
}

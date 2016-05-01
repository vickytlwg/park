package com.park.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.Posdata;
import com.park.service.PosdataService;
import com.park.service.Utility;

@Controller
@RequestMapping("/pos")
public class PosdataController {
	
@Autowired
private PosdataService posdataService;

@RequestMapping(value = "/insertChargeDetail", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
@ResponseBody
public String insertPosdata(@RequestBody Posdata posdata ){
	int num=posdataService.insert(posdata);
	Map<String, Object> retMap = new HashMap<String, Object>();
	if(num==1)
	{
		retMap.put("status", 1001);
		retMap.put("message", "success");
	}
	else
	{
		retMap.put("status", 1002);
		retMap.put("message", "failure");
	}
	return Utility.gson.toJson(retMap);
}
}

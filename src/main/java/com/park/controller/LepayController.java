package com.park.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.service.LepayService;
import com.park.service.Utility;

@Controller
@RequestMapping("lepay")
public class LepayController {

	@RequestMapping(value="/pay",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String pay(@RequestBody Map<String, Object> args){
		Integer amount=(Integer)args.get("amount");
		int type=(int) args.get("type");
		Map<String, Object> result=new HashMap<>();
		LepayService lepayService=new LepayService();
		
		Map<String, Object> data=lepayService.saomaPay((long)amount, type);
		if (data!=null) {
			result.put("status", 1001);
			result.put("body", data);
		}
		else {
			result.put("status", 1002);
		}
		
		return Utility.gson.toJson(result);
	} 
}

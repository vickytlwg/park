package com.park.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.service.Utility;

@Controller
@RequestMapping("zjj")
public class ZhongjianjianController {

	private static Log logger = LogFactory.getLog(ZhongjianjianController.class);
	@RequestMapping(value = "/carIn", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8" })
	@ResponseBody
	public String carIn(@RequestBody Map<String, Object> args) {
		logger.info(args.toString());
		Map<String, Object> result=new HashMap<>();
		result.put("errorcode", 200);
		result.put("message", "成功");
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value = "/carOut", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8" })
	@ResponseBody
	public String carOut(@RequestBody Map<String, Object> args) {
		logger.info(args.toString());
		Map<String, Object> result=new HashMap<>();
		result.put("errorcode", 200);
		result.put("message", "成功");
		return Utility.gson.toJson(result);
	}
}

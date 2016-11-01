package com.park.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.park.service.Utility;

@Controller
@RequestMapping("fee")
public class FeeDataController {
private static Log logger = LogFactory.getLog(FeeDataController.class);
@RequestMapping(value="/alipaydata/insert",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
public String alipaydataInsert(@RequestBody List<Map<String, Object>> args){
	Map<String, Object> result=new HashMap<>();
	logger.info(args);
	return Utility.gson.toJson(result);
}
@RequestMapping(value="/wechartdata/insert",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
public String wechartdataInsert(@RequestBody List<Map<String, Object>> args){
	Map<String, Object> result=new HashMap<>();
	logger.info(args);
	return Utility.gson.toJson(result);
}
}

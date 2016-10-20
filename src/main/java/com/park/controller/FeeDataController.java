package com.park.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.park.service.Utility;

@Controller
@RequestMapping("fee")
public class FeeDataController {
@RequestMapping(value="/alipaydata/insert",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
public String alipaydataInsert(@RequestBody List<Map<String, Object>> args){
	Map<String, Object> result=new HashMap<>();
	return Utility.gson.toJson(result);
}
@RequestMapping(value="/wechartdata/insert",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
public String wechartdataInsert(@RequestBody List<Map<String, Object>> args){
	Map<String, Object> result=new HashMap<>();
	return Utility.gson.toJson(result);
}
}

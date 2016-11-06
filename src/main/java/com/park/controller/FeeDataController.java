package com.park.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
public String alipaydataInsert(HttpServletRequest request){
	Map<String, Object> result=new HashMap<>();
	
	logger.info("支付宝数据接收");
	String amount=request.getParameter("amount");
	String payTypeTradeNo=(String) request.getParameter("payTypeTradeNo");
	logger.info("支付金额:"+amount);
	logger.info("支付渠道订单号:"+payTypeTradeNo);
	result.put("status", "success");
	return Utility.gson.toJson(result);
}
@RequestMapping(value="/wechartdata/insert",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
public String wechartdataInsert(HttpServletRequest request){
	Map<String, Object> result=new HashMap<>();
	logger.info("微信数据接收");
	String amount=request.getParameter("amount");
	String payTypeTradeNo=(String) request.getParameter("payTypeTradeNo");
	logger.info("支付金额:"+amount);
	logger.info("支付渠道订单号:"+payTypeTradeNo);
	result.put("status", "success");
	return Utility.gson.toJson(result);
}
}

package com.park.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.park.model.Lepayrecord;
import com.park.service.LepayRecordService;
import com.park.service.Utility;

@Controller
@RequestMapping("fee")
public class FeeDataController {
private static Log logger = LogFactory.getLog(FeeDataController.class);
@Autowired
LepayRecordService lepayRecord;
@RequestMapping(value="/alipaydata/insert",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
public String alipaydataInsert(HttpServletRequest request){
	Map<String, Object> result=new HashMap<>();

	Lepayrecord lepayrecord=new Lepayrecord();
	lepayrecord.setPaytype((short) 0);//支付宝为0
	String signature=request.getParameter("signature");
	String amount=request.getParameter("amount");
	lepayrecord.setAmount(Integer.parseInt(amount));;
	String mchId=request.getParameter("mchId");
	lepayrecord.setMchid(mchId);
	String outTradeNo=request.getParameter("outTradeNo");
	lepayrecord.setOuttradeno(outTradeNo);
	String payTypeTradeNo=(String) request.getParameter("payTypeTradeNo");
	lepayrecord.setPaytypetradeno(payTypeTradeNo);
	String orderNo=request.getParameter("orderNo");
	lepayrecord.setOrderno(orderNo);
	lepayRecord.insertSelective(lepayrecord);
	result.put("status", "success");
	return Utility.gson.toJson(result);
}
@RequestMapping(value="/wechartdata/insert",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
public String wechartdataInsert(HttpServletRequest request){
	Map<String, Object> result=new HashMap<>();
	Lepayrecord lepayrecord=new Lepayrecord();
	lepayrecord.setPaytype((short) 1);//微信为1
	String signature=request.getParameter("signature");
	String amount=request.getParameter("amount");
	lepayrecord.setAmount(Integer.parseInt(amount));;
	String mchId=request.getParameter("mchId");
	lepayrecord.setMchid(mchId);
	String outTradeNo=request.getParameter("outTradeNo");
	lepayrecord.setOuttradeno(outTradeNo);
	String payTypeTradeNo=(String) request.getParameter("payTypeTradeNo");
	lepayrecord.setPaytypetradeno(payTypeTradeNo);
	String orderNo=request.getParameter("orderNo");
	lepayrecord.setOrderno(orderNo);
	result.put("status", "success");
	return Utility.gson.toJson(result);
}
}

package com.park.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.service.PingPPService;

@Controller
@RequestMapping("pingpp")
public class PingPPController {
	@Autowired
	PingPPService pingppService;
	@RequestMapping(value="/pay",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String pay(@RequestBody Map<String, Object> args){
		args.put("channel", "alipay_qr");
		args.put("client_ip", "127.0.0.1");		
		return pingppService.saomaPay(args);
	}
	@RequestMapping(value="/payByPosChargeDataId",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String payByPosChargeDataId(@RequestBody Map<String, Object> args){
		args.put("channel", "alipay_qr");
		args.put("client_ip", "127.0.0.1");		
		return pingppService.saomaPay(args);
	}
	
	@RequestMapping(value="/query",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String query(@RequestBody Map<String, Object> args){
		return pingppService.query(args);
	}
}

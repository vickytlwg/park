package com.park.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.Park;
import com.park.model.PosChargeData;
import com.park.service.ParkService;
import com.park.service.PosChargeDataService;
import com.park.service.Utility;

@Controller
@RequestMapping("/pos/charge")
public class PosChargeDataController {
	
	@Autowired
	ParkService parkService;
	
	@Autowired
	PosChargeDataService chargeSerivce;
	
	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String get(){
		
		List<PosChargeData> charges = chargeSerivce.get();
		return Utility.createJsonMsg(1001, "success", charges);
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String insert(@RequestBody PosChargeData charge){
		
		int parkId = charge.getParkId();
		Park park = parkService.getParkById(parkId);
		
		if(park == null || park.getFeeCriterionId() == null){
			return Utility.createJsonMsg(1002, "请先绑定计费标准到停车场");
		}
		
		charge.setEntranceDate(new Date());
		int ret = chargeSerivce.insert(charge);
		if(ret == 1)
			return Utility.createJsonMsg(1001, "success");
		else
			return Utility.createJsonMsg(1002, "failed");
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String modify(@RequestBody PosChargeData charge){
		
		int ret = chargeSerivce.update(charge);
		if(ret == 1)
			return Utility.createJsonMsg(1001, "success");
		else
			return Utility.createJsonMsg(1002, "failed");
	
	}
	
	
	@RequestMapping(value = "/unpaid/{cardNumber}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String getDebt(@PathVariable String cardNumber){
		List<PosChargeData> unpaidCharges;
		try {
			unpaidCharges = chargeSerivce.getDebt(cardNumber);
		} catch (Exception e) {
			return Utility.createJsonMsg(1002, "请先绑定停车场计费标准" );
		}
		
		return Utility.createJsonMsg(1001, "success", unpaidCharges);
	}
	
	@RequestMapping(value = "/pay/{cardNumber}/{money}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String getDebt(@PathVariable String cardNumber, @PathVariable double money){
		List<PosChargeData> payRet =  null;
		try {
			payRet = chargeSerivce.pay(cardNumber, money);
		} catch (Exception e) {
			return Utility.createJsonMsg(1002, "请先绑定停车场计费标准" );
		}
		
		return Utility.createJsonMsg(1001, "success", payRet);
	}
	

}

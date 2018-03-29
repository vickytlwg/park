package com.park.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.PosChargeData;
import com.park.service.PosChargeDataService;
import com.park.service.Utility;

@Controller
@RequestMapping("SmartParkComm")
public class GongHangController {
	@Autowired
	PosChargeDataService posChargedataService;
	
	@RequestMapping(value = "queryOrderTemp", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String queryOrderTemp(@RequestBody Map<String, String> args){
		String plateNo = args.get("plateNo");
		String plateColor = args.get("plateColor");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", "0");
		ret.put("msg", "正确返回");
		
		if (plateNo==null) {
			ret.put("code", "1");
			ret.put("msg", "错误");
			return Utility.gson.toJson(ret);
		}
		List<PosChargeData> queryCharges=new ArrayList<>();
		try {
			Date exitdate=new Date();
			 queryCharges=posChargedataService.queryDebt(plateNo, exitdate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret.put("code", "1");
			ret.put("msg", "错误");
			return Utility.gson.toJson(ret);
		}
		if (queryCharges.isEmpty()) {
			ret.put("code", "1");
			ret.put("msg", "无停车记录");
			return Utility.gson.toJson(ret);
		}
		PosChargeData posChargeData=queryCharges.get(0);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orderId", String.valueOf(posChargeData.getId()));
		data.put("plateNo", plateNo);
		data.put("plateColor", plateColor);
		data.put("startTime", new SimpleDateFormat("yyyyMMddHHmmss").format(posChargeData.getEntranceDate()));
		double dd=(new Date().getTime()-posChargeData.getEntranceDate().getTime())/1000;
		data.put("serviceTime", String.valueOf((int)dd));
		data.put("serviceFee", (int)(posChargeData.getChargeMoney()*100));
		ret.put("data", data);
		return Utility.gson.toJson(ret);
	}
	
	@RequestMapping(value = "payOrderTemp", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String payOrderTemp(@RequestBody Map<String, String> args){
		String orderId = args.get("orderId");
		String money = args.get("money");
		String plateNo = args.get("plateNo");
		String payTime = args.get("payTime");
		String saleType = args.get("saleType");
		String saleTime = args.get("saleTime");
		String salAmount = args.get("salAmount");
		int poschargeId=Integer.parseInt(orderId);
		PosChargeData Chargedata =posChargedataService.getById(poschargeId);
		Chargedata.setChargeMoney(Double.parseDouble(money)/100);
		Chargedata.setPaidCompleted(true);
		Chargedata.setOperatorId("工行");
		Chargedata.setPayType(3);
		Chargedata.setExitDate1(new Date());
		Chargedata.setRejectReason(payTime);
	
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", "0");
		ret.put("msg", "正确返回");
		if (posChargedataService.update(Chargedata)==0) {
			ret.put("code", "1");
			ret.put("msg", "错误");
		}
		return Utility.gson.toJson(ret);
	}
}

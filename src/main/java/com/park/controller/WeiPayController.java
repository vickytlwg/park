package com.park.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.park.model.Alipayrecord;
import com.park.model.Constants;
import com.park.model.PosChargeData;
import com.park.service.AliParkFeeService;
import com.park.service.AlipayrecordService;
import com.park.service.HongxingService;
import com.park.service.ParkToAliparkService;
import com.park.service.PosChargeDataService;
import com.park.service.Utility;

@Controller
@RequestMapping("weipay")
public class WeiPayController {
	
	@Autowired
	HongxingService hongxingService;
	@Autowired
	PosChargeDataService poschargedataService;
	@Autowired
	ParkToAliparkService parkToAliparkService;
	@Autowired
	AliParkFeeService parkFeeService;
	@Autowired
	AlipayrecordService alipayrecordService;
	
	@RequestMapping(value = "notifyPay", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String notifyUrlWebPay(@RequestBody Map<String, Object> args){
		int poschargeId=(int) args.get("poschargeId");
		String paidAmount=(String) args.get("paidAmount");	
		String tradeNo=(String) args.get("tradeNo");	
		//更新poschargeData的状态
		PosChargeData lastCharge =poschargedataService.getById(poschargeId);
		lastCharge.setChargeMoney(Double.parseDouble(paidAmount));
		lastCharge.setPaidCompleted(true);
		lastCharge.setPayType(1);
	//	lastCharge.setExitDate1(new Date());
		lastCharge.setRejectReason("weipay"+new SimpleDateFormat(Constants.DATEFORMAT).format(new Date()));
		poschargedataService.update(lastCharge);
		//支付宝记录的添加  微信暂时添加进来
		Alipayrecord alipayrecord=new Alipayrecord();
		alipayrecord.setStatus("5");
		alipayrecord.setMoney(Double.parseDouble(paidAmount));
		alipayrecord.setAlitradeno(tradeNo);
		alipayrecord.setPoschargeid(poschargeId);
		alipayrecord.setDate(new Date());
		int num=alipayrecordService.insertSelective(alipayrecord);
		Map<String, Object> ret = new HashMap<String, Object>();
		if (num==1) {
			ret.put("status", 1001);
			ret.put("message", "success");
		}
		else {
			ret.put("status", 1002);
			ret.put("message", "failed");
		}
		return Utility.gson.toJson(ret);
	}
	
	@RequestMapping(value = "notifyPayWithExitTime", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String updateExitTime(@RequestBody Map<String, Object> args){
		int poschargeId=(int) args.get("poschargeId");
		String paidAmount=(String) args.get("paidAmount");	
		String tradeNo=(String) args.get("tradeNo");	
		//更新poschargeData的状态
		PosChargeData lastCharge =poschargedataService.getById(poschargeId);
		lastCharge.setChargeMoney(Double.parseDouble(paidAmount));
		lastCharge.setPaidCompleted(true);
		lastCharge.setPayType(1);
		lastCharge.setExitDate1(new Date());
		lastCharge.setRejectReason("weipay"+new SimpleDateFormat(Constants.DATEFORMAT).format(new Date()));
		poschargedataService.update(lastCharge);
		//支付宝记录的添加  微信暂时添加进来
		Alipayrecord alipayrecord=new Alipayrecord();
		alipayrecord.setStatus("5");
		alipayrecord.setMoney(Double.parseDouble(paidAmount));
		alipayrecord.setAlitradeno(tradeNo);
		alipayrecord.setPoschargeid(poschargeId);
		alipayrecord.setDate(new Date());
		int num=alipayrecordService.insertSelective(alipayrecord);
		Map<String, Object> ret = new HashMap<String, Object>();
		if (num==1) {
			ret.put("status", 1001);
			ret.put("message", "success");
		}
		else {
			ret.put("status", 1002);
			ret.put("message", "failed");
		}
		return Utility.gson.toJson(ret);
	}
}

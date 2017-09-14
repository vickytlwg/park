package com.park.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.Constants;
import com.park.model.Monthuser;
import com.park.model.Njcarfeerecord;
import com.park.model.Njmonthuser;
import com.park.service.NjCarFeeRecordService;
import com.park.service.NjMonthUserService;
import com.park.service.Utility;

import ch.qos.logback.classic.pattern.Util;

@Controller
@RequestMapping("njpark")
public class NjparkController {
	@Autowired
	NjMonthUserService njMonthUserService;
	@Autowired
	NjCarFeeRecordService njcarFeeRecordService;
	
	@RequestMapping(value="carArrive",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String carArrive(@RequestBody Map<String, Object> args) throws ParseException{
		Njcarfeerecord njcarfeerecord=new Njcarfeerecord();
		njcarfeerecord.setTradedate(new SimpleDateFormat(Constants.DATEFORMAT).parse((String) args.get("tradeDate")));
		njcarfeerecord.setCarnumber((String) args.get("carNumber"));
		njcarfeerecord.setCartype((String) args.get("carType"));
		njcarfeerecord.setArrivetime(new SimpleDateFormat(Constants.DATEFORMAT).parse((String) args.get("arriveTime")));
		njcarfeerecord.setParkid((int)args.get("parkId"));
		njcarfeerecord.setParkname((String) args.get("parkName"));
		njcarfeerecord.setTradenumber((String) args.get("tradeNumber"));
		njcarfeerecord.setStoptype((String) args.get("stopType"));
		njcarfeerecord.setPaidmoney((int)args.get("paidMoney"));
		njcarfeerecord.setPicturepath((String) args.get("picturePath"));
		int num=njcarFeeRecordService.insertSelective(njcarfeerecord);
		
		Map<String, Object> ret = new HashMap<String, Object>();
		if (num==1) {
			return Utility.createJsonMsg(1001, "success");
		}
		else{
			return Utility.createJsonMsg(1002, "failed");
		}
	}
	@RequestMapping(value="carLeave",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String carLeave(@RequestBody Map<String, Object> args) throws ParseException{
		Njcarfeerecord njcarfeerecord=new Njcarfeerecord();
		njcarfeerecord.setTradedate(new SimpleDateFormat(Constants.DATEFORMAT).parse((String) args.get("tradeDate")));
		njcarfeerecord.setCarnumber((String) args.get("carNumber"));
		njcarfeerecord.setCartype((String) args.get("carType"));
		njcarfeerecord.setArrivetime(new SimpleDateFormat(Constants.DATEFORMAT).parse((String) args.get("arriveTime")));
		njcarfeerecord.setParkid((int)args.get("parkId"));
		njcarfeerecord.setParkname((String) args.get("parkName"));
		njcarfeerecord.setTradenumber((String) args.get("tradeNumber"));
		njcarfeerecord.setStoptype((String) args.get("stopType"));
		njcarfeerecord.setPaidmoney((int)args.get("paidMoney"));
		njcarfeerecord.setPicturepath((String) args.get("picturePath"));
		List<Njcarfeerecord> njcarfeeSelecteds=njcarFeeRecordService.selectByTradeNumber((String) args.get("tradeNumber"));
		if (njcarfeeSelecteds.isEmpty()) {
			return Utility.createJsonMsg(1002, "无入场信息");
		}
		njcarfeerecord=njcarfeeSelecteds.get(0);
		njcarfeerecord.setLeavetime(new SimpleDateFormat(Constants.DATEFORMAT).parse((String)args.get("leaveTime")));
		njcarfeerecord.setShouldcharge((int) args.get("shouldCharge"));
		njcarfeerecord.setArrearspay((int) args.get("arrearsPay"));
		njcarfeerecord.setDiscount((int) args.get("discount"));
		njcarfeerecord.setOtherpay((int) args.get("otherPay"));
		njcarfeerecord.setRealpay((int) args.get("realPay"));
		njcarfeerecord.setInvoiceurl((String) args.get("invoiceUrl"));
		int num=njcarFeeRecordService.updateByPrimaryKeySelective(njcarfeerecord);
		if (num==1) {
			return Utility.createJsonMsg(1001, "success");
		}
		else {
			return Utility.createJsonMsg(1002, "failed");
		}	
	}
	@RequestMapping(value="monthUserInsert",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String monthUserInsert(@RequestBody Map<String, Object> args) throws ParseException{
		Njmonthuser njmonthuser=new Njmonthuser();
		njmonthuser.setCardnumber((String) args.get("cardNumber"));
		njmonthuser.setCarnumber((String) args.get("carNumber"));
		njmonthuser.setMembername((String) args.get("memberName"));
		njmonthuser.setMonthid((String) args.get("monthId"));
		njmonthuser.setCartype((String) args.get("carType"));
		njmonthuser.setMonthtype((String) args.get("monthType"));
		njmonthuser.setTradenumber((String) args.get("tradeNumber"));
		njmonthuser.setTradedate(new SimpleDateFormat(Constants.DATEFORMAT).parse((String)args.get("tradeDate")));
		njmonthuser.setEffectivetimes((String) args.get("effectiveTimes"));
		njmonthuser.setMonthfee((int) args.get("monthFee"));
		njmonthuser.setMonthstart(new SimpleDateFormat(Constants.DATEFORMAT).parse((String) args.get("monthStart")));
		njmonthuser.setMonthend(new SimpleDateFormat(Constants.DATEFORMAT).parse((String) args.get("monthEnd")));
		njmonthuser.setRechargebefore((int) args.get("rechargeBefore"));
		njmonthuser.setRechargeafter((int) args.get("rechargeAfter"));
		njmonthuser.setRechargemoney((int) args.get("rechargeMoney"));
		njmonthuser.setRealpay((int) args.get("realPay"));
		njmonthuser.setShouldcharge((int) args.get("shouldCharge"));
		njmonthuser.setDiscount((int) args.get("discount"));
		njmonthuser.setStandardfees((int) args.get("standardFees"));
		njmonthuser.setPreferential((int)args.get("preferential"));
		njmonthuser.setPicturepath((String) args.get("picturePath"));
		njmonthuser.setDescription((String) args.get("description"));
		int num=njMonthUserService.insert(njmonthuser);
		if (num==1) {
			return Utility.createJsonMsg(1001, "success");
		}
		else {
			return Utility.createJsonMsg(1002, "failed");
		}	
	}
}

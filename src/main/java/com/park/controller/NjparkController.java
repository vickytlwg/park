package com.park.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.park.model.Constants;
import com.park.model.Monthuser;
import com.park.model.Njcarfeerecord;
import com.park.model.Njmonthuser;
import com.park.service.AliParkFeeService;
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
	@Autowired
	AliParkFeeService parkFeeService;
	
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
	///此为美凯龙的调用接口
	@RequestMapping(value="enterCar",method=RequestMethod.GET,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String carArrivehx(HttpServletRequest request) throws ParseException, UnsupportedEncodingException, AlipayApiException{
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String carNo=new String((request.getParameter("carNo")).getBytes("iso-8859-1"),"utf-8");
		String orderNo=new String((request.getParameter("orderNo")).getBytes("iso-8859-1"),"utf-8");
		String enterTime=new String((request.getParameter("enterTime")).getBytes("iso-8859-1"),"utf-8");
		String carType=new String((request.getParameter("carType")).getBytes("iso-8859-1"),"utf-8");
		String gateName=new String((request.getParameter("gateName")).getBytes("iso-8859-1"),"utf-8");
		String operatorName=new String((request.getParameter("operatorName")).getBytes("iso-8859-1"),"utf-8");

		Njcarfeerecord njcarfeerecord=new Njcarfeerecord();
		njcarfeerecord.setArrivetime(new Date());
		njcarfeerecord.setCarnumber(carNo);
		njcarfeerecord.setCartype(carType);
		njcarfeerecord.setParkname(gateName);
		njcarfeerecord.setStoptype("入场");
		
		Map<String, String> args=new HashMap<>();
		args.put("parking_id", "PI1501317472942184881");
		args.put("car_number", carNo);
		args.put("in_time", enterTime);
		parkFeeService.parkingEnterinfoSync(args);
		
		njcarfeerecord.setInvoiceurl(enterTime);
		njcarFeeRecordService.insertSelective(njcarfeerecord);
		return Utility.createJsonMsg(1001, "ok");
	}
	///此为美凯龙的调用接口
	@RequestMapping(value="outCar",method=RequestMethod.GET,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String outcar(HttpServletRequest request) throws ParseException{
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String carNo=request.getParameter("carNo");
		String orderNo=request.getParameter("orderNo");
		String carType=request.getParameter("carType");
		String outTime=request.getParameter("outTime");
		String gateName=request.getParameter("gateName");
		String operatorName=request.getParameter("operatorName");

		Njcarfeerecord njcarfeerecord=new Njcarfeerecord();
		njcarfeerecord.setArrivetime(new Date());
		njcarfeerecord.setCarnumber(carNo);
		njcarfeerecord.setInvoiceurl(orderNo);
		njcarfeerecord.setCartype(carType);
		njcarfeerecord.setParkname(gateName);
		njcarfeerecord.setStoptype("出场");
	
		njcarfeerecord.setInvoiceurl(outTime);
		njcarFeeRecordService.insertSelective(njcarfeerecord);
		return Utility.createJsonMsg(1001, "ok");
	}
}

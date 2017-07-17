package com.park.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.AuthUser;
import com.park.model.AuthUserRole;
import com.park.model.Constants;
import com.park.model.Page;
import com.park.model.PosChargeData;
import com.park.service.AuthorityService;
import com.park.service.HardwareService;
import com.park.service.PosChargeDataService;
import com.park.service.UserPagePermissionService;
import com.park.service.Utility;

@RequestMapping("barrierCharge")
@Controller
public class BarrierChargeController {
	@Autowired
	HardwareService hardwareService;
	@Autowired
	PosChargeDataService chargeSerivce;
	
	
	@RequestMapping(value="insert",method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String insert(@RequestBody Map<String, String> args) throws ParseException{
		String mac= args.get("mac");
		String cardNumber=args.get("cardNumber");
		PosChargeData charge=new PosChargeData();
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> info=hardwareService.getInfoByMac(mac);
		if (info==null) {
			ret.put("status", 1002);
			return Utility.gson.toJson(ret);
		}
		Integer parkId=(Integer) info.get("parkID");
		String parkName=(String) info.get("Name");
		
		charge.setCardNumber(cardNumber);
		charge.setParkId(parkId);
		charge.setParkDesc(parkName);
		charge.setEntranceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		int num = chargeSerivce.insert(charge);
		if (num==1) {
			ret.put("status", 1001);
		}
		else {
			ret.put("status", 1002);
		}
		return Utility.gson.toJson(ret);
	}
	
	@RequestMapping(value="exit",method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String exit(@RequestBody Map<String, Object> args) throws ParseException{
		String cardNumber=(String) args.get("cardNumber");
		List<PosChargeData> queryCharges = null;
		String exitDate=(String) args.get("exitDate");
		if (exitDate != null) {
			Date eDate = new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
			try {
				queryCharges = chargeSerivce.getDebt(cardNumber, eDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		} else {
			try {
				queryCharges = chargeSerivce.getDebt(cardNumber);
			} catch (Exception e) {
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		}
		PosChargeData payRet=queryCharges.get(0);
		payRet.setPaidCompleted(true);
		payRet.setPaidMoney(payRet.getChargeMoney());
		payRet.setUnPaidMoney(0);
		payRet.setOperatorId("道闸");
		int num = chargeSerivce.update(payRet);
		if (num==1) {
			return Utility.createJsonMsg(1001, "success", payRet);
		}
		else {
			return Utility.createJsonMsg(1002, "fail");
		}
	
}}

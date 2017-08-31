package com.park.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.park.dao.PosChargeDataDAO;
import com.park.model.AuthUser;
import com.park.model.AuthUserRole;
import com.park.model.Constants;
import com.park.model.Monthuser;
import com.park.model.Page;
import com.park.model.Parktoalipark;
import com.park.model.PosChargeData;
import com.park.service.AliParkFeeService;
import com.park.service.AuthorityService;
import com.park.service.HardwareService;
import com.park.service.MonthUserParkService;
import com.park.service.MonthUserService;
import com.park.service.ParkToAliparkService;
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
	@Autowired
	PosChargeDataDAO chargeDao;
	@Autowired
	ParkToAliparkService parkToAliparkService;
	@Autowired
	AliParkFeeService aliparkFeeService;
	@Autowired
	MonthUserService monthUserService;
	
	@RequestMapping(value="insert",method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String insert(@RequestBody Map<String, String> args) throws ParseException, AlipayApiException{
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
			List<Parktoalipark> parktoaliparks=parkToAliparkService.getByParkId(parkId);
			if (!parktoaliparks.isEmpty()) {
				Parktoalipark parktoalipark=parktoaliparks.get(0);
				Map<String, String> argstoali=new HashMap<>();
				argstoali.put("parking_id", parktoalipark.getAliparkingid());
				argstoali.put("car_number", cardNumber);
				argstoali.put("in_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				aliparkFeeService.parkingEnterinfoSync(argstoali);
			}
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
}
	@RequestMapping(value="touched",method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String touched(@RequestBody Map<String, String> args) throws ParseException, AlipayApiException{
		String mac= args.get("mac");
		String cardNumber=args.get("cardNumber");
		boolean largeCar=Boolean.parseBoolean(args.get("largeCar"));
		PosChargeData charge=new PosChargeData();
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> info=hardwareService.getInfoByMac(mac);
		if (info==null) {
			return Utility.createJsonMsg(1002, "fail");
		}
		Map<String, Object> dataMap = new TreeMap<String, Object>();
		List<Monthuser> monthusers=monthUserService.getByCardNumber(cardNumber);
		if (monthusers.isEmpty()) {
			dataMap.put("uT", "0");			
		}
		else{
			Monthuser monthuser=monthusers.get(0);
			dataMap.put("uT", "1");
		//	dataMap.put("monthUserStartDate", new SimpleDateFormat(Constants.DATEFORMAT).format(monthuser.getStarttime()));
			dataMap.put("eD", new SimpleDateFormat(Constants.DATEFORMAT).format(monthuser.getEndtime()));
			Long diff=(monthuser.getEndtime().getTime()-(new Date()).getTime());
			if (diff>0) {
				int leftDays=(int) (diff/(1000*60*60*24));
				dataMap.put("ds", String.valueOf(leftDays));
			}
			else {
				dataMap.put("ds","-1");
			}
			
		}
		if (info==null) {
			ret.put("status", 1002);
			return Utility.gson.toJson(ret);
		}
		int channelFlag=(int) info.get("channelFlag");
		Integer parkId=(Integer) info.get("parkID");
		String parkName=(String) info.get("Name");
		List<Parktoalipark> parktoaliparks=parkToAliparkService.getByParkId(parkId);
		//入口硬件
		if (channelFlag==1) {			
			if (largeCar==true) {
				charge.setIsLargeCar(true);
			}
			dataMap.put("cT", "in");	
			charge.setCardNumber(cardNumber);
			charge.setParkId(parkId);
			charge.setParkDesc(parkName);
			charge.setEntranceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			int num = chargeSerivce.insert(charge);
			if (num==1) {
				if (!parktoaliparks.isEmpty()) {
					Parktoalipark parktoalipark=parktoaliparks.get(0);
					Map<String, String> argstoali=new HashMap<>();
					argstoali.put("parking_id", parktoalipark.getAliparkingid());
					argstoali.put("car_number", cardNumber);
					argstoali.put("in_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					aliparkFeeService.parkingEnterinfoSync(argstoali);
				}
				ret.put("status", 1001);
			}
			else {
				ret.put("status", 1002);
			}
			return Utility.createJsonMsgWithoutMsg(1001, dataMap);
		}
		else {		
			dataMap.put("cT", "out");	
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
			} else {//出场硬件
				try {
					queryCharges = chargeSerivce.getDebt(cardNumber);
				} catch (Exception e) {
					return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
				}
			}
			if (queryCharges.isEmpty()) {
				if (largeCar==true) {
					charge.setIsLargeCar(true);
				}
//				charge.setCardNumber(cardNumber);
//				charge.setParkId(parkId);
//				charge.setParkDesc(parkName);
//				charge.setEntranceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//				chargeSerivce.insert(charge);
				if (!parktoaliparks.isEmpty()) {
					Parktoalipark parktoalipark=parktoaliparks.get(0);
					Map<String, String> argstoali=new HashMap<>();
					argstoali.put("parking_id", parktoalipark.getAliparkingid());
					argstoali.put("car_number", cardNumber);
					argstoali.put("out_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					aliparkFeeService.parkingExitinfoSync(argstoali);
				}
				return Utility.createJsonMsgWithoutMsg(1003, dataMap);
			}
			//PosChargeData payRet=queryCharges.get(queryCharges.size()-1);
			PosChargeData payRet=queryCharges.get(0);
			payRet.setPaidCompleted(true);
			payRet.setPaidMoney(payRet.getChargeMoney());
			payRet.setUnPaidMoney(0);
			payRet.setOperatorId("道闸");
			int num = chargeSerivce.update(payRet);
			dataMap.put("eD", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRet.getEntranceDate()));
				if (monthusers.isEmpty()) {
					dataMap.put("my", String.valueOf(payRet.getChargeMoney()));
				}						
			
			if (num==1) {
				return Utility.createJsonMsgWithoutMsg(1001, dataMap);
			}
			else {
				return Utility.createJsonMsg(1002, "fail");
			}	
		}
	}
	
	@RequestMapping(value="getTypeByMac",method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getTypeByMac(@RequestBody Map<String, String> args) {
		String mac= args.get("mac");
		Map<String, Object> info=hardwareService.getInfoByMac(mac);
		if (info==null) {
			return Utility.createJsonMsg(1002, "fail");
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		int channelFlag=(int) info.get("channelFlag");
		if (channelFlag==1) {	
			dataMap.put("channelType", "in");
		}
		else {
			dataMap.put("channelType", "out");
		}
		return Utility.createJsonMsg(1001, "success", dataMap);
	}
}

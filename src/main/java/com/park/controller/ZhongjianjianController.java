package com.park.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
@RequestMapping("zjj")
public class ZhongjianjianController {
	@Autowired
	PosChargeDataService posChargeDataService;

	private static Log logger = LogFactory.getLog(ZhongjianjianController.class);
	@RequestMapping(value = "/carIn", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8" })
	@ResponseBody
	public String carIn(@RequestBody Map<String, Object> args) {
		logger.info(args.toString());
		Map<String, Object> result=new HashMap<>();
		result.put("errorcode", 200);
		result.put("message", "成功");
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value = "/carOut", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8" })
	@ResponseBody
	public String carOut(@RequestBody Map<String, Object> args) {
		logger.info(args.toString());
		Map<String, Object> result=new HashMap<>();
		result.put("errorcode", 200);
		result.put("message", "成功");
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value = "/evt", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8" })
	@ResponseBody
	public String evt(@RequestBody Map<String, Object> args) throws Exception {
		logger.info("evt:"+args.toString());
		String evt=(String) args.get("evt");
		String plateNumber =(String) args.get("plateNumber");
		String picUrlIn =(String) args.get("picUrlIn");
		String timeIn =(String) args.get("timeIn");
		String parkingCode =(String) args.get("parkingCode");
		String parkingName =(String) args.get("parkingName");
		String happenTime =(String) args.get("happenTime");
		String parkingActId =(String) args.get("parkingActId");
	
		if (evt.equals("evt.car.in")) {
			PosChargeData chargeData=new PosChargeData();
			chargeData.setCardNumber(plateNumber);
			chargeData.setEntranceDate(timeIn);
			chargeData.setParkId(308);
			chargeData.setParkDesc(parkingName);
			chargeData.setUrl(picUrlIn);
			posChargeDataService.insert(chargeData);
		}
		if (evt.equals("evt.car.out")) {
			String timeOut =(String) args.get("timeOut");
			PosChargeData posChargeData=posChargeDataService.getByCardNumberAndPark(plateNumber, 308).get(0);
			if (posChargeData!=null) {
				posChargeDataService.getDebt(plateNumber);
			}
		}
		Map<String, Object> result=new HashMap<>();
		result.put("errorcode", 200);
		result.put("message", "成功");
		return Utility.gson.toJson(result);
	}
}

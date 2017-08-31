package com.park.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayEcoMycarParkingVehicleQueryRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipayEcoMycarParkingVehicleQueryResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.park.model.Constants;
import com.park.model.Hongxingrecord;
import com.park.model.Park;
import com.park.model.PosChargeData;
import com.park.service.HongxingRecordService;
import com.park.service.HongxingService;
import com.park.service.Utility;

@Controller
@RequestMapping("hx")
public class HongxingController {
	@Autowired
	HongxingService hongxingService;
	@Autowired
	HongxingRecordService hongxingRecordService;

	public String APP_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMellSPcYZV7dk0/"
			+ "xvIgHE8jnHfIpgTt6KL+rLsSGYmFQ8aOkRvTnZSP92+98Z0NWzDMKU5ohf3QVPzO"
			+ "X/QhmGRylFMUwEThZ8etlUuKDxk7hUjmkI1Okb4Qunczr8Q9jpfX8FFRGV5j52Od"
			+ "p6ncY2pkEWuUBrwqH9VE16I8C/9tAgMBAAECgYEAhiOBtit/QVFHphWA1POwMZgK"
			+ "rAybR1qV4NXeNn6tu9FXPVRSuPCQwt2L8X8clFoB+CJkanMd++/6+jSrEbt0yG/7"
			+ "W5mOPVJMGS1vPFWjmofwZqfPEgH4zqXmcTV8DV7QkYa8pBECuV5n+sA6eKeiw3dk"
			+ "NOj/hxpaEvHmE6q02p0CQQDzRDocuh/UyL5k4sMSD2OadQ8Fq+yIK+H5MSNZMo+U"
			+ "/YLy6ZGVIr5ZO3yql1Bq2oK+b8f1nZL7vSxp4HizsNIPAkEA0hjZ6aK/HMl67234"
			+ "1smrRYZw3UjHKdUvW/P1TcCcqyMEp6Sf2m9RYUKxvviB8SPRwc6c9HhhgBj9rdyZ"
			+ "9ioiwwJAMXyrpbRnTU4ZDUTkEgR3arBtgeXblEf5DExmuHqEovZ/cRL6vq/2sQhc"
			+ "8AcgINyaxErRDrIjeHqfUlqLs2JBGQJBAKhPTFOFE4FmT1v8R7saOGEsQMKliRgU"
			+ "Nyp9F+lAAsJ+/T2n/n+pahJ2sZqBzud1gJa4hLi8r69FVgSwk47HVq0CQQDa8iXR"
			+ "319hbSivw4z4BWQ2hjWALaUAocVP4+4tV1zAINJkopqxpMJ7HivXkO/DczDEdbnpEeFNfMCTp5GoFgdO";
	public String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	String URL = "https://openapi.alipay.com/gateway.do";
	String APP_ID = "2015101400439228";
	String FORMAT = "json";
	String CHARSET = "UTF-8";
	String SIGN_TYPE = "RSA";
	AlipayClient alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET,
			ALIPAY_PUBLIC_KEY, SIGN_TYPE);

	@RequestMapping(value = "getRecord", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getRecord(@RequestBody Map<String, String> args) {
		String carNumber = args.get("carNumber");
		Map<String, Object> data = hongxingService.getFeeByCarNumber(carNumber);

		return Utility.createJsonMsg(1001, "sucess", data);
	}
	
	@RequestMapping(value = "/parkingPayH5", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	public String aliParkingPayH5(ModelMap modelMap,@RequestParam("auth_code")String auth_code,@RequestParam("car_id")String car_id,@RequestParam("parking_id")String parking_id) throws Exception{
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode(auth_code);
		request.setGrantType("authorization_code");
		 AlipaySystemOauthTokenResponse oauthTokenResponse =null;
		try {
		   oauthTokenResponse = alipayClient.execute(request);
		} catch (AlipayApiException e) {
		    //处理异常
		    e.printStackTrace();
		}		
		AlipayUserInfoShareRequest request2 = new AlipayUserInfoShareRequest();
		String access_token = oauthTokenResponse.getAccessToken();
		String userId=oauthTokenResponse.getUserId();
		try {
		    AlipayUserInfoShareResponse userinfoShareResponse = alipayClient.execute(request2, access_token);
		   // userId=userinfoShareResponse.getUserId();
		} catch (AlipayApiException e) {
		    //处理异常
		    e.printStackTrace();
		}
		
		AlipayEcoMycarParkingVehicleQueryRequest request3 = new AlipayEcoMycarParkingVehicleQueryRequest();
		request3.setBizContent("{" +
		"\"car_id\":\""
		+ car_id
		+ "\"" +
		"  }");
		String carNumber="";
		try {
			AlipayEcoMycarParkingVehicleQueryResponse response3 = alipayClient.execute(request3,access_token);
			carNumber=response3.getCarNumber();
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		modelMap.addAttribute("carNumber",carNumber);
		Map<String, Object> data=new HashMap<>();
		try {
			 data = hongxingService.getFeeByCarNumber(carNumber);
		} catch (Exception e) {
				return "alipayh5/noRecord";
		}
		
		String totalAmount=(String) data.get("totalAmount");
		Integer recordId=(Integer) data.get("recordId");
	
//		PosChargeData lastCharge = charges.get(0);
//		
		modelMap.addAttribute("charge",totalAmount);
		modelMap.addAttribute("enterDate",(String)data.get("enterTime"));
//		modelMap.addAttribute("exitDate",(String)data.get("totalAmount"));
//		if (lastCharge.getExitDate()==null) {
//			lastCharge.setExitDate1(new Date());
//		}
//		long parkingDuration=(lastCharge.getExitDate().getTime()-lastCharge.getEntranceDate().getTime())/60000;
//		modelMap.addAttribute("parkingDuration",parkingDuration);
//		Park park=parkService.getParkById(lastCharge.getParkId());
//		modelMap.addAttribute("parkName",park.getName());
		modelMap.addAttribute("chargeId",recordId);
//		modelMap.addAttribute("userId",userId);
//		modelMap.addAttribute("parkingId",parking_id);
		return "alipayh5/index";
	}
}

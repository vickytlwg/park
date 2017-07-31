package com.park.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayEcoMycarParkingConfigSetRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.park.model.Constants;
import com.park.service.AliParkFeeService;
import com.park.service.Utility;

@Controller
@RequestMapping("alipay")
public class AlipayController {
	@Autowired
	AliParkFeeService parkFeeService;
	
	public String APP_PRIVATE_KEY="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMellSPcYZV7dk0/"
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

	@RequestMapping(value = "getAliPayUrl", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getAliPayUrl(@RequestBody Map<String, Object> args) throws AlipayApiException {
		String amount=(String) args.get("amount");		
		// Map<String, Object> resultMap=new HashMap<>();
		String URL = "https://openapi.alipay.com/gateway.do";
		String APP_ID = "2015101400439228";
		String FORMAT = "json";
		String CHARSET = "UTF-8";
		String SIGN_TYPE = "RSA";
		AlipayClient alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET,
				ALIPAY_PUBLIC_KEY, SIGN_TYPE);
		// alipayClient.
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
		request.setBizModel(model);
		String outTradeNo=new Date().getTime() + "fee";
		request.setNotifyUrl("http://www.iotclouddashboard.com/park/alipay/notifyUrl/"+outTradeNo);
		model.setOutTradeNo(outTradeNo);
		model.setTotalAmount(amount);
		model.setSubject("parking_fee");
		AlipayTradePrecreateResponse response = alipayClient.execute(request);
		
		return Utility.gson.toJson(response);
	}
	@RequestMapping(value = "notifyUrl/{outTradeNo}", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String notifyUrl(@RequestBody Map<String, Object> args){
		Constants.dataReceived.append(args.toString());
		return null;
	}
	@RequestMapping(value = "queryTradeNo", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String queryTradeNo(@RequestBody Map<String, Object> args) throws AlipayApiException{
		String URL = "https://openapi.alipay.com/gateway.do";
		String APP_ID = "2015101400439228";
		String FORMAT = "json";
		String CHARSET = "UTF-8";
		String SIGN_TYPE = "RSA";
		AlipayClient alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET,
				ALIPAY_PUBLIC_KEY, SIGN_TYPE);
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		String outTradeNO=(String) args.get("outTradeNO");
		request.setBizContent("{" +
				"\"out_trade_no\":\""
				+ outTradeNO
				+ "\"" +
		//		"\"trade_no\":\"2014112611001004680 073956707\"" +
				"  }");
		AlipayTradeQueryResponse response = alipayClient.execute(request);
		return Utility.gson.toJson(response);
	}
	
	@RequestMapping(value = "parkRegister", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String parkRegister(@RequestBody Map<String, Object> args){
		String URL = "https://openapi.alipay.com/gateway.do";
		String APP_ID = "2015101400439228";
		String FORMAT = "json";
		String CHARSET = "UTF-8";
		String SIGN_TYPE = "RSA";
		AlipayClient alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET,
				ALIPAY_PUBLIC_KEY, SIGN_TYPE);
		AlipayEcoMycarParkingConfigSetRequest request = new AlipayEcoMycarParkingConfigSetRequest();
		String merchant_name;
		String merchant_service_phone;
		String account_no;
		String interface_name;
		String interface_url;
		
		request.setBizContent("{" +
				"\"merchant_name\":\"杭州立方\"," +
				"\"merchant_service_phone\":\"021-25413215\"," +
				"\"account_no\":\"1234567@126.com\"," +
				"      \"interface_info_list\":[{" +
				"        \"interface_name\":\"alipay.eco.mycar.parking.userpage.query\"," +
				"\"interface_type\":\"interface_page\"," +
				"\"interface_url\":\"https%3A%2F%2Fwww.parking24.cn%2Frf_carlife_alipay%2FCarLifeAction%21alipayAuth.action\"" +
				"        }]" +
				"  }");
		return null;
	}
	@RequestMapping(value = "getUserAccessToken", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public void getUserAccessToken(@RequestParam("auth_code")String auth_code) throws AlipayApiException{
		String URL = "https://openapi.alipay.com/gateway.do";
		String APP_ID = "2015101400439228";
		String FORMAT = "json";
		String CHARSET = "UTF-8";
		String SIGN_TYPE = "RSA";
		AlipayClient alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET,
				ALIPAY_PUBLIC_KEY, SIGN_TYPE);
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode(auth_code);
		request.setGrantType("authorization_code");
		AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
		String access_token=oauthTokenResponse.getAccessToken();
	}
	@RequestMapping(value = "testParking", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String testparking(@RequestBody Map<String, Object> args){
		Map<String, Object> parkconfigdata=new HashMap<>();
		parkconfigdata.put("merchant_name", "九比特停车");
		parkconfigdata.put("merchant_service_phone", "15882009230");
		
		return null;
	}
	@RequestMapping(value = "testset", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String testset() throws AlipayApiException{
		Map<String, String> args=new HashMap<>();
		args.put("merchant_name", "九比特");
		args.put("merchant_service_phone", "15882009230");
		args.put("account_no", "ninebit@aliyun.com");
		args.put("interface_url", "https%3a%2f%2fwww.iotclouddashboard.com%2fpark%2falipay%2f");
		return Utility.createJsonMsg("1001", "success", parkFeeService.parkConfigSet(args));
	}
	@RequestMapping(value = "testquery", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String testQuery() throws AlipayApiException{
		Map<String, String> args=new HashMap<>();
		args.put("merchant_name", "九比特");
		args.put("merchant_service_phone", "15882009230");
		args.put("account_no", "ninebit@aliyun.com");
		args.put("interface_url", "https%3a%2f%2fwww.iotclouddashboard.com%2fpark%2falipay%2f");
		return Utility.createJsonMsg("1001", "success", parkFeeService.parkConfigQuery(args));
	}
	@RequestMapping(value = "testParkCreate", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String testParkCreate() throws AlipayApiException{
		Map<String, String> args=new HashMap<>();
		args.put("city_id", "320100");
		args.put("equipment_name", "众彩物流园-九比特");
		args.put("out_parking_id", "3");
		args.put("parking_address", "江苏省-南京市-江宁区 东山街道大里社区");
		args.put("longitude", "118.814557");
		args.put("latitude", "32.047111");
		args.put("parking_start_time", "08:00:00");
		args.put("parking_end_time", "18:00:00");
		args.put("parking_number", "2000");
		args.put("parking_lot_type", "1");
		args.put("parking_type", "1");
		args.put("payment_mode", "1");
		args.put("pay_type", "3");
		args.put("shopingmall_id", "test");
		args.put("parking_fee_description", "小车首小时内，每15分钟0.5元，首小时后，每15分钟0.5元。前15分钟免费");
		args.put("contact_name", "刘猛");
		args.put("contact_mobile", "15882009230");
		args.put("parking_name", "众彩物流园");
		return Utility.createJsonMsg("1001", "success", parkFeeService.parkingInfoCreate(args));
	}
	@RequestMapping(value = "testParkingEnterinfoSync", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String testParkingEnterinfoSync() throws AlipayApiException{
		Map<String, String> args=new HashMap<>();
		args.put("parking_id", "PI1501317472942184881");
		args.put("car_number", "川A1LM97");
		args.put("in_time", "2017-07-28 03:07:50");
		return Utility.createJsonMsg("1001", "success", parkFeeService.parkingEnterinfoSync(args));
	}
	@RequestMapping(value = "testParkingExitinfoSync", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String testParkingExitinfoSync() throws AlipayApiException{
		Map<String, String> args=new HashMap<>();
		args.put("parking_id", "PI1501317472942184881");
		args.put("car_number", "川A1LM97");
		args.put("in_time", "2017-07-28 07:07:50");
		return Utility.createJsonMsg("1001", "success", parkFeeService.parkingEnterinfoSync(args));
	}
	@RequestMapping(value = "testParkingOrderSync", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String testParkingOrderSync() throws AlipayApiException{
		Map<String, String> args=new HashMap<>();
		args.put("user_id", "");
		args.put("out_parking_id", "");
		args.put("parking_name", "");
		args.put("car_number", "");
		args.put("out_order_no", "");
		args.put("order_status", "");
		args.put("order_time", "");
		args.put("order_no", "");
		args.put("pay_time", "");
		args.put("pay_type", "");
		args.put("pay_money", "");
		args.put("in_time", "");
		args.put("parking_id", "");
		args.put("in_duration", "");
		args.put("card_number", "");
		return Utility.createJsonMsg("1001", "success", parkFeeService.parkingOrderSync(args));
	}
}

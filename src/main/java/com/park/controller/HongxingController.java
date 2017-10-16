package com.park.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Utilities;

import org.bouncycastle.jce.provider.JDKDSASigner.noneDSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipayEcoMycarParkingVehicleQueryResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.park.model.Alipayrecord;
import com.park.model.Constants;
import com.park.model.Hongxingrecord;
import com.park.model.Park;
import com.park.model.PosChargeData;
import com.park.service.AliParkFeeService;
import com.park.service.AlipayrecordService;
import com.park.service.HongxingRecordService;
import com.park.service.HongxingService;
import com.park.service.ParkToAliparkService;
import com.park.service.PosChargeDataService;
import com.park.service.Utility;

@Controller
@RequestMapping("alipay")
public class HongxingController {
	@Autowired
	HongxingService hongxingService;
	@Autowired
	HongxingRecordService hongxingRecordService;
	@Autowired
	AlipayrecordService alipayrecordService;
	@Autowired
	PosChargeDataService poschargedataService;
	@Autowired
	ParkToAliparkService parkToAliparkService;
	@Autowired
	AliParkFeeService parkFeeService;
	
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
	public String getRecord(@RequestBody Map<String, String> args,ModelMap modelMap) throws Exception {
	
		String code=hongxingService.creatPayOrder("20171008170709140-A1N603");
	
	
	//	Boolean success=hongxingService.payOrderNotify("2.5","20170926085443491-AV738F","20170926085443491-AV738F");
		return Utility.createJsonMsg(1001, "sucess", code);
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
		Map<String, Object> data=null;
		try {
			 data = hongxingService.getFeeByCarNumber(carNumber);
		} catch (Exception e) {
				return "alipayh5/noRecord";
		}	
		if (data==null) {
			return "alipayh5/noRecord";
		}
		
		
		String totalAmount=String.valueOf((double) data.get("totalAmount")/10);
	
		PosChargeData lastCharge = new PosChargeData();
		lastCharge.setCardNumber(carNumber);
		lastCharge.setParkId(3);
		lastCharge.setChargeMoney((double) data.get("totalAmount")/10);
		String enterTimeStr=((String)data.get("enterTime")).replace("/", "-");
		lastCharge.setEntranceDate(enterTimeStr);
		lastCharge.setExitDate1(new Date());
		lastCharge.setParkDesc("美凯龙停车场");		
		lastCharge.setOperatorId((String)data.get("orderNo"));
		poschargedataService.insert(lastCharge);
		
		List<PosChargeData> charges=poschargedataService.queryDebt(carNumber,new Date());
		lastCharge = charges.get(0);
		
		modelMap.addAttribute("charge",totalAmount);
		modelMap.addAttribute("enterDate",enterTimeStr);
//		modelMap.addAttribute("exitDate",(String)data.get("totalAmount"));
//		if (lastCharge.getExitDate()==null) {
//			lastCharge.setExitDate1(new Date());
//		}
		long parkingDuration=(lastCharge.getExitDate().getTime()-lastCharge.getEntranceDate().getTime())/60000;
		modelMap.addAttribute("parkingDuration",parkingDuration);
//		Park park=parkService.getParkById(lastCharge.getParkId());
		modelMap.addAttribute("parkName","美凯龙停车场");
		modelMap.addAttribute("chargeId",lastCharge.getId());
		modelMap.addAttribute("userId",userId);
		modelMap.addAttribute("parkingId",parking_id);
		return "alipayh5/index";
	}
	
	@RequestMapping(value = "quickPayWeb/{chargeId}/{userId}/{parkingId}", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public void quickPayWeb(HttpServletRequest httpRequest,HttpServletResponse httpResponse,@PathVariable int chargeId,@PathVariable String userId,@PathVariable String parkingId) throws Exception{
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
		alipayRequest.setReturnUrl("http://www.iotclouddashboard.com/park/alipay/returnUrlWebPay/"+chargeId);
		alipayRequest.setNotifyUrl("http://www.iotclouddashboard.com/park/alipay/notifyUrlWebPay");		
		PosChargeData lastCharge =poschargedataService.getById(chargeId);
		try {
			poschargedataService.getCharges(lastCharge.getCardNumber());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		String out_trade_no=lastCharge.getOperatorId();
		String total_amount=String.valueOf(lastCharge.getChargeMoney());
		Alipayrecord alipayrecord=new Alipayrecord();
		alipayrecord.setOutTradeNo(out_trade_no);
		alipayrecord.setPoschargeid(chargeId);
		alipayrecord.setUserid(userId);
		alipayrecord.setParkingid(parkingId);
		alipayrecord.setDate(new Date());
		alipayrecord.setStatus("0");		
		alipayrecordService.insertSelective(alipayrecord);
		
		String subject="美凯龙停车费";
		alipayRequest.setBizContent("{" +
				" \"out_trade_no\":\""
				+ out_trade_no
				+ "\"," +
				" \"total_amount\":\""
				+ total_amount
				+ "\"," +
				" \"subject\":\""
				+ subject
				+ "\"," +
				" \"product_code\":\"QUICK_WAP_PAY\"" +
				" }");//填充业务参数
				String form="";
				try {
				form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
				} catch (AlipayApiException e) {
				e.printStackTrace();
				}
				httpResponse.setContentType("text/html;charset=" + CHARSET);
				httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
				httpResponse.getWriter().flush();
				httpResponse.getWriter().close();
	}
	
	@RequestMapping(value = "returnUrlWebPay/{chargeId}", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	public String returnUrlWebPay(ModelMap modelMap,@PathVariable int chargeId){
		PosChargeData posChargeData=poschargedataService.getById(chargeId);
		
		modelMap.addAttribute("chargeMoney",posChargeData.getChargeMoney());
		return "alipayh5/success";
	}
	@RequestMapping(value = "notifyUrlWebPay", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String notifyUrlWebPay(@RequestParam("out_trade_no")String out_trade_no,HttpServletRequest request){
		String trade_no=request.getParameter("trade_no");
		String trade_status=request.getParameter("trade_status");
		String receipt_amount=request.getParameter("receipt_amount");
		List<Alipayrecord> alipayrecords=alipayrecordService.getByOutTradeNO(out_trade_no);
		Alipayrecord alipayrecord=alipayrecords.get(0);
		PosChargeData lastCharge =poschargedataService.getById(alipayrecord.getPoschargeid());
		if (trade_status.equals("TRADE_SUCCESS")) {			
			alipayrecord.setStatus("2");
			alipayrecord.setMoney(Double.parseDouble(receipt_amount));
			alipayrecord.setAlitradeno(trade_no);
			alipayrecordService.updateByPrimaryKeySelective(alipayrecord);	
			lastCharge.setPaidCompleted(true);			
			
			String code=hongxingService.creatPayOrder(lastCharge.getOperatorId());
			//通知
			Boolean success=hongxingService.payOrderNotify(receipt_amount, code, code);
			if (success) {
				lastCharge.setRejectReason("成功通知");
			}
			else {
				lastCharge.setRejectReason("失败通知");
			}
			poschargedataService.update(lastCharge);
			Map<String, String> args=new HashMap<>();
			args.put("user_id", alipayrecord.getUserid());
			
			args.put("out_parking_id", "mkl");
			args.put("parking_name", "美凯龙停车场");
			args.put("car_number", lastCharge.getCardNumber());
			args.put("out_order_no", out_trade_no);
			args.put("order_status", "0");
			args.put("order_time", new SimpleDateFormat(Constants.DATEFORMAT).format(alipayrecord.getDate()));
			args.put("order_no",trade_no);
			args.put("pay_time", new SimpleDateFormat(Constants.DATEFORMAT).format(new Date()));
			args.put("pay_type", "1");
			args.put("pay_money", receipt_amount);
			args.put("in_time", new SimpleDateFormat(Constants.DATEFORMAT).format(lastCharge.getEntranceDate()));
			args.put("parking_id", alipayrecord.getParkingid());
			long parkingDuration=(lastCharge.getExitDate().getTime()-lastCharge.getEntranceDate().getTime())/60000;
			args.put("in_duration",String.valueOf(parkingDuration));
			args.put("card_number", "*");
			
			try {
				parkFeeService.parkingOrderSync(args);
			} catch (AlipayApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			
			Map<String, String> args=new HashMap<>();
			args.put("user_id", alipayrecord.getUserid());
			args.put("out_parking_id", "mkl");
			args.put("parking_name", "美凯龙停车场");
			args.put("car_number", lastCharge.getCardNumber());
			args.put("out_order_no", out_trade_no);
			args.put("order_status", "1");
			args.put("order_time", new SimpleDateFormat(Constants.DATEFORMAT).format(alipayrecord.getDate()));
			args.put("order_no",trade_no);
			args.put("pay_time", new SimpleDateFormat(Constants.DATEFORMAT).format(new Date()));
			args.put("pay_type", "1");
			args.put("pay_money", receipt_amount);
			args.put("in_time", new SimpleDateFormat(Constants.DATEFORMAT).format(lastCharge.getEntranceDate()));
			args.put("parking_id", alipayrecord.getParkingid());
			long parkingDuration=(lastCharge.getExitDate().getTime()-lastCharge.getEntranceDate().getTime())/60000;
			args.put("in_duration",String.valueOf(parkingDuration));
			args.put("card_number", "*");
			
			try {
				parkFeeService.parkingOrderSync(args);
			} catch (AlipayApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "success";
	}
	@RequestMapping(value = "/carArrive", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String carArrive(@RequestBody Map<String, Object> args){
		String carNumber=(String) args.get("carNumber");
		String carType=(String) args.get("carType");
		String arriveTime=(String) args.get("arriveTime");
		String parkName=(String) args.get("parkName");
		int parkId=(int) args.get("parkId");
		return null;
	}
	
}

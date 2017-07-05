package com.park.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.park.model.Constants;
import com.park.service.Utility;

@Controller
@RequestMapping("alipay")
public class AlipayController {

	@RequestMapping(value = "getAliPayUrl", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getAliPayUrl(@RequestBody Map<String, Object> args) throws AlipayApiException {
		String amount=(String) args.get("amount");		
		// Map<String, Object> resultMap=new HashMap<>();
		String URL = "https://openapi.alipay.com/gateway.do";
		String APP_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMellSPcYZV7dk0/"
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
		String APP_ID = "2015101400439228";
		String FORMAT = "json";
		String CHARSET = "UTF-8";
		String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
		String SIGN_TYPE = "RSA";
		AlipayClient alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET,
				ALIPAY_PUBLIC_KEY, SIGN_TYPE);
		// alipayClient.
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
		request.setBizModel(model);
		request.setNotifyUrl("http://www.iotclouddashboard.com/park/alipay/notifyUrl/");
		model.setOutTradeNo(new Date().getTime() + "fee");
		model.setTotalAmount(amount);
		model.setSubject("parking_fee");
		AlipayTradePrecreateResponse response = alipayClient.execute(request);
		return Utility.gson.toJson(response);
	}
	@RequestMapping(value = "notifyUrl", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String notifyUrl(@RequestBody Map<String, Object> args){
		Constants.dataReceived.append(args.toString());
		return null;
	}
	@RequestMapping(value = "queryTradeNo", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String queryTradeNo(@RequestBody Map<String, Object> args) throws AlipayApiException{
		String URL = "https://openapi.alipay.com/gateway.do";
		String APP_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMellSPcYZV7dk0/"
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
		String APP_ID = "2015101400439228";
		String FORMAT = "json";
		String CHARSET = "UTF-8";
		String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
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
}

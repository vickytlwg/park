package com.park.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.park.controller.UserController;
import com.park.model.Constants;
import com.park.service.PingPPService;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.exception.PingppException;
import com.pingplusplus.model.Charge;

@Transactional
@Service
public class PingPPServiceImpl implements PingPPService{
	
	private static Log logger = LogFactory.getLog(UserController.class);
	
	@Override
	public String saomaPay(Map<String, Object> argMap) {
		
		//Map<String, Object> result = new HashMap<String, Object>();
		Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", argMap.get("amount"));
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", "test");
        chargeMap.put("body", "test");
        String order_no = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        chargeMap.put("order_no", order_no);
        chargeMap.put("channel", argMap.get("channel"));
        chargeMap.put("client_ip", argMap.get("client_ip"));        
        Map<String, String> app = new HashMap<String, String>();
        app.put("id",Constants.PINGAPPID);
        chargeMap.put("app", app);
        
        Pingpp.apiKey = Constants.PINGAPIKEY;
        try {
            //发起交易请求
            charge = Charge.create(chargeMap, Constants.PINGAPIKEY);          
         
        } catch (PingppException e) {
            e.printStackTrace();
            logger.error("order_no: " + argMap.get("order_no") + " charge fail");
        }
        logger.info("order_no: " + argMap.get("order_no") + " charge success");
        
        return new Gson().toJson(charge);

	}

	@Override
	public String query(Map<String, Object> argMap)  {
		// TODO Auto-generated method stub
		Charge charge = null;
		try {
			charge= Charge.retrieve((String) argMap.get("id"));
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Gson().toJson(charge);
	}

	@Override
	public String saomaPayByPosId(Map<String, Object> argMap) {
		// TODO Auto-generated method stub
		Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", argMap.get("amount"));
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", "test");
        chargeMap.put("body", "test");
        String posdataId=(String) argMap.get("poschargedataId");
        String order_no = new Date().getTime()+";"+String.valueOf(posdataId);;
        chargeMap.put("order_no", order_no);
        chargeMap.put("channel", argMap.get("channel"));
        chargeMap.put("client_ip", argMap.get("client_ip"));        
        Map<String, String> app = new HashMap<String, String>();
        app.put("id",Constants.PINGAPPID);
        chargeMap.put("app", app);
        
        Pingpp.apiKey = Constants.PINGAPIKEY;
        try {
            //发起交易请求
            charge = Charge.create(chargeMap, Constants.PINGAPIKEY);          
         
        } catch (PingppException e) {
            e.printStackTrace();
            logger.error("order_no: " + argMap.get("order_no") + " charge fail");
        }
        logger.info("order_no: " + argMap.get("order_no") + " charge success");
        
        return new Gson().toJson(charge);
	}
	
}

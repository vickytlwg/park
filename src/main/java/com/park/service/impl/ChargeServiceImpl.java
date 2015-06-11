package com.park.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.park.controller.UserController;
import com.park.model.Constants;
import com.park.service.ChargeService;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.PingppException;
import com.pingplusplus.model.Charge;

@Service
public class ChargeServiceImpl implements ChargeService{
	
	private static Log logger = LogFactory.getLog(UserController.class);
	
	@Override
	public String getCharge(Map<String, Object> argMap) {
		
		//Map<String, Object> result = new HashMap<String, Object>();
		Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", argMap.get("amount"));
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", "test");
        chargeMap.put("body", "test");
        chargeMap.put("order_no", argMap.get("order_no"));
        chargeMap.put("channel", argMap.get("channel"));
        chargeMap.put("client_ip", argMap.get("client_ip"));
        Map<String, String> app = new HashMap<String, String>();
        app.put("id",Constants.PINGAPPID);
        chargeMap.put("app", app);
        
        Pingpp.apiKey = Constants.PINGAPIKEY;
        try {
            //发起交易请求
            charge = Charge.create(chargeMap);
           
         
        } catch (PingppException e) {
            e.printStackTrace();
            logger.error("order_no: " + argMap.get("order_no") + " charge fail");
        }
        logger.info("order_no: " + argMap.get("order_no") + " charge success");
        
        return new Gson().toJson(charge);

	}

}

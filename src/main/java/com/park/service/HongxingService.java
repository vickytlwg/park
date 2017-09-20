package com.park.service;

import java.util.Map;

public interface HongxingService {

	public Map<String, Object> getFeeByCarNumber(String carNumber);
	
	public String creatPayOrder(String orderNo);
	
	public Boolean payOrderNotify(String paidMoney,String OrderNo,String Order);
}

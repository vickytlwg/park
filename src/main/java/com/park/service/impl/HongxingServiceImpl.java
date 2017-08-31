package com.park.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.park.model.Hongxingrecord;
import com.park.service.HongxingRecordService;
import com.park.service.HongxingService;
import com.park.service.HttpUtil;
@Service
public class HongxingServiceImpl implements HongxingService {

	@Autowired
	HongxingRecordService hongxingRecordService;
	
	@Override
	public Map<String, Object> getFeeByCarNumber(String carNumber) {
		// TODO Auto-generated method stub
		Map<String, Object> map=HttpUtil.get("http://www.168parking.com//ApiPlatform/GetCarPrice?SecretKey=xx&carNo="+carNumber);
		Object data=map.get("body");
		 Gson gson = new Gson();
		 Map<String, Object> mapdata=gson.fromJson((String) data, new TypeToken<Map<String, Object>>(){
         }.getType() );		
		 Map<String, Object> resultData=(Map<String, Object>) mapdata.get("Data");
			Hongxingrecord hongxingrecord = new Hongxingrecord();
			hongxingrecord.setCarlock((String) resultData.get("carLock"));
			hongxingrecord.setCarno((String) resultData.get("carNo"));
			hongxingrecord.setCartype((String) resultData.get("CarType"));
			hongxingrecord.setPayamount((float) (double) resultData.get("couponAmount"));
			hongxingrecord.setEntergatename((String) resultData.get("enterGateName"));
			hongxingrecord.setEnterimg((String) resultData.get("enterImg"));
			hongxingrecord.setEntertime((String) resultData.get("enterTime"));
			hongxingrecord.setEnteroperatorname((String) resultData.get("enterOperatorName"));
			hongxingrecord.setFreetime((int) (double) resultData.get("freeTime"));
			hongxingrecord.setFreetimeout((int) (double) resultData.get("freeTimeout"));
			hongxingrecord.setOrderno((String) resultData.get("orderNo"));
			hongxingrecord.setParkkey((String) resultData.get("parkKey"));
			hongxingrecord.setTotalamount((float) (double) resultData.get("totalAmount"));
			hongxingrecord.setCouponamount((float) (double) resultData.get("couponAmount"));
			int num=hongxingRecordService.insert(hongxingrecord);
			if (num==1) {
				resultData.put("recordId", hongxingrecord.getId());
			}
		return resultData;
	}

}

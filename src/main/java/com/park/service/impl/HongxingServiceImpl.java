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
		Map<String, Object> map=HttpUtil.get("http://139.196.19.162/ApiPlatform/GetCarPrice?SecretKey=ABCD&parkKey=c1648ccf33314dc384155896cf4d00b9&carNo="+carNumber);
		Object data=map.get("body");
		 Gson gson = new Gson();
		 Map<String, Object> mapdata=gson.fromJson((String) data, new TypeToken<Map<String, Object>>(){
         }.getType() );		
		 String code=(String) mapdata.get("Code");
		 if (!code.equals("200")) {
			return null;
		}
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
			hongxingrecord.setCouponamount((float) (double) resultData.get("payAmount"));
			int num=hongxingRecordService.insert(hongxingrecord);
			if (num==1) {
				resultData.put("recordId", hongxingrecord.getId());
			}
		return resultData;
	}

	@Override
	public String creatPayOrder(String orderNo) {
		// TODO Auto-generated method stub
		Map<String, Object> map=HttpUtil.get("http://139.196.19.162/ApiPlatform/GetCarPrice?SecretKey=ABCD&parkKey=c1648ccf33314dc384155896cf4d00b9&orderNo="+orderNo);
		Object data=map.get("body");
		 Gson gson = new Gson();
		 Map<String, Object> mapdata=gson.fromJson((String) data, new TypeToken<Map<String, Object>>(){
        }.getType() );		
		 Boolean status=(Boolean) mapdata.get("Success");
		 if (status!=true) {
			return null;
		}
		 String returnOrderNo=(String) mapdata.get("Code");
		return returnOrderNo;
	}

	@Override
	public Boolean payOrderNotify(String paidMoney, String OrderNo, String Order) {
		// TODO Auto-generated method stub
		Map<String, Object> map=HttpUtil.get("http://139.196.19.162/ApiPlatform/CarOrderPay?SecretKey=ABCD&parkKey=c1648ccf33314dc384155896cf4d00b9&payOrder="
				+ Order+"&payedSN="+OrderNo+"&payedMoney="+paidMoney);
		Object data=map.get("body");
		 Gson gson = new Gson();
		 Map<String, Object> mapdata=gson.fromJson((String) data, new TypeToken<Map<String, Object>>(){
       }.getType() );		
		 Boolean status=(Boolean) mapdata.get("Success");
		 if (status!=true) {
			return false;
		}
		return true;
	}

}

package com.park.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import redis.clients.jedis.Jedis;
@Service
public class InvoiceService {
	 Jedis jedis = new Jedis("106.15.231.85", 6390);
	
	public  InvoiceService() {
		jedis.auth("liushuijiwusi");
	}
	public String getTocken(){
		Map<String, Object> params=new HashMap<>();
		params.put("openid", "ceTSp96pAzxwTlmxy4c84CrooflGZsK3wHUOjMG2NzfX3SzWpvT1480038819783");
		params.put("app_secret","a8sm8zfFw9Wi9N392IOKCYISGRkaQMR5SlWSVRpasSfQShAA2G81499333176474");
		Map<String, Object> map=HttpUtil.post("http://open.hydzfp.com/open_access/access/token.open",params);
		Object data=map.get("body");
		 Gson gson = new Gson();
		 Map<String, Object> mapdata=gson.fromJson((String) data, new TypeToken<Map<String, Object>>(){
         }.getType() );
		 String result=(String)mapdata.get("result");
		 if (result.equals("SUCCESS")) {
			 Map<String, Object> mapdata2=(Map<String, Object>) mapdata.get("rows");
			 String accessToken=(String) mapdata2.get("access_token");
			 return accessToken;
		}
		 return null;
	}
}

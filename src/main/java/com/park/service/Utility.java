package com.park.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utility {

	public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	public static String createJsonMsg(Object status, Object message, Object body){
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("status", status);
		ret.put("message", message);
		ret.put("body", body);
		return gson.toJson(ret);
	}
	
	public static String createJsonMsg(Object status, Object message){
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("status", status);
		ret.put("message", message);
		return gson.toJson(ret);
	}
	
	public static Map<String, Object> post(String url, Map<String, Object> argMap){
		  
		  HttpClient httpClient = new DefaultHttpClient();
		  HttpPost post = new HttpPost(url);
		  		  
		  String json = new Gson().toJson(argMap);
		  StringEntity se = new StringEntity(json.toString(), "UTF-8"); 		  
		  se.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));
	      se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8"));
	      post.setEntity(se);
	      
	      Map<String, Object> resultMap = new HashMap<String, Object>();
	      try {
	          HttpResponse response = httpClient.execute(post);         
	          
	          StatusLine status = response.getStatusLine();	          	          
	          int code = status.getStatusCode();
	          resultMap.put("status", code);
	          if(code < 200 || code >=300){
	        	  resultMap.put("message", "request fail");
	          }else{
	        	  HttpEntity entity = response.getEntity();
	        	  String body = EntityUtils.toString(entity);
	        	  resultMap.put("body", body);
	        	  resultMap.put("message", "request success");
	          }                            
	                    
	      } catch (ClientProtocolException e) {
	          e.printStackTrace();
	          post.abort();
	      } catch (IOException e) {
	          e.printStackTrace();
	          post.abort();
	      } finally{
	    	  httpClient.getConnectionManager().shutdown();
	    	  
	      }
	      return resultMap;
	}
}

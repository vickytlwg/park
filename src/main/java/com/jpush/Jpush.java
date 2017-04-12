package com.jpush;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.api.push.model.notification.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.SMS;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import com.google.gson.JsonObject;
public class Jpush {
	protected static final Logger LOG = LoggerFactory.getLogger(PushExample.class);
	   // demo App defined in resources/jpush-api.conf 
		private static final String appKey ="ea4986f01e80bd6e4f9a14b8";
		private static final String masterSecret = "741a1bd8c4e429c7c7310235";
	public static void SendPushToAudiences(List<String> audiences,String message) {
	    // HttpProxy proxy = new HttpProxy("localhost", 3128);
	    // Can use this https proxy: https://github.com/Exa-Networks/exaproxy
		ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
          
        PushPayload payload = buildPushObject_android(audiences,message);
   //     PushPayload pushPayload=buil
        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);
            
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        }
	}
	
	public static void SendPushToAudiencesWithExtras(List<String> audiences,Map<String, String> extras,String message) {
		ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);          
        PushPayload payload = buildPushObject_android(audiences,extras,message);
        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);
            
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        }
	}
	
    public static PushPayload buildPushObject_android(List<String> audiences,String message){
    	
    	return PushPayload.newBuilder()
    			.setPlatform(Platform.android())
    			.setAudience(Audience.tag(audiences))
    			.setMessage(Message.newBuilder()
    					.setMsgContent(message)
    					.build()).build();
    };
public static PushPayload buildPushObject_android(List<String> audiences,Map<String, String> extras,String message){
    	
    	return PushPayload.newBuilder()
    			.setPlatform(Platform.android())
    			.setAudience(Audience.tag(audiences))
    			.setMessage(Message.newBuilder()
    					.setMsgContent(message)
    					.addExtras(extras)
    					.build()).build();
    };
}

package com.park.service;

import java.util.HashMap;
import java.util.Map;

public class ActiveMqService {

	public static void SendPosChargeData(String data){
		Map<String, Object> args=new HashMap<>();
		args.put("data", data);
		HttpUtil.post("http://120.25.159.154:8080/parkServer/mq/poschargeData",args);
	}
	public static void SendWithQueueName(String data,String queue){
		Map<String, Object> args=new HashMap<>();
		args.put("data", data);
		args.put("queue", queue);
		HttpUtil.post("http://120.25.159.154:8080/mq/queueData",args);
	}
}

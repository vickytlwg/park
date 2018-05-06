package com.park.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.park.controller.BarrierChargeController;

public class ActiveMqService {
	private static Log logger=LogFactory.getLog(ActiveMqService.class);
	public static void SendPosChargeData(String data){
		Map<String, Object> args=new HashMap<>();
		args.put("data", data);
		logger.info("发送队列:"+data);
		HttpUtil.post("http://120.25.159.154:8080/parkServer/mq/poschargeData",args);
	}
	public static void SendWithQueueName(String data,String queue){
		Map<String, Object> args=new HashMap<>();
		args.put("data", data);
		logger.info("发送队列:"+data);
		args.put("queue", queue);
		HttpUtil.post("http://120.25.159.154:8080/parkServer/mq/queueData",args);
	}
}

package com.park.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.Constants;
import com.park.service.Utility;
import com.park.tcp.TcpServerService;

import io.netty.channel.Channel;

@Controller
@RequestMapping("tcp")
public class TcpController {
	
	TcpServerService tcpServerService=new TcpServerService();
	@RequestMapping(value = "send", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String send(@RequestBody Map<String, Object> args) throws InterruptedException{
		int parkId=(int) args.get("parkId");		
		String content=(String) args.get("content");
		Boolean result=tcpServerService.sendByParkId(parkId, content);
		if (result) {						
			for (int i = 0; i < 5; i++) {
				Thread.sleep(1000);
				System.out.println(Constants.tcpReceiveDatas);
				if (Constants.tcpReceiveDatas.get(String.valueOf(parkId))!=null) {
					
				//	Constants.tcpReceiveDatas.remove(String.valueOf(parkId));
					return Utility.createJsonMsg(1001, "success",Constants.tcpReceiveDatas.get(String.valueOf(parkId)));
				}
			}
			return Utility.createJsonMsg(1001, "no received");
		}
		return  Utility.createJsonMsg(1002, "failed");
	}
	@RequestMapping(value = "start", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String start(){
		try {
			TcpServerService.StartTcpServer(8999);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Utility.createJsonMsg(1001, "ok");
	}
	@RequestMapping(value = "getConnections", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getConnections(){
		Map<String, Object> ret=new HashMap<>();
		Map<String, Channel> channelsMap=tcpServerService.getConnections();
		System.out.println(channelsMap);
		 Set<String> key = channelsMap.keySet();		 
         for (Iterator it = key.iterator(); it.hasNext();) {
        	 String s = (String) it.next();
        	 ret.put(s, channelsMap.get(s).remoteAddress()+"");
         }
         return Utility.createJsonMsg(1001, "success", ret);
	}
}

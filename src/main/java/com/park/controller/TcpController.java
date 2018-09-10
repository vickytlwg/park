package com.park.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
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
import com.park.service.ParkService;
import com.park.service.PosChargeDataService;
import com.park.service.Utility;
import com.park.tcp.TcpServerService;

import io.netty.channel.Channel;

@Controller
@RequestMapping("tcp")
public class TcpController {
	@Autowired 
	PosChargeDataService posChargeDataService;
	@Autowired
	ParkService parkService;
	TcpServerService tcpServerService=new TcpServerService();
	@RequestMapping(value = "send", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String send(@RequestBody Map<String, Object> args) throws InterruptedException{
		int parkId=(int) args.get("parkId");		
		String content=(String) args.get("content");
		Boolean result=tcpServerService.sendByParkId(parkId, content);
		if (result) {						
			for (int i = 0; i < 3; i++) {
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
		Constants.poschargeSerivce=posChargeDataService;
		Constants.parkService=parkService;
		try {
			TcpServerService.StartTcpServer(8099);
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
	@RequestMapping(value="tcpSendTest",method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public void tcptest(@RequestBody Map<String, Object> args) throws UnknownHostException, IOException{
		String content=(String) args.get("content");
		int parkId=(int) args.get("parkId");
//		 String server="192.168.44.1";            	          
//	     int servPort=8099;  
//	     Socket socket=new Socket(server,servPort);
//	     OutputStream os =  socket.getOutputStream();  
//         DataOutputStream bos = new DataOutputStream(os);    
//         bos.writeUTF(content+"\n");  
//         bos.flush();      
//
//	     socket.close(); 
		tcpServerService.sendByParkId(parkId, content);
	}
	
	
}

package com.park.tcp;

import java.util.Map;

import io.netty.channel.Channel;

public class TcpServerService {

	public static void StartTcpServer(int port) throws Exception{
		 new SimpleTcpServer(port).run();
	}
	
	public static Map<String, Channel> getConnections(){
		return SimpleTcpServerHandler.channelsMap;
	}
	
	public static void sendChannel(Channel ch,String content){
		ch.writeAndFlush(content+"\n");
	}
	public Boolean sendByParkId(int parkId,String content){
		Channel channel=SimpleTcpServerHandler.channelsMap.get(String.valueOf(parkId));
		if (channel==null) {
			return false;
		}
		channel.writeAndFlush(content+"\n");
		return true;
	}
}

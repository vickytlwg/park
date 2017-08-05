package com.park.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.park.model.Constants;
import com.park.model.Udpconnectors;
import com.park.service.UdpConnectorInfoService;

public class ServerThreadMuiti  implements Runnable {

	DatagramSocket socket=null;
	DatagramPacket packet=null;
	byte[] data=null;
	
	public ServerThreadMuiti(DatagramSocket socket,DatagramPacket packet,byte[] data)
	{
		this.socket=socket;
		this.packet=packet;
		this.data=data;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String info=new String(data,0,packet.getLength());		
		/*
		 * 给出响应
		 */
		//响应报文包含服务器的ip地址
		InetAddress address=packet.getAddress();
		int port=packet.getPort();
		Udpconnectors udpconnector=new Udpconnectors();
		udpconnector.setIp(address.getHostAddress());
		udpconnector.setPort(String.valueOf(port));
		udpconnector.setMac(info);
		udpconnector.setLastdate(new Date());
								
		if (Constants.udpconnectors.isEmpty()) {
			 Constants.udpconnectors.add(udpconnector);
		}
		else {
			Boolean isOld=false;
			for (Udpconnectors u : Constants.udpconnectors) {
				if (u.getIp().equals(udpconnector.getIp())&&u.getPort().equals(udpconnector.getPort())) {
					u.setLastdate(new Date());
					u.setMac(info);
					isOld=true;
				}
				if ((new Date().getTime()-u.getLastdate().getTime())>1000*60*5) {
					Constants.udpconnectors.remove(u);
				}
			}
			if (!isOld) {
				Constants.udpconnectors.add(udpconnector);
			}
		}
	
}}

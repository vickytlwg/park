package com.park.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

import com.park.model.Constants;
import com.park.model.Udpconnectors;

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
	//	System.out.println("我是服务器，客户端说："+info);
		
		/*
		 * 给出响应
		 */
		//响应报文包含服务器的ip地址
		InetAddress address=packet.getAddress();
		int port=packet.getPort();
		byte[] dataToClient="connected!".getBytes();
		Udpconnectors udpconnector=new Udpconnectors();
		udpconnector.setIp(address.getHostAddress());
		udpconnector.setPort(String.valueOf(port));
		udpconnector.setLastdate(new Date());
		if (Constants.udpconnectors.isEmpty()) {
			 Constants.udpconnectors.add(udpconnector);
		}
		else {
			Boolean isIn=false;
			for (Udpconnectors u : Constants.udpconnectors) {
				if (u.getIp().equals(udpconnector.getIp())&&u.getPort().equals(udpconnector.getPort())) {
					u.setLastdate(new Date());
					isIn=true;
				}
//				else {
//					 Constants.udpconnectors.add(udpconnector);
//				}
				if ((new Date().getTime()-u.getLastdate().getTime())>1000*60*60*2) {
					Constants.udpconnectors.remove(u);
				}
			}
			if (!isIn) {
				Constants.udpconnectors.add(udpconnector);
			}
		}
		
		DatagramPacket packetToClient=new DatagramPacket(dataToClient, dataToClient.length, address, port);
		try {
			socket.send(packetToClient);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}}

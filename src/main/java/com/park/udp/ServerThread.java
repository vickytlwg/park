package com.park.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

import com.park.model.Constants;
import com.park.model.Udpconnectors;

public class ServerThread implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub

		// 1.创建服务器端DatagramSocket,指定端口
	//	DatagramSocket socket = null;
		try {
			Constants.socket = new DatagramSocket(10086);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 2.创建数据报，用于接收客户端发送的数据
		byte[] data = new byte[1024];// 创建字节数组，指定接收的数据包的大小。

		while (true) {

			// 3.接收客户端发送的数据
			System.out.println("****服务器端已经启动，等待客户端发送信息");

			try {
				DatagramPacket packet = new DatagramPacket(data, data.length);
				Constants.socket.receive(packet);// 此方法在接收到数据报之前会一直阻塞
				// 4.读取数据
				String info = new String(data, 0, packet.getLength());
				System.out.println("我是服务器，客户端说：" + info + "IP地址:" + packet.getAddress());
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				Udpconnectors udpconnector=new Udpconnectors();
				udpconnector.setIp(address.getHostAddress());
				udpconnector.setPort(String.valueOf(port));
				udpconnector.setLastdate(new Date());
				if (Constants.udpconnectors.isEmpty()) {
					 Constants.udpconnectors.add(udpconnector);
				}
				else {
					for (Udpconnectors u : Constants.udpconnectors) {
						if (u.getIp().equals(udpconnector.getIp())&&u.getPort().equals(udpconnector.getPort())) {
							u.setLastdate(new Date());
						}
						else {
							 Constants.udpconnectors.add(udpconnector);
						}
						if ((new Date().getTime()-u.getLastdate().getTime())>1000*60*60*2) {
							Constants.udpconnectors.remove(u);
						}
					}
				}
				
			//	byte[] data2 = "connected!".getBytes();
				// 把数据发送给客户端
			//	DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address, port);
			//	System.out.println("客户端口:"+port);
			//	Constants.socket.send(packet2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

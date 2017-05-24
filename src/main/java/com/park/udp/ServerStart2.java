package com.park.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.park.model.Constants;

public class ServerStart2 implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(10087);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("服务器已经启动，等待客户端发送数据。。。。");
		byte[] data=new byte[1024];
	//	int count=0;
		while(true)
		{
			try {
				//创建接收数据报
				DatagramPacket packet=new DatagramPacket(data, data.length);
				socket.receive(packet);
		//		count++;
			//	System.out.println("当前连接数："+count);
				ServerThreadMuiti2 server2=new ServerThreadMuiti2(Constants.socket,packet,data);
				Thread t=new Thread(server2);
				t.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //在接收到数据报之前会一直阻塞
		}
	}

}

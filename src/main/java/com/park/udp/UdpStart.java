package com.park.udp;

import java.io.Console;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class UdpStart implements ServletContextListener {
	private static Log logger = LogFactory.getLog(UdpStart.class);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("已经进入监听");
		logger.info("已经进入监听");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("已经进入监听");
		
	ServerStart thread=new ServerStart();
	new Thread(thread).start();
	
		
	}
	
}

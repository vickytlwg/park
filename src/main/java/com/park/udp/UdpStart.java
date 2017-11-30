package com.park.udp;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class UdpStart implements ServletContextListener {
//	private static Log logger = LogFactory.getLog(UdpStart.class);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("已经离开监听");

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("已经进入监听");
		
	ServerStart thread=new ServerStart();
	new Thread(thread).start();
	
//	ServerStart2 thread2=new ServerStart2();
//	new Thread(thread2).start();
	}
	
}

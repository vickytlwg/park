package com.park.tcp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TcpStart implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		try {
			TcpServerService.StartTcpServer(8999);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

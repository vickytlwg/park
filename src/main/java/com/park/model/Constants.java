package com.park.model;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.springframework.asm.commons.StaticInitMerger;

import com.park.service.UdpConnectorInfoService;
import com.park.service.impl.UdpConnectorsInfoServiceImpl;

public class  Constants{
	
	public static  String PINGAPIKEY = "sk_live_Wr104O5Gu5m508arbTmX9aL8";
	public static  String PINGAPPID = "app_bX1SKKWbP4K0Ge1i";
	public static  String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	public static  int ACCESSTABLESCOUNT = 500;
	public static  String URL = "http://www.iotclouddashboard.com/parkpictures/";
	public static  String UPLOADDIR = "/alidata/server/tomcat7/webapps/parkpictures/";
	public static  String WEBAPIURL="http://www.iotclouddashboard.com/park";
	public static  Set<Udpconnectors>  udpconnectors=new HashSet<>();
	public static  DatagramSocket socket=null;
	public static StringBuilder  dataReceived=new StringBuilder();
	public static  Map<String, Object> iotData=new HashMap<>();
	public static Map<String, Object> tcpReceiveDatas=new HashMap<>();
	public static UdpConnectorInfoService udpConnectorInfoService=new UdpConnectorsInfoServiceImpl();
	
}

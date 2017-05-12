package com.park.model;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import org.springframework.asm.commons.StaticInitMerger;

public class  Constants{
	
	public static  String PINGAPIKEY = "sk_test_nbDy94HOWXT8GmjXnLfX18eD";
	public static  String PINGAPPID = "app_K8aH0444i5G8qvTO";
	public static  String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	public static  int ACCESSTABLESCOUNT = 500;
	public static  String URL = "http://www.iotclouddashboard.com/parkpictures/";
	public static  String UPLOADDIR = "/alidata/server/tomcat7/webapps/parkpictures/";
	public static  String WEBAPIURL="http://www.iotclouddashboard.com/park";
	public static  List<Udpconnectors>  udpconnectors=new ArrayList<>();
	public static  DatagramSocket socket=null;
}

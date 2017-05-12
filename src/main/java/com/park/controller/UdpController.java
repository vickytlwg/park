package com.park.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.formula.functions.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.AuthUser;
import com.park.model.AuthUserRole;
import com.park.model.Constants;
import com.park.model.Page;
import com.park.model.Udpconnectors;
import com.park.service.AuthorityService;
import com.park.service.UserPagePermissionService;
import com.park.service.Utility;
@RequestMapping("udp")
@Controller
public class UdpController {

	@Autowired
	private AuthorityService authService;
	
	@Autowired
	private UserPagePermissionService pageService;
	
	@RequestMapping(value="/",produces={"application/json;charset=UTF-8"})
	public String Index(ModelMap modelMap, HttpServletRequest request, HttpSession session){
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		if(user != null){
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if(user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin=true;
			modelMap.addAttribute("isAdmin", isAdmin);
			
			Set<Page> pages = pageService.getUserPage(user.getId()); 
			for(Page page : pages){
				modelMap.addAttribute(page.getPageKey(), true);
			}
		}		
		return "udpTest";
	}
	
@RequestMapping(value="send",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
@ResponseBody
public String send(@RequestBody Map<String, String> args) throws IOException, InterruptedException{
	String message=args.get("message");
	byte[] msToSend=message.getBytes();
	List<Udpconnectors> udpconnectors=Constants.udpconnectors;
	for (Udpconnectors udpconnector : Constants.udpconnectors) {
		InetAddress address = InetAddress.getByName(udpconnector.getIp());
		int port=Integer.parseInt(udpconnector.getPort());
		System.out.println(address+"::"+port);
		DatagramPacket packet2 = new DatagramPacket(msToSend, msToSend.length, address, port);
		Constants.socket.send(packet2);
		Thread.sleep(1000);
	}
	return Utility.createJsonMsg(1001, "ok");
}
@RequestMapping(value="getConnectors",method=RequestMethod.GET,produces={"application/json;charset=UTF-8"})
@ResponseBody
public String getConnectors() throws IOException, InterruptedException{
	
	return Utility.createJsonMsg(1001, "ok", Constants.udpconnectors);
}

}

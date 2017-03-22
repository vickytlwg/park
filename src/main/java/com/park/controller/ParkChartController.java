package com.park.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.park.model.AuthUser;
import com.park.model.AuthUserRole;
import com.park.model.Page;
import com.park.model.Park;
import com.park.service.AccessService;
import com.park.service.AuthorityService;
import com.park.service.ParkService;
import com.park.service.UserPagePermissionService;


@Controller
public class ParkChartController {
	
	@Autowired
	private ParkService parkService;
	
	@Autowired
	private AuthorityService authService;
	
	@Autowired
	private UserPagePermissionService pageService;
	
	@Autowired
	private AccessService accessService;
	
	@RequestMapping(value = "/chart", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String parkChart(ModelMap modelMap, HttpServletRequest request, HttpSession session){		
		List<Park> parkList = parkService.getParks();
		String username = (String) session.getAttribute("username");
		if(username != null)
			parkList = parkService.filterPark(parkList, username);
		List<Park> parkl = new ArrayList<>();
		SimpleDateFormat sFormat=new SimpleDateFormat("yyyy-MM-dd");
		String date=sFormat.format(new Date())+" 00:00:00";
		for (Park park : parkList) {
			if (park.getType()!=3) {
				int count=accessService.getAccessCountToday(park.getId(), date);
				if (count>0) {
					parkl.add(park);
				}
				
			}
		}
		modelMap.addAttribute("parks", parkl);
		
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
		
		
		return "parkChart";
	}
	@RequestMapping(value="/flow",method = RequestMethod.GET,produces={"application/json;charset=UTF-8"})
	public String parkFlow(ModelMap modelMap, HttpServletRequest request, HttpSession session){
		List<Park> parkList = parkService.getParks();
		
		String username = (String) session.getAttribute("username");
		if(username != null)
		parkList = parkService.filterPark(parkList, username);
		modelMap.addAttribute("parks", parkList);
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
		
		return "flow";
	}
}

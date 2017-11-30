package com.park.controller;

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
import com.park.service.AuthorityService;
import com.park.service.UserPagePermissionService;

@RequestMapping("alipayinfo")
@Controller
public class AlipayInfoController {
	
	@Autowired
	private AuthorityService authService;
	@Autowired
	private UserPagePermissionService pageService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8" })
	public String index(ModelMap modelMap, HttpServletRequest request, HttpSession session){
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
		return "alipayinfo";
	}
}

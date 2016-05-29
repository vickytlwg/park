package com.park.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.park.model.AuthUser;
import com.park.model.AuthUserRole;
import com.park.service.AuthorityService;

@Controller
public class IndexController {

	@Autowired
	private AuthorityService authService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String accessIndex1(){
		
		return "login";
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String accessIndex2(){
		
		return "login";
	}
	@RequestMapping(value = ".", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String accessIndex3(){
		
		return "login";
	}
	@RequestMapping(value = "authority", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public String authority(ModelMap modelMap, @RequestParam("username") String username,@RequestParam("password") String password,HttpSession session){
		if(authService.checkUserAccess(username, password)){
			session.setAttribute("username", username);
			AuthUser user = authService.getUserByUsername(username);
			if(user != null){
				modelMap.addAttribute("user", user);
				boolean isAdmin = false;
				if(user.getRole() == AuthUserRole.ADMIN.getValue())
					isAdmin=true;
				modelMap.addAttribute("isAdmin", isAdmin);
			}
			return "redirect:parks";
		}else{
			return "redirect:login";
		}
		
	}
	@RequestMapping(value = "login", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String login(){
		
		return "login";
	}
	
	@RequestMapping(value = "demo", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String demo(){
		
		return "demo";
	}
	
	@RequestMapping(value = "uploadTest", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String uploadTest(){
		
		return "uploadTest";
	}
}

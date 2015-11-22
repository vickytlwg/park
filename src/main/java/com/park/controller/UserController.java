package com.park.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.AuthUser;
import com.park.model.AuthUserRole;
import com.park.model.User;
import com.park.model.UserDetail;
import com.park.service.AuthorityService;
import com.park.service.UserService;
import com.park.service.Utility;


@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorityService authService;
	
	private static Log logger = LogFactory.getLog(UserController.class);
	
	@RequestMapping(value = "/users", produces = {"application/json;charset=UTF-8"})
	public String getUsers(ModelMap modelMap, HttpServletRequest request, HttpSession session){
		List<User> userList = userService.getUsers();
		modelMap.put("users", userList);
		logger.info("info test");		
		
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		boolean isAdmin = false;
		if(user != null){
			modelMap.addAttribute("user", user);
			if(user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin=true;
			modelMap.addAttribute("isAdmin", isAdmin);
		}
		
		if(isAdmin)
			return "user";
		else
			return "/login";
		
	}
	
	
	@RequestMapping(value = "/getUserCount", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getuserCount(){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> body = new HashMap<String, Object>();
		int count = userService.getUserCount();
		body.put("count", count);
	
		ret.put("status", "1001");
		ret.put("message", "get user detail success");
		ret.put("body", Utility.gson.toJson(body));
		
		return Utility.gson.toJson(ret);					
	}
	
	@RequestMapping(value = "/getUserDetail", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String accessIndex(@RequestParam("low")int low, @RequestParam("count")int count){	
		Map<String, Object> ret = new HashMap<String, Object>();
		List<UserDetail> userDetail = userService.getUserDetail(low, count);
		if(userDetail != null){
			ret.put("status", "1001");
			ret.put("message", "get user detail success");
			ret.put("body", Utility.gson.toJson(userDetail));
		}else{
			ret.put("status", "1002");
			ret.put("message", "get user detail fail");
		}
		return Utility.gson.toJson(ret);
		
	}
	@RequestMapping(value = "/insert/user", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertUser(@RequestBody User user){
		logger.info("userName: " + user.getUserName() + " number: " + user.getNumber() + " passwd: " + user.getPasswd());
		return userService.insertUser(user);
	}
	
	
	@RequestMapping(value = "/user/passwd", method = RequestMethod.POST)
	@ResponseBody
	public String getUserPasswd(@RequestParam("userName")String userName){
		return userService.getUserPassword(userName);
	}
	
	@RequestMapping(value = "/register/user")
	public String registerUser(User user){
		return "registerUser";
	}
	
}

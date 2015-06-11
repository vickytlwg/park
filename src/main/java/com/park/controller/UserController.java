package com.park.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.park.model.User;
import com.park.service.UserService;


@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	private static Log logger = LogFactory.getLog(UserController.class);
	
	@RequestMapping(value = "/users", produces = {"application/json;charset=UTF-8"})
	public String getUsers(ModelMap modelMap, HttpServletRequest request){
		List<User> userList = userService.getUsers();
		modelMap.put("users", userList);
		logger.info("info test");				
		return "userList";
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
	
	
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public  String test(@RequestBody Map<String, Object> map){
		logger.info("Id:" + map.get("Id"));
		return "registerUser";
	}
}

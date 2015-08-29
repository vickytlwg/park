package com.park.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.logging.log4j2.Log4j2AbstractLoggerImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

	
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String accessIndex1(){
		
		return "access";
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String accessIndex2(){
		
		return "access";
	}
	@RequestMapping(value = ".", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String accessIndex3(){
		
		return "access";
	}
	@RequestMapping(value = "loginA", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public String accessIndex4(@RequestParam("username") String username,@RequestParam("password") String password,HttpSession session){
		Log logger = LogFactory.getLog(UserController.class);
		logger.info(username);
		logger.info(password);
		logger.info(password=="admin");
		if (username.equals("admin")&&password.equals("admin")) {
			session.setAttribute("username", "admin");
			return "access";
		}
		return "login";
	}
	@RequestMapping(value = "login", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String accessIndex5(){
		
		return "login";
	}
}

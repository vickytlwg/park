package com.park.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
}

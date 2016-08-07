package com.park.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.AuthUser;
import com.park.model.AuthUserRole;
import com.park.model.FeeCriterion;
import com.park.service.AuthorityService;
import com.park.service.FeeCriterionService;
import com.park.service.Utility;

@Controller
@RequestMapping("/fee/criterion")
public class FeeCriterionController {

	@Autowired
	private FeeCriterionService criterionService;

	@Autowired
	private AuthorityService authService;
	
	@RequestMapping(value = "/index", produces = {"application/json;charset=UTF-8"})
	public String apiUserIndex(ModelMap modelMap, HttpServletRequest request, HttpSession session){
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		if(user != null){
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if(user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin=true;
			modelMap.addAttribute("isAdmin", isAdmin);
		}
		return "feeCriterion";		
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String get(){
		
		List<FeeCriterion> criterions = criterionService.get();
		return Utility.createJsonMsg(1001, "success", criterions);
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String insert(@RequestBody FeeCriterion criterion){
		
		int ret = criterionService.insert(criterion);
		if(ret == 1)
			return Utility.createJsonMsg(1001, "success");
		else
			return Utility.createJsonMsg(1002, "failed");
	}
	
	@RequestMapping(value = "/modify",method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String modify(@RequestBody FeeCriterion criterion){
		
		int ret = criterionService.modify(criterion);
		if(ret == 1)
			return Utility.createJsonMsg(1001, "success");
		else
			return Utility.createJsonMsg(1002, "failed");
	
	}
	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String insert(@PathVariable int id){
		int ret = criterionService.delete(id);
		if(ret == 1)
			return Utility.createJsonMsg(1001, "success");
		else
			return Utility.createJsonMsg(1002, "failed");
	}
	

}
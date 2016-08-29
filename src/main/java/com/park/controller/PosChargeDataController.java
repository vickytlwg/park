package com.park.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.AuthUser;
import com.park.model.AuthUserRole;
import com.park.model.FeeCriterion;
import com.park.model.Page;
import com.park.model.Park;
import com.park.model.PosChargeData;
import com.park.service.AuthorityService;
import com.park.service.ParkService;
import com.park.service.PosChargeDataService;
import com.park.service.UserPagePermissionService;
import com.park.service.Utility;

@Controller
@RequestMapping("/pos/charge")
public class PosChargeDataController {
	
	@Autowired
	ParkService parkService;
	
	@Autowired
	private AuthorityService authService;
	
	@Autowired
	PosChargeDataService chargeSerivce;
	
	@Autowired
	private UserPagePermissionService pageService;
	
	@RequestMapping(value = "/detail", produces = {"application/json;charset=UTF-8"})
	public String feeDetailIndex(ModelMap modelMap, HttpServletRequest request, HttpSession session){
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
		return "feeDetail";		
	}
	
	
	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String count(){
		
		int count = chargeSerivce.count();
		return Utility.createJsonMsg(1001, "success", count);
	}
	
	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String page(@RequestBody Map<String, Object> args){
		int low = (int) args.get("low");
		int count = (int) args.get("count");
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getPage(low, count));
	}
	
	
	@RequestMapping(value = "/get", method = {RequestMethod.GET,RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String get(@RequestParam(value="cardNumber",required=false)String cardNumber){
		List<PosChargeData> charges =null;
		if (cardNumber!=null) {
			try {
				charges=chargeSerivce.getDebt(cardNumber);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, "请先绑定计费标准到停车场");
			}
		}
		else{
			 charges = chargeSerivce.getUnCompleted();
		}
		
		return Utility.createJsonMsg(1001, "success", charges);
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String insert(@RequestBody PosChargeData charge){
		
		int parkId = charge.getParkId();
		Park park = parkService.getParkById(parkId);
		
		if(park == null || park.getFeeCriterionId() == null){
			return Utility.createJsonMsg(1002, "请先绑定计费标准到停车场");
		}
		
		charge.setEntranceDate(new Date());
		int ret = chargeSerivce.insert(charge);
		if(ret == 1)
			return Utility.createJsonMsg(1001, "success");
		else
			return Utility.createJsonMsg(1002, "failed");
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String modify(@RequestBody PosChargeData charge){
		
		int ret = chargeSerivce.update(charge);
		if(ret == 1)
			return Utility.createJsonMsg(1001, "success");
		else
			return Utility.createJsonMsg(1002, "failed");
	
	}
	
	
	@RequestMapping(value = "/unpaid", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String getDebt(@RequestBody Map<String, Object> args){
		String cardNumber = (String) args.get("cardNumber");
		List<PosChargeData> unpaidCharges = null;
		try {
			unpaidCharges = chargeSerivce.getDebt(cardNumber);
		} catch (Exception e) {
			return Utility.createJsonMsg(1002, "请先绑定停车场计费标准" );
		}
		
		return Utility.createJsonMsg(1001, "success", unpaidCharges);
	}
	
	@RequestMapping(value = "/pay", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String pay(@RequestBody Map<String, Object> args){
		String cardNumber = (String) args.get("cardNumber");
		double money = (double) args.get("money");
		
		List<PosChargeData> payRet =  null;
		try {
			payRet = chargeSerivce.pay(cardNumber, money);
		} catch (Exception e) {
			return Utility.createJsonMsg(1002, "请先绑定停车场计费标准" );
		}
		
		return Utility.createJsonMsg(1001, "success", payRet);
	}
	

}

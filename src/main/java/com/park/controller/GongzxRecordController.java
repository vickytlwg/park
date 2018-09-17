package com.park.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.AuthUser;
import com.park.model.AuthUserRole;
import com.park.model.GongzxRecord;
import com.park.model.Page;
import com.park.model.Park;
import com.park.model.PosChargeData;
import com.park.service.AuthorityService;
import com.park.service.GongzxRecordService;
import com.park.service.ParkService;
import com.park.service.PosdataService;
import com.park.service.UserPagePermissionService;
import com.park.service.Utility;

@Controller
@RequestMapping("/gongzx")
public class GongzxRecordController {
	@Autowired
	private GongzxRecordService gongzxRecordService;
	@Autowired
	private AuthorityService authService;
	@Autowired
	ParkService parkService;
	@Autowired
	private UserPagePermissionService pageService;
	@Autowired
	private PosdataService posdataService;
	
	@RequestMapping(value = "/record2", produces = { "application/json;charset=UTF-8" })
	public String record2(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Park> parkList = parkService.getParks();
		if (username != null)
			parkList = parkService.filterPark(parkList, username);
		List<Park> outsideparks = new ArrayList<>();
		for (Park park : parkList) {
			if (park.getType() == 3) {
				outsideparks.add(park);
			}
		}
		modelMap.addAttribute("parks", outsideparks);
		if (user != null) {
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if (user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin = true;
			modelMap.addAttribute("isAdmin", isAdmin);

			Set<Page> pages = pageService.getUserPage(user.getId());
			for (Page page : pages) {
				modelMap.addAttribute(page.getPageKey(), true);
			}
		}
		return "record2";
	}
	
	@RequestMapping(value = "/gongCount", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String gongCount() {
		int gongcount = gongzxRecordService.gongcount();
		return Utility.createJsonMsg(1001, "success", gongcount);
	}
	
	@RequestMapping(value = "/gongpage", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String gongpage(@RequestBody Map<String, Object> args, HttpSession session) {
		@SuppressWarnings("unused")
		int low = (int) args.get("low");
		@SuppressWarnings("unused")
		int count = (int) args.get("count");
		String userName = (String) session.getAttribute("username");
		List<GongzxRecord> list=gongzxRecordService.getByParkAuthority(userName);
		return Utility.createJsonMsg(1001, "success", gongzxRecordService.getByParkAuthority(userName));
	}
	
	@RequestMapping(value = "/pageByParkId", method = RequestMethod.POST, produces = {
	"application/json;charset=UTF-8" })
	public @ResponseBody String pageByParkId(@RequestBody Map<String, Object> args, HttpSession session) {
		int parkId = (int) args.get("parkId");
		int start = (int) args.get("start");
		int count = (int) args.get("count");
		List<GongzxRecord> gongzxRecord = gongzxRecordService.getPageByParkId(parkId, start, count);
		if (gongzxRecord.isEmpty()) {
			return Utility.createJsonMsg(1001, "success",
					posdataService.selectPosdataByPageAndPark(parkId, start, count));
		}
		return Utility.createJsonMsg(1001, "success", gongzxRecord);
	}
	
	@RequestMapping(value = "getByCarnumberAuthority", method = RequestMethod.POST, produces = {
	"application/json;charset=UTF-8" })
	@ResponseBody
	public String getByCarnumberAuthority(@RequestBody Map<String, String> args, HttpSession session) {
		String carNumber = args.get("carNumber");
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Park> parkList = parkService.getParks();
		if (username == null)
			return null;
		if (user.getRole() == AuthUserRole.ADMIN.getValue()) {
			return Utility.createJsonMsg(1001, "success", gongzxRecordService.getByCarNumber(carNumber));
		}
		parkList = parkService.filterPark(parkList, username);
		List<GongzxRecord> gongzxRecord = new ArrayList<>();
		for (Park park : parkList) {
			gongzxRecord.addAll(gongzxRecordService.getByCarNumberAndPark(carNumber, park.getId()));
		}
		return Utility.createJsonMsg(1001, "success", gongzxRecord);
	}
	
	@RequestMapping(value = "getByParkName", method = RequestMethod.POST, produces = {
	"application/json;charset=UTF-8" })
	@ResponseBody
	public String getByParkName(@RequestBody Map<String, String> args) {
		String parkName = args.get("parkName");
		return Utility.createJsonMsg(1001, "success", gongzxRecordService.getByParkName(parkName));
	}
}

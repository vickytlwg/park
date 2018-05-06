package com.park.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.jpush.Jpush;
import com.park.dao.CarportStatusDetailDAO;
import com.park.model.AuthUser;
import com.park.model.AuthUserRole;
import com.park.model.CarportStatusDetail;
import com.park.model.Constants;
import com.park.model.FeeCriterion;
import com.park.model.Feeoperator;
import com.park.model.Monthuser;
import com.park.model.Outsideparkinfo;
import com.park.model.Page;
import com.park.model.Park;
import com.park.model.Parktoalipark;
import com.park.model.Pos;
import com.park.model.PosChargeData;
import com.park.model.PosChargeDataSimple;
import com.park.model.Posdata;
import com.park.service.AliParkFeeService;
import com.park.service.AuthorityService;
import com.park.service.ExcelExportService;
import com.park.service.FeeCriterionService;
import com.park.service.FeeOperatorService;
import com.park.service.MonthUserService;
import com.park.service.OutsideParkInfoService;
import com.park.service.ParkService;
import com.park.service.ParkToAliparkService;
import com.park.service.PosChargeDataService;
import com.park.service.PosService;
import com.park.service.PosdataService;
import com.park.service.UserPagePermissionService;
import com.park.service.Utility;
import com.squareup.okhttp.Request;


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

	@Autowired
	private PosService posService;
	@Autowired
	private PosdataService posdataService;
	@Autowired
	private ExcelExportService excelService;
	@Autowired
	MonthUserService monthUserService;
	@Autowired
	private FeeOperatorService feeOperatorService;

	@Autowired
	private FeeCriterionService feeCriterionService;
	@Autowired
	AliParkFeeService aliparkFeeService;
	@Autowired
	ParkToAliparkService parkToAliparkService;

	@RequestMapping(value = "/detail", produces = { "application/json;charset=UTF-8" })
	public String feeDetailIndex(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
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
		return "feeDetail";
	}

	@RequestMapping(value = "/record", produces = { "application/json;charset=UTF-8" })
	public String record(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
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
		return "record";
	}

	@RequestMapping(value = "/taopaiche", produces = { "application/json;charset=UTF-8" })
	public String taopaiche(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
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
		return "taopaiche";
	}

	@RequestMapping(value = "/flowbill", produces = { "application/json;charset=UTF-8" })
	public String flowbill(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
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
		return "flowbill";
	}

	@RequestMapping(value = "/reconciliation", produces = { "application/json;charset=UTF-8" })
	public String reconciliation(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Park> parkList = parkService.getParks();
		if (username != null)
			parkList = parkService.filterPark(parkList, username);
		// List<Park> outsideparks = new ArrayList<>();
//		List<Park> outsideparks = new ArrayList<>();
//		for (Park park : parkList) {
//			if (park.getType() == 3) {
//				outsideparks.add(park);
//			}
//		}
//		modelMap.addAttribute("parks", outsideparks);
		modelMap.addAttribute("parks", parkList);
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
		return "reconciliation";
	}

	@RequestMapping(value = "/feeoperatorCharge", produces = { "application/json;charset=UTF-8" })
	public String feeoperatorCharge(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
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
		return "feeOperatorChargeData";
	}

	@RequestMapping(value = "/arrearage", produces = { "application/json;charset=UTF-8" })
	public String arrearage(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
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
		return "arrearage";
	}

	@RequestMapping(value = "/getByParkAndRange", method = RequestMethod.POST, produces = {
			"application/json;charset=utf-8" })
	@ResponseBody
	public String getByParkAndRange(@RequestBody Map<String, Object> args) {
		int parkId = Integer.parseInt((String) args.get("parkId"));
		String startDay = (String) args.get("startDay");
		String endDay = (String) args.get("endDay");
		Map<String, Object> retMap = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(startDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date parsedEndDay = null;
		try {
			parsedEndDay = sdf.parse(endDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<PosChargeData> posChargeDatas = chargeSerivce.selectPosdataByParkAndRange(parsedStartDay, parsedEndDay,
				parkId);
		if (posChargeDatas.isEmpty()) {
			retMap.put("status", 1002);
		} else {
			retMap.put("status", 1001);
			retMap.put("message", "success");
			retMap.put("body", posChargeDatas);
		}
		return Utility.gson.toJson(retMap);
	}
	
	@RequestMapping(value="/getParkChargeByRange",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getParkChargeByRange(@RequestBody Map<String, Object> args){
		int parkId=Integer.parseInt((String)args.get("parkId"));
		String startDay=(String)args.get("startDay");
		String endDay=(String)args.get("endDay");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(startDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		Date parsedEndDay  = null;
		try {
			parsedEndDay = sdf.parse(endDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		
		Calendar start =Calendar.getInstance(); 
		start.setTime(parsedStartDay);
		Long startTime = start.getTimeInMillis();
		Calendar end = Calendar.getInstance();
		end.setTime(parsedEndDay);
		Long endTime = end.getTimeInMillis();
		Long oneDay = 1000 * 60 * 60 * 24l;
		Long time = startTime;  
		Map<Long, Object> comparemap=new TreeMap<>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		  while (time <= endTime) {  
		        Date d = new Date(time);            
		        Map<String, Object> posChargeDatas = chargeSerivce.getParkChargeCountByDay(parkId, df.format(d));
		        comparemap.put(d.getTime(), posChargeDatas);
		        time += oneDay;  
		    }     
		return  Utility.gson.toJson(comparemap);
	}

	@RequestMapping(value = "getByCardnumber", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getByCardnumber(@RequestBody Map<String, String> args) {
		String cardNumber = args.get("cardNumber");
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getByCardNumber(cardNumber));
	}
	@RequestMapping(value = "getByCardnumberAuthority", method = RequestMethod.POST, produces = {
	"application/json;charset=UTF-8" })
@ResponseBody
public String getByCardnumberAuthority(@RequestBody Map<String, String> args, HttpSession session) {
String cardNumber = args.get("cardNumber");
String username = (String) session.getAttribute("username");
AuthUser user = authService.getUserByUsername(username);
List<Park> parkList = parkService.getParks();
if (username == null)
	return null;	
if (user.getRole() == AuthUserRole.ADMIN.getValue())
{
	return Utility.createJsonMsg(1001, "success", chargeSerivce.getByCardNumber(cardNumber));
}
	parkList = parkService.filterPark(parkList, username);
	List<PosChargeData> posChargeDatas=new ArrayList<>();
	for (Park park : parkList) {
		posChargeDatas.addAll(chargeSerivce.getByCardNumberAndPark(cardNumber, park.getId()));
	}
return Utility.createJsonMsg(1001, "success", posChargeDatas);
}
	@RequestMapping(value = "getByCardnumberAndPort", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getByCardnumberAndPort(@RequestBody Map<String, Object> args) {
		String cardNumber = (String) args.get("cardNumber");
		Integer portNumber = (Integer) args.get("portNumber");		
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getByCardNumberAndPort(cardNumber, portNumber).get(0));
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String count() {
		int count = chargeSerivce.count();
		return Utility.createJsonMsg(1001, "success", count);
	}

	@RequestMapping(value = "/getById", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getById(@RequestBody Map<String, Integer> args) {
		Integer id = args.get("id");
		PosChargeData posChargeDatas = chargeSerivce.getById(id);
		return Utility.createJsonMsg(1001, "success", posChargeDatas);
	}

	@RequestMapping(value = "getByParkName", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getByParkName(@RequestBody Map<String, String> args) {
		String parkName = args.get("parkName");
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getByParkName(parkName));
	}

	@RequestMapping(value = "deleteByParkIdAndDate", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String deleteByParkIdAndDate(@RequestBody Map<String, String> args) {
		int parkId = Integer.parseInt(args.get("parkId"));
		String startDate = args.get("startDate");
		String endDate = args.get("endDate");
		Integer count = chargeSerivce.deleteByParkIdAndDate(parkId, startDate, endDate);
		Map<String, Object> result = new HashMap<>();
		result.put("count", count);
		return Utility.createJsonMsg(1001, "success", result);
	}

	@RequestMapping(value = "deleleById", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String deleleById(@RequestBody Map<String, String> args) {
		int id = Integer.parseInt(args.get("id"));
		Integer count = chargeSerivce.deleteById(id);
		Map<String, Object> result = new HashMap<>();
		result.put("count", count);
		return Utility.createJsonMsg(1001, "success", result);
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String page(@RequestBody Map<String, Object> args, HttpSession session) {
		int low = (int) args.get("low");
		int count = (int) args.get("count");
		String userName = (String) session.getAttribute("username");
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getByParkAuthority(userName));
	}

	@RequestMapping(value = "/getByCount", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getByCount(@RequestBody Map<String, Object> args) {
		int low = (int) args.get("low");
		int count = (int) args.get("count");
		List<PosChargeData> posChargeDatas = chargeSerivce.getPage(low, count);
		return Utility.createJsonMsg(1001, "success", posChargeDatas);
	}

	@RequestMapping(value = "/pageByParkId", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String pageByParkId(@RequestBody Map<String, Object> args, HttpSession session) {
		// String username = (String) session.getAttribute("username");
		int parkId = (int) args.get("parkId");
		int start = (int) args.get("start");
		int count = (int) args.get("count");
		List<PosChargeData> posChargeDatas = chargeSerivce.getPageByParkId(parkId, start, count);
		if (posChargeDatas.isEmpty()) {
			return Utility.createJsonMsg(1001, "success",
					posdataService.selectPosdataByPageAndPark(parkId, start, count));
		}
		return Utility.createJsonMsg(1001, "success", posChargeDatas);
	}

	@RequestMapping(value = "/pageArrearage", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String pageArrearage(@RequestBody Map<String, Object> args) {
		int low = (int) args.get("low");
		int count = (int) args.get("count");
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getPageArrearage(low, count));
	}

	@RequestMapping(value = "/getParkCarportStatusToday", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String getParkCarportStatusToday(@RequestBody Map<String, Object> args) {
		int parkId = (int) args.get("parkId");
		List<PosChargeData> resultData = new ArrayList<>();
		Date tmpdate = new Date(new Date().getTime() - 1000 * 60 * 60 * 24 * 5);
		List<PosChargeData> posChargeDatas = chargeSerivce.getParkCarportStatusToday(parkId, tmpdate);
		for (PosChargeData posChargeData : posChargeDatas) {
			// if (posChargeData.getExitDate()==null) {
			resultData.add(posChargeData);
			// }
		}
		return Utility.createJsonMsg(1001, "success", resultData);
	}

	@RequestMapping(value = "/getParkCarportStatusTodaySimple", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String getParkCarportStatusTodaySimple(@RequestBody Map<String, Object> args) {
		int parkId = (int) args.get("parkId");
		Date tmpdate = new Date(new Date().getTime() - 1000 * 60 * 60 * 24 * 365);
		List<PosChargeData> posChargeDatas = chargeSerivce.getParkCarportStatusToday(parkId, tmpdate);
		List<PosChargeDataSimple> posChargeDataSimples = new ArrayList<>();
		int num = 1;
		for (PosChargeData posChargeData : posChargeDatas) {
			if (num > 10) {
				break;
			}
			DecimalFormat    df   = new DecimalFormat("######0.00");   
			if (posChargeData.isPaidCompleted() == false && posChargeData.getExitDate() != null) {
				num++;
				PosChargeDataSimple posChargeDataSimple = new PosChargeDataSimple();
				posChargeDataSimple.setId(posChargeData.getId());
				posChargeDataSimple.setCardNumber(posChargeData.getCardNumber());
				posChargeDataSimple.setUnPaidMoney(Double.parseDouble(df.format(posChargeData.getUnPaidMoney())));
				posChargeDataSimple.setExitDate(posChargeData.getExitDate());
				posChargeDataSimples.add(posChargeDataSimple);
			}

		}
		return Utility.createJsonMsg(1001, "success", posChargeDataSimples);
	}

	@RequestMapping(value = "/pageArrearageByParkId", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String pageArrearageByParkId(@RequestBody Map<String, Object> args) {
		int parkId = (int) args.get("parkId");
		int start = (int) args.get("start");
		int count = (int) args.get("count");
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getPageArrearageByParkId(parkId, start, count));
	}

	@RequestMapping(value = "/get", method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String get(@RequestParam(value = "cardNumber", required = false) String cardNumber) {
		List<PosChargeData> charges = null;
		if (cardNumber != null) {
			try {
				charges = chargeSerivce.getDebt(cardNumber);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, "请先绑定计费标准到停车场");
			}
		} else {
			charges = chargeSerivce.getUnCompleted();
		}

		return Utility.createJsonMsg(1001, "success", charges);
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String insert(@RequestBody PosChargeData charge) throws ParseException, AlipayApiException {

		int parkId = charge.getParkId();
		Park park = parkService.getParkById(parkId);
		// Outsideparkinfo outsideparkinfo =
		// outsideParkInfoService.getByParkidAndDate(parkId,
		// charge.getEntranceDate());
		if (charge.getParkDesc()==null) {
			charge.setParkDesc(park.getName());
		}
		Boolean isMonthUser=false;
		Boolean isRealMonthUser=false;
		List<Monthuser> monthusers=monthUserService.getByCardNumber(charge.getCardNumber());
		Monthuser monthuserUse=new Monthuser();
		for (Monthuser monthuser : monthusers) {
			if (monthuser.getParkid().intValue()==charge.getParkId()) {
				isMonthUser=true;
				monthuserUse=monthuser;
				break;
			}
		}
		if (isMonthUser) {
			Long diff=(monthuserUse.getEndtime().getTime()-(new Date()).getTime());	
			if (diff>0) {
				isRealMonthUser=true;
				
			}
		}
		if (isRealMonthUser) {
			charge.setParkDesc(charge.getParkDesc()+"-包月车");
		} else {
			charge.setParkDesc(charge.getParkDesc()+"-临停车");
		}
		if (park == null || park.getFeeCriterionId() == null) {
			return Utility.createJsonMsg(1002, "请先绑定计费标准到停车场");
		}
		// if (outsideparkinfo != null) {
		// if (charge.isPaidCompleted() == false) {
		// int Unusedcarportcount = outsideparkinfo.getUnusedcarportcount();
		// outsideparkinfo.setUnusedcarportcount(Unusedcarportcount - 1);
		// outsideparkinfo.setEntrancecount(outsideparkinfo.getEntrancecount() +
		// 1);
		// outsideparkinfo.setPossigndate(new Date());
		// outsideParkInfoService.updateByPrimaryKeySelective(outsideparkinfo);
		// } else {
		// outsideparkinfo.setEntrancecount(outsideparkinfo.getEntrancecount() +
		// 1);
		// outsideparkinfo.setOutcount(outsideparkinfo.getOutcount() + 1);
		// outsideparkinfo.setAmountmoney((float)
		// (outsideparkinfo.getAmountmoney() + charge.getChargeMoney()));
		// outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()
		// + charge.getPaidMoney()
		// + charge.getGivenMoney() - charge.getChangeMoney()));
		// outsideparkinfo.setPossigndate(new Date());
		// outsideParkInfoService.updateByPrimaryKeySelective(outsideparkinfo);
		// }
		// }

		if (charge.getEntranceDate() == null) {
			charge.setEntranceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
		int ret = chargeSerivce.insert(charge);
		if (ret == 1) {
			List<Parktoalipark> parktoaliparks = parkToAliparkService.getByParkId(parkId);
			if (!parktoaliparks.isEmpty()) {
				Parktoalipark parktoalipark = parktoaliparks.get(0);
				Map<String, String> argstoali = new HashMap<>();
				argstoali.put("parking_id", parktoalipark.getAliparkingid());
				argstoali.put("car_number", charge.getCardNumber());
				argstoali.put("in_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				aliparkFeeService.parkingEnterinfoSync(argstoali);
			}
			return Utility.createJsonMsg(1001, "success");
		}

		else
			return Utility.createJsonMsg(1002, "failed");
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String modify(@RequestBody PosChargeData charge) {

		int ret = chargeSerivce.update(charge);
		if (ret == 1)
			return Utility.createJsonMsg(1001, "success");
		else
			return Utility.createJsonMsg(1002, "failed");
	}
	@RequestMapping(value = "/updateEDate", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String updateEDate(@RequestBody Map<String, String> args) throws ParseException {
		String carNumber = args.get("carNumber");
		String entranceDate=args.get("entranceDate");
		List<PosChargeData> posChargeDatas=chargeSerivce.getLastRecord(carNumber, 1);
		if (posChargeDatas.isEmpty()) {
			return Utility.createJsonMsg(1002, "无记录");
		}
		PosChargeData posChargeData=posChargeDatas.get(0);
		posChargeData.setEntranceDate(entranceDate);
		int num=chargeSerivce.update(posChargeData);
		if (num==1) {
			return Utility.createJsonMsg(1001, "ok");
		}
		return Utility.createJsonMsg(1002, "failed!");
	}
	@RequestMapping(value = "/updateYj", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String updateYj(@RequestBody Map<String, String> args) {
		int parkId = Integer.parseInt(args.get("parkId"));
		String cardNumber = (String) args.get("cardNumber");
		Double yajin = Double.parseDouble(args.get("yajin"));
		List<PosChargeData> posChargeDatas = chargeSerivce.getByParkIdAndCardNumber(parkId, cardNumber);
		if (posChargeDatas.isEmpty()) {
			return Utility.createJsonMsg(1002, "no record");
		}
		// Outsideparkinfo outsideparkinfo =
		// outsideParkInfoService.getByParkidAndDate(parkId,posChargeDatas.get(0).getEntranceDate());
		// outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()
		// + yajin));
		posChargeDatas.get(0).setPaidMoney(yajin);
		// outsideParkInfoService.updateByPrimaryKeySelective(outsideparkinfo);
		int ret = chargeSerivce.update(posChargeDatas.get(0));
		if (ret == 1)
			return Utility.createJsonMsg(1001, "success");
		else
			return Utility.createJsonMsg(1002, "failed");
	}

	@RequestMapping(value = "/query", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String query(@RequestBody Map<String, Object> args) throws ParseException {
		String cardNumber = (String) args.get("cardNumber");
		String exitDate = (String) args.get("exitDate");
		List<PosChargeData> queryCharges = null;
		if (exitDate != null) {
			Date eDate = new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
			try {
				queryCharges = chargeSerivce.queryDebt(cardNumber, eDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		} else {
			try {
				queryCharges = chargeSerivce.queryDebt(cardNumber, new Date());
			} catch (Exception e) {
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		}
		return Utility.createJsonMsg(1001, "success", queryCharges);
	}

	@RequestMapping(value = "/getArrearageByCardNumber", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getArrearageByCardNumber(@RequestBody Map<String, Object> args) {
		String cardNumber = (String) args.get("cardNumber");
		List<PosChargeData> queryCharges = chargeSerivce.getArrearageByCardNumber(cardNumber);

		return Utility.createJsonMsg(1001, "success", queryCharges);
	}

	@RequestMapping(value = "/getArrearageByCardNumberSimple", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getArrearageByCardNumberSimple(@RequestBody Map<String, Object> args) {
		String cardNumber = (String) args.get("cardNumber");
		List<PosChargeData> queryCharges = chargeSerivce.getArrearageByCardNumber(cardNumber);
		List<PosChargeDataSimple> posChargeDataSimples = new ArrayList<>();
		for (PosChargeData posChargeData : queryCharges) {
			PosChargeDataSimple posChargeDataSimple = new PosChargeDataSimple();
			posChargeDataSimple.setId(posChargeData.getId());
			posChargeDataSimple.setCardNumber(posChargeData.getCardNumber());
			posChargeDataSimple.setUnPaidMoney(posChargeData.getUnPaidMoney());
			posChargeDataSimple.setExitDate(posChargeData.getExitDate());
			posChargeDataSimples.add(posChargeDataSimple);
		}
		return Utility.createJsonMsg(1001, "success", posChargeDataSimples);
	}

	@RequestMapping(value = "/queryCurrent", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String queryCurrent(@RequestBody Map<String, Object> args) throws ParseException {
		String cardNumber = (String) args.get("cardNumber");
		String exitDate = (String) args.get("exitDate");
		List<PosChargeData> queryCharges = null;
		if (exitDate != null) {
			Date eDate = new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
			try {
				queryCharges = chargeSerivce.queryCurrentDebt(cardNumber, eDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		} else {
			try {
				queryCharges = chargeSerivce.queryCurrentDebt(cardNumber, new Date());
			} catch (Exception e) {
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		}
		return Utility.createJsonMsg(1001, "success", queryCharges);
	}

	@RequestMapping(value = "/unpaid", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String getDebt(@RequestBody Map<String, Object> args) throws ParseException {
		String cardNumber = (String) args.get("cardNumber");
		String exitDate = (String) args.get("exitDate");

		List<PosChargeData> unpaidCharges = null;
		if (exitDate != null) {
			Date eDate = new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
			try {
				unpaidCharges = chargeSerivce.getDebt(cardNumber, eDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		} else {
			try {
				unpaidCharges = chargeSerivce.getDebt(cardNumber);
			} catch (Exception e) {
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		}

		return Utility.createJsonMsg(1001, "success", unpaidCharges);
	}

	@RequestMapping(value = "/exitAndPay", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String exitAndPay(@RequestBody Map<String, Object> args) throws Exception {
		String cardNumber = (String) args.get("cardNumber");
		double money = (double) args.get("money");
		String exitDate = (String) args.get("exitDate");
		PosChargeData payRet = null;

		Date eDate = new Date();
		if (exitDate != null) {
			eDate = new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
		}
		List<PosChargeData> unpaidCharges = chargeSerivce.getDebt(cardNumber, eDate);
		try {
			payRet = chargeSerivce.pay(cardNumber, money);
		} catch (Exception e) {
			return Utility.createJsonMsg(1002, "没有欠费条目或请先绑定停车场计费标准");
		}
		return Utility.createJsonMsg(1001, "success", payRet);
	}

	@RequestMapping(value = "/pay", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String pay(@RequestBody Map<String, Object> args) {
		String cardNumber = (String) args.get("cardNumber");
		double money = (double) args.get("money");

		PosChargeData payRet = null;
		try {
			payRet = chargeSerivce.pay(cardNumber, money);
		} catch (Exception e) {
			return Utility.createJsonMsg(1002, "没有欠费条目或请先绑定停车场计费标准");
		}

		return Utility.createJsonMsg(1001, "success", payRet);
	}

	@RequestMapping(value = "/getInfoByCardNumber", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String getInfoByCardNumber(@RequestBody Map<String, Object> args) throws Exception {
		String cardNumber = (String) args.get("cardNumber");
		Map<String, Object> result = new HashMap<>();
		List<PosChargeData> charges = chargeSerivce.queryDebt(cardNumber, new Date());
		if (charges.isEmpty()) {
			result.put("status", 1002);
			return Utility.gson.toJson(result);
		}
		PosChargeData lastCharge = charges.get(0);
		double charge = (lastCharge.getChargeMoney() - lastCharge.getPaidMoney()) > 0
				? (lastCharge.getChargeMoney() - lastCharge.getPaidMoney()) : 0;
		result.put("deposit", lastCharge.getPaidMoney());
		result.put("charge", charge);
		result.put("chargeId", lastCharge.getId());
		Feeoperator feeoperator = feeOperatorService.getOperatorByAccount(lastCharge.getOperatorId()).get(0);
		if (feeoperator == null) {
			result.put("status", 1002);
			return Utility.gson.toJson(result);
		}
		result.put("operatorName", feeoperator.getName());
		result.put("operatorPhone", feeoperator.getPhone());
		Park park = parkService.getParkById(lastCharge.getParkId());
		FeeCriterion feeCriterion = feeCriterionService.getById(park.getFeeCriterionId());
		if (feeCriterion == null) {
			result.put("status", 1002);
			return Utility.gson.toJson(result);
		}
		result.put("feeCriterion", feeCriterion.getExplaination());
		result.put("status", 1001);
		return Utility.gson.toJson(result);
	}

	@RequestMapping(value = "/pushInfoFromWechat", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String pushInfoFromWechat(@RequestBody Map<String, Object> args) throws Exception {
		Integer chargeId = (Integer) args.get("chargeId");
		Map<String, Object> result = new HashMap<>();
		PosChargeData posChargeData = chargeSerivce.getById(chargeId);
		Double charge = (Double) args.get("charge");
		posChargeData.setGivenMoney(charge);
		posChargeData.setPayType(3);
		posChargeData.setExitDate1(new Date());
		posChargeData.setPaidCompleted(true);
		if (chargeSerivce.update(posChargeData) == 1) {
			result.put("status", 1001);
		} else {
			result.put("status", 1002);
		}
		List<Pos> poses = posService.getByParkId(posChargeData.getParkId());
		List<String> audiences = new ArrayList<>();
		for (Pos pos : poses) {
			audiences.add(pos.getNum());
		}
		Map<String, String> extras = new HashMap<>();
		extras.put("parkId", String.valueOf(posChargeData.getParkId()));
		extras.put("portNum", String.valueOf(posChargeData.getPortNumber()));
		Jpush.SendPushToAudiencesWithExtras(audiences, extras, "wechatPaidChanged");
		return Utility.gson.toJson(result);
	}

	@RequestMapping(value = "/rejectReason", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String rejectReason(@RequestBody Map<String, Object> args) {
		String cardNumber = (String) args.get("cardNumber");
		String rejectReason = (String) args.get("rejectReason");
		PosChargeData lastCharge = null;
		try {
			lastCharge = chargeSerivce.updateRejectReason(cardNumber, rejectReason);
		} catch (Exception e) {
			return Utility.createJsonMsg(1002, "异常");
		}
		return Utility.createJsonMsg(1001, "success", lastCharge);
	}

	@RequestMapping(value = "/payById", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String payById(@RequestBody Map<String, Object> args) {
		String cardNumber = (String) args.get("cardNumber");
		double money = (double) args.get("money");
		Integer chargeId = (Integer) args.get("id");
		PosChargeData payRet = chargeSerivce.getById(chargeId);
		if (payRet != null && payRet.getCardNumber().equals(cardNumber)) {
			if (payRet.getUnPaidMoney() <= money) {
				payRet.setGivenMoney(money + payRet.getGivenMoney());
				money -= payRet.getUnPaidMoney();
				payRet.setPaidCompleted(true);
				String data = new DecimalFormat("0.00").format(money);
				payRet.setChangeMoney(Double.parseDouble(data));
				chargeSerivce.update(payRet);
			} else {
				payRet.setGivenMoney(money + payRet.getGivenMoney());
				// payRet.setPaidMoney(payRet.getPaidMoney()+money);
				payRet.setUnPaidMoney(payRet.getUnPaidMoney() - money);
				chargeSerivce.update(payRet);
			}
		} else {
			return Utility.createJsonMsg(1002, "付费失败");
		}

		return Utility.createJsonMsg(1001, "success", payRet);
	}

	@RequestMapping(value = "/getFeeOperatorChargeData", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getFeeOperatorChargeData(@RequestBody Map<String, String> args) throws ParseException {
		String startDay = (String) args.get("startDay");
		String endDay = (String) args.get("endDay");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(startDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date parsedEndDay = null;
		try {
			parsedEndDay = sdf.parse(endDay + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> results = chargeSerivce.getFeeOperatorChargeData(parsedStartDay, parsedEndDay);
		return Utility.createJsonMsg(1001, "success", results);
	}

	@RequestMapping(value = "/repay", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String repay(@RequestBody Map<String, Object> args) {
		String cardNumber = (String) args.get("cardNumber");
		double money = (double) args.get("money");

		List<PosChargeData> payRet = null;
		try {
			payRet = chargeSerivce.repay(cardNumber, money);
		} catch (Exception e) {
			return Utility.createJsonMsg(1002, "没有欠费条目或请先绑定停车场计费标准");
		}
		return Utility.createJsonMsg(1001, "success", payRet);
	}

	@RequestMapping(value = "/getNewFee", method = RequestMethod.POST, produces = {
	"application/json;charset=UTF-8" })
	@ResponseBody
	public String getNewFee(@RequestBody PosChargeData charge) throws Exception{
		chargeSerivce.newFeeCalcExpense(charge, charge.getExitDate(), false);
		return Utility.createJsonMsg(1001, "success", charge);
	}
	
	@RequestMapping(value = "/hardwareRecord", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String hardwareRecord(@RequestBody Map<String, Object> args) throws Exception {

		Map<String, Object> result = new HashMap<>();
		result.put("status", 1001);

		return Utility.gson.toJson(result);
	}

	@RequestMapping("/getExcel")
	@ResponseBody
	public void getExcel(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
		List<PosChargeData> posdatas = chargeSerivce.getPage(0, 100000);
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers = { "车牌", "停车场名", "车位号", "操作员id", "收费状态", "押金", "应收费", "补交", "返还", "进场时间", "离场时间" };
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR + "poschargedata.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		excelService.produceExceldataPosChargeData("收费明细", headers, posdatas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR + "poschargedata.xlsx", response);
	}

	@RequestMapping(value = "/getExcelByParkAndDay")
	@ResponseBody
	public void getExcelByParkAndDay(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, NumberFormatException, ParseException {
		String date = request.getParameter("date");
		String parkId = request.getParameter("parkId");

		List<PosChargeData> posdatas = chargeSerivce.getByParkAndDay(Integer.parseInt(parkId), date);
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers = { "车牌", "停车场名", "车位号", "操作员id", "收费状态", "押金", "应收费", "补交", "返还", "进场时间", "离场时间" };
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
		String filename = sFormat.format(new Date()) + "poschargedata.xlsx";
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR + "poschargedata.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		excelService.produceExceldataPosChargeData("收费明细", headers, posdatas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR + "poschargedata.xlsx", response);
	}

	@RequestMapping(value = "/getExcelByDay")
	@ResponseBody
	public void getExcelByDay(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, NumberFormatException, ParseException {
		String date = request.getParameter("date");
		List<PosChargeData> posdatas = chargeSerivce.getAllByDay(date);
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers = { "车牌", "停车场名", "车位号", "操作员id", "收费状态", "押金", "应收费", "补交", "返还", "进场时间", "离场时间" };
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR + "poschargedata.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		excelService.produceExceldataPosChargeData("收费明细", headers, posdatas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR + "poschargedata.xlsx", response);
	}
	@RequestMapping(value = "/getPayTypeCountByRange", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String getPayTypeCountByRange(@RequestBody Map<String, Object> args) throws ParseException {
		String startDate = (String) args.get("startDate");
		String endDate = (String) args.get("endDate");
		int parkId = (int) args.get("parkId");
		List<PosChargeData> posdatas = chargeSerivce.getByParkAndDayRange(parkId, startDate, endDate);
		int type0=0;
		int type1=0;
		int type2=0;
		int type9=0;
		for (PosChargeData posChargeData : posdatas) {
			switch (posChargeData.getPayType()) {
			case 0:
				type0+=1;
				break;
			case 1:
				type1+=1;
				break;
			case 2:
				type2+=1;
				break;
			case 9:
				type9+=1;
				break;

			default:
				break;
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("status", 1001);
		result.put("alipay", type0);
		result.put("weipay", type1);
		result.put("cash", type2);
		result.put("barrier", type9);
		return  Utility.gson.toJson(result);
	}
	@RequestMapping(value = "/getExcelByParkAndDayRange")
	@ResponseBody
	public void getExcelByParkAndDayRange(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, NumberFormatException, ParseException {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String parkId = request.getParameter("parkId");

		List<PosChargeData> posdatas = chargeSerivce.getByParkAndDayRange(Integer.parseInt(parkId), startDate, endDate);
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers = { "车牌", "停车场名", "车位号", "操作员id", "收费状态", "押金", "应收费", "补交", "返还", "进场时间", "离场时间" };
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR + "poschargedata.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		excelService.produceExceldataPosChargeData("收费明细", headers, posdatas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR + "poschargedata.xlsx", response);
	}

	@RequestMapping(value = "/getExcelByDayRange")
	@ResponseBody
	public void getExcelByDayRange(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, NumberFormatException, ParseException {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		List<PosChargeData> posdatas = chargeSerivce.getAllByDayRange(startDate, endDate);
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers = { "车牌", "停车场名", "车位号", "操作员id", "收费状态", "押金", "应收费", "补交", "返还", "进场时间", "离场时间" };
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR + "poschargedata.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		excelService.produceExceldataPosChargeData("收费明细", headers, posdatas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR + "poschargedata.xlsx", response);
	}
}

package com.park.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.park.model.Constants;
import com.park.model.FeeCriterion;
import com.park.model.Page;
import com.park.model.Park;
import com.park.model.PosChargeData;
import com.park.model.Posdata;
import com.park.service.AuthorityService;
import com.park.service.ExcelExportService;
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
	
	@Autowired
	private ExcelExportService excelService;
	
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
	
	@RequestMapping(value="/getByParkAndRange",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getByParkAndRange(@RequestBody Map<String,Object> args){
		int parkId=Integer.parseInt((String)args.get("parkId"));
		String startDay=(String)args.get("startDay");
		String endDay=(String)args.get("endDay");
		Map<String, Object> retMap = new HashMap<String, Object>();
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
		List<PosChargeData> posChargeDatas=chargeSerivce.selectPosdataByParkAndRange(parsedStartDay, parsedEndDay, parkId);
		if (posChargeDatas.isEmpty()) {
			retMap.put("status", 1002);
		}
		else {
			retMap.put("status", 1001);
			retMap.put("message", "success");
			retMap.put("body", posChargeDatas);
		}		
		return Utility.gson.toJson(retMap);
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
	public @ResponseBody String insert(@RequestBody PosChargeData charge) throws ParseException{
		
		int parkId = charge.getParkId();
		Park park = parkService.getParkById(parkId);
		
		if(park == null || park.getFeeCriterionId() == null){
			return Utility.createJsonMsg(1002, "请先绑定计费标准到停车场");
		}
		if (charge.getEntranceDate()==null) {
			charge.setEntranceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}		
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
	@RequestMapping(value = "/query", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String query(@RequestBody Map<String, Object> args) throws ParseException{
		String cardNumber = (String) args.get("cardNumber");
		String exitDate = (String) args.get("exitDate");
		List<PosChargeData> queryCharges = null;
		if (exitDate!=null) {
			Date eDate=new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
			try {
				queryCharges = chargeSerivce.queryDebt(cardNumber,eDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准" );
			}
		}
		else {
			try {
				queryCharges = chargeSerivce.queryDebt(cardNumber,new Date());
			} catch (Exception e) {
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准" );
			}
		}			
		return Utility.createJsonMsg(1001, "success", queryCharges);
	}
	
	@RequestMapping(value = "/queryCurrent", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String queryCurrent(@RequestBody Map<String, Object> args) throws ParseException{
		String cardNumber = (String) args.get("cardNumber");
		String exitDate = (String) args.get("exitDate");
		List<PosChargeData> queryCharges = null;
		if (exitDate!=null) {
			Date eDate=new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
			try {
				queryCharges = chargeSerivce.queryCurrentDebt(cardNumber,eDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准" );
			}
		}
		else {
			try {
				queryCharges = chargeSerivce.queryCurrentDebt(cardNumber,new Date());
			} catch (Exception e) {
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准" );
			}
		}			
		return Utility.createJsonMsg(1001, "success", queryCharges);
	}
	
	@RequestMapping(value = "/unpaid", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody String getDebt(@RequestBody Map<String, Object> args) throws ParseException{
		String cardNumber = (String) args.get("cardNumber");
		String exitDate = (String) args.get("exitDate");
		
		List<PosChargeData> unpaidCharges = null;
		if (exitDate!=null) {
			Date eDate=new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
			try {
				unpaidCharges = chargeSerivce.getDebt(cardNumber,eDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准" );
			}
		}
		else {
			try {
				unpaidCharges = chargeSerivce.getDebt(cardNumber);
			} catch (Exception e) {
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准" );
			}
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
			return Utility.createJsonMsg(1002, "没有欠费条目或请先绑定停车场计费标准" );
		}
		
		return Utility.createJsonMsg(1001, "success", payRet);
	}
	@RequestMapping("/getExcel")
	@ResponseBody
	public void getExcel(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException{
		List<PosChargeData> posdatas=chargeSerivce.getPage(0, 2000);
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers={"车牌","停车场名","车位号","操作员id","收费状态","押金","应收费","补交","返还","进场时间","离场时间"};
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR+ "poschargedata.xls");
		HSSFWorkbook workbook = new HSSFWorkbook();
		excelService.produceExceldataPosChargeData("收费明细", headers, posdatas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR+ "poschargedata.xls", response);
	}

}

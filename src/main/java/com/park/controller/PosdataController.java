package com.park.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.park.model.DataUsageCardDetail;
import com.park.model.Page;
import com.park.model.Park;
import com.park.model.Posdata;
import com.park.model.posdataReceive;
import com.park.service.AuthorityService;
import com.park.service.ExcelExportService;
import com.park.service.ParkService;
import com.park.service.PosdataService;
import com.park.service.UserPagePermissionService;
import com.park.service.Utility;
import com.park.service.impl.ExcelServiceImpl;

@Controller
@RequestMapping("/pos")
public class PosdataController {
	
@Autowired
private PosdataService posdataService;
@Autowired
private ParkService parkService;
@Autowired
private AuthorityService authService;

@Autowired
private UserPagePermissionService pageService;

@Autowired
private ExcelExportService excelService;
@RequestMapping(value = "/insertChargeDetail", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
@ResponseBody
public String insertPosdata(@RequestBody List<posdataReceive> posdatarecv ){
	int listnum=posdatarecv.size();
	int num=0;
	for(int i=0;i<listnum;i++)
	{
		Posdata posdata=new Posdata();
		posdata.setBackbyte(posdatarecv.get(i).getBackbyte());
		posdata.setCardsnr(posdatarecv.get(i).getCardsnr());
		posdata.setCardtype(posdatarecv.get(i).getCardtype());
		posdata.setCredencesnr(posdatarecv.get(i).getCredencesnr());
		posdata.setGiving(posdatarecv.get(i).getGiving());
		posdata.setMemo(posdatarecv.get(i).getMemo());
		posdata.setMode(posdatarecv.get(i).getMode());
		posdata.setMoney(posdatarecv.get(i).getMoney());
		posdata.setPossnr(posdatarecv.get(i).getPossnr());
		posdata.setRealmoney(posdatarecv.get(i).getRealmoney());
		posdata.setReturnmoney(posdatarecv.get(i).getReturnmoney());
		posdata.setSitename(posdatarecv.get(i).getSitename());
		posdata.setSysid(posdatarecv.get(i).getSysid());
		posdata.setUserid(posdatarecv.get(i).getUserid());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date parsedStarttime = null;
		try {
			parsedStarttime = sdf.parse(posdatarecv.get(i).getStarttime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		posdata.setStarttime(parsedStarttime);
		Date parseEndtime=null;
		try {
			parseEndtime=sdf.parse(posdatarecv.get(i).getEndtime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		posdata.setEndtime(parseEndtime);
		num+=posdataService.insert(posdata);
	}
	
	Map<String, Object> retMap = new HashMap<String, Object>();
	if(num==listnum)
	{
		retMap.put("status", 1001);
		retMap.put("message", "success");
	}
	else
	{
		retMap.put("status", 1002);
		retMap.put("message", "failure");
	}
	return Utility.gson.toJson(retMap);
}
@RequestMapping(value="/getChargeDetail", produces = {"application/json;charset=UTF-8"})
@ResponseBody
public String getChargeDetail(){
	Map<String, Object> retMap = new HashMap<String, Object>();
	List<Posdata> posdatas=posdataService.selectAll();
	if(posdatas!=null)
	{
		retMap.put("status", 1001);
		retMap.put("message", "success");
		retMap.put("body", posdatas);
	}
	else
	{
		retMap.put("status", 1002);
		retMap.put("message", "failure");
	}
	return Utility.gson.toJson(retMap);
}
@RequestMapping(value="/chargeDetail")
public String chargeDetail(ModelMap modelMap, HttpServletRequest request, HttpSession session){
	String username = (String) session.getAttribute("username");
	List<Park> parkList = parkService.getParks();
	if(username != null)
		parkList = parkService.filterPark(parkList, username);
	List<Park> parkl = new ArrayList<>();
	for (Park park : parkList) {
		if (park.getType()==3) {
			parkl.add(park);
		}
	}
	modelMap.addAttribute("parks", parkl);
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
	return "posChargeDetail";
}
@RequestMapping(value="/carportUsage")
public String carportUsage(ModelMap modelMap, HttpServletRequest request, HttpSession session){
	List<Park> parkList = parkService.getParks();
	String username = (String) session.getAttribute("username");
	if(username != null)
		parkList = parkService.filterPark(parkList, username);

	List<Park> parkl = new ArrayList<>();
	for (Park park : parkList) {
		if (park.getType()==3) {
			parkl.add(park);
		}
	}
	modelMap.addAttribute("parks", parkl);
	
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
	
	return "carportUsage";
}
@RequestMapping(value="/getPosdataCount",method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
@ResponseBody
public String getPosdataCount(){
	Map<String, Object> retMap = new HashMap<String, Object>();
	Integer count=posdataService.getPosdataCount();
	retMap.put("status", 1001);
	retMap.put("message", "success");
	retMap.put("count", count);
	return Utility.gson.toJson(retMap);
}
@RequestMapping(value="/getPosdataCountByPark/{parkId}",method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
@ResponseBody
public String getPosdataCountByPark(@PathVariable("parkId")Integer parkId){
	Map<String, Object> retMap = new HashMap<String, Object>();
	Integer count=posdataService.getPosdataCountByPark(parkId);
	retMap.put("status", 1001);
	retMap.put("message", "success");
	retMap.put("count", count);
	return Utility.gson.toJson(retMap);
}

@RequestMapping("/getExcel")
@ResponseBody
public void getExcel(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException{
	List<Posdata> posdatas=posdataService.selectPosdataByPage(0,500);
	String docsPath = request.getSession().getServletContext().getRealPath("/");
	final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
	String[] headers={"车牌","停车场名","车位号","出入场","操作员id","终端机号","应收费","押金","补交","返还","进场时间","离场时间"};
	OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR+ "posdata.xls");
	HSSFWorkbook workbook = new HSSFWorkbook();
	excelService.produceExceldataPosData("收费明细", headers, posdatas, workbook);
	
	try {
		workbook.write(out);
	} catch (IOException e) {
		e.printStackTrace();
	}
	Utility.download(docsPath + FILE_SEPARATOR+ "posdata.xls", response);
}

@RequestMapping(value="/getParkCharge",method=RequestMethod.GET)
@ResponseBody
public String getParkCharge(@RequestParam("parkId") int parkId, @RequestParam("startDay")String startDay, @RequestParam("endDay")String endDay ){
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	Date parsedStartDay = null;
	try {
		parsedStartDay = sdf.parse(startDay);
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Date parsedEndDay  = null;
	try {
		parsedEndDay = sdf.parse(endDay);
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Map<String, Object> result = posdataService.getParkCharge(parkId, parsedStartDay, parsedEndDay);
	return Utility.createJsonMsg(1001, "success", result);
}

@RequestMapping(value="/getCarportCharge",method=RequestMethod.GET)
@ResponseBody
public String getCarportCharge(@RequestParam("carport") int carportId, @RequestParam("startDay")String startDay, @RequestParam("endDay")String endDay ){
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	Date parsedStartDay = null;
	try {
		parsedStartDay = sdf.parse(startDay);
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Date parsedEndDay  = null;
	try {
		parsedEndDay = sdf.parse(endDay);
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Map<String, Object> result = posdataService.getCarportCharge(carportId, parsedStartDay, parsedEndDay);
	return Utility.createJsonMsg(1001, "success", result);
}


@RequestMapping(value="/selectPosdataByPage",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String selectPosdataByPage(@RequestBody Map<String,Object> args){
	int low=(Integer)args.get("low");
	int count=(Integer)args.get("count");
	Map<String, Object> retMap = new HashMap<String, Object>();
	List<Posdata> posdatas=posdataService.selectPosdataByPage(low, count);
	if(posdatas!=null)
	{
		retMap.put("status", 1001);
		retMap.put("message", "success");
		retMap.put("body", posdatas);
	}
	else
	{
		retMap.put("status", 1002);
		retMap.put("message", "failure");
	}
	return Utility.gson.toJson(retMap);
}
@RequestMapping(value="/selectPosdataByPageAndPark",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String selectPosdataByPageAndPark(@RequestBody Map<String,Object> args){
	int low=(Integer)args.get("low");
	int count=(Integer)args.get("count");
	int parkId=(Integer)args.get("parkId");
	Map<String, Object> retMap = new HashMap<String, Object>();
	List<Posdata> posdatas=posdataService.selectPosdataByPageAndPark(parkId, low, count);
	if(posdatas!=null)
	{
		retMap.put("status", 1001);
		retMap.put("message", "success");
		retMap.put("body", posdatas);
	}
	else
	{
		retMap.put("status", 1002);
		retMap.put("message", "failure");
	}
	return Utility.gson.toJson(retMap);
}

@RequestMapping(value="/selectPosdataByParkAndRange",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String selectPosdataByParkAndRange(@RequestBody Map<String,Object> args){
	int parkId=Integer.parseInt((String)args.get("parkId"));
	Park park = parkService.getParkById(parkId);
	String parkName=park.getName();
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
	List<Posdata> posdatas=posdataService.selectPosdataByParkAndRange(parkName, parsedStartDay, parsedEndDay);
	retMap.put("status", 1001);
	retMap.put("message", "success");
	retMap.put("body", posdatas);
	return Utility.gson.toJson(retMap);
}
@RequestMapping(value="/selectPosdataByParkAndCarportId",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String selectPosdataByParkAndCarportId(@RequestBody Map<String,Object> args){
	int parkId=Integer.parseInt((String)args.get("parkId"));
	Park park = parkService.getParkById(parkId);
	String parkName=park.getName();
	String startDay=(String)args.get("startDay");
	String endDay=(String)args.get("endDay");
	String carportId=(String)args.get("carportId");
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
	List<Posdata> posdatas=posdataService.getPosdataByCarportAndRange(parkName,carportId, parsedStartDay, parsedEndDay);
	retMap.put("status", 1001);
	retMap.put("message", "success");
	retMap.put("body", posdatas);
	return Utility.gson.toJson(retMap);
}


@RequestMapping(value="/getParkChargeByRange",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String getParkChargeByRange(@RequestBody Map<String, Object> args){
	int parkId=Integer.parseInt((String)args.get("parkId"));
	Park park = parkService.getParkById(parkId);
	String parkName=park.getName();
	String startDay=(String)args.get("startDay");
	String endDay=(String)args.get("endDay");
	Map<String, Object> retMap = new HashMap<>();
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
          
        Map<String,Object> tmpmap=posdataService.getParkChargeByDay(parkId, df.format(d));
        comparemap.put(d.getTime(), tmpmap);
    //    retMap.put(df.format(d), tmpmap);
        time += oneDay;  
    }  
    
	//List<Posdata> posdatas=posdataService.selectPosdataByParkAndRange(parkName, parsedStartDay, parsedEndDay);
	
	return Utility.gson.toJson(comparemap);
}
@RequestMapping(value="/getCountsByCard",produces={"application/json;charset=utf-8"})
@ResponseBody
public String getCountsByCard(){
	Map<String, Object> ret=new HashMap<>();
	List<Map<String, Object>> data=posdataService.getCountByCard();
	if (data!=null) {
		ret.put("status", 1001);
	}
	ret.put("body", data);
	return Utility.gson.toJson(ret);
}
@RequestMapping(value="/getCarportChargeByRange",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String getCarportChargeByRange(@RequestBody Map<String, Object> args){
	int parkId=Integer.parseInt((String)args.get("parkId"));
	Park park = parkService.getParkById(parkId);
	String parkName=park.getName();
	String carportId=(String)args.get("carportId");
	String startDay=(String)args.get("startDay");
	String endDay=(String)args.get("endDay");
	Map<String, Object> retMap = new HashMap<>();
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
	          
	        Map<String,Object> tmpmap=posdataService.getCarpotChargeByDay(parkId, carportId, df.format(d));
	        comparemap.put(d.getTime(), tmpmap);
	    //    retMap.put(df.format(d), tmpmap);
	        time += oneDay;  
	    }
	 return Utility.gson.toJson(comparemap);
}

}

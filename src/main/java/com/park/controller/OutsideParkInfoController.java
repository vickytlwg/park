package com.park.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.model.Area;
import com.park.model.Outsideparkinfo;
import com.park.model.Park;
import com.park.model.Street;
import com.park.model.Zonecenter;
import com.park.service.AreaService;
import com.park.service.OutsideParkInfoService;
import com.park.service.ParkService;
import com.park.service.PosChargeDataService;
import com.park.service.StreetService;
import com.park.service.Utility;
import com.park.service.ZoneCenterService;

@Controller
@RequestMapping("outsideParkInfo")
public class OutsideParkInfoController {
	@Autowired
	private ZoneCenterService zoneCenterService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private StreetService streetService;
	@Autowired
	private ParkService parkService;
	@Autowired
	private PosChargeDataService posChargeDataService;
	@Autowired 
	private OutsideParkInfoService outsideParkInfoService;
	@RequestMapping(value="zoneCenterInfo",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String zoneCenterInfo(@RequestParam("start")int start,@RequestParam("count")int count){
		Map<String, Object> result=new HashMap<>();
		List<Map<String, Object>> info=new ArrayList<>();
		String day=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		List<Zonecenter> zonecenters=zoneCenterService.getByStartAndCount(0,100);
		for (Zonecenter zonecenter : zonecenters) {
			Map<String, Object> tmpdata=new HashMap<>();
			tmpdata.put("id",zonecenter.getId());
			tmpdata.put("zonenum", zonecenter.getNum());
			tmpdata.put("zonename", zonecenter.getName());
			List<Area> areas=areaService.getByZoneCenterId(zonecenter.getId());
			tmpdata.put("areacount", areas.size());
			int streetCount=0;
			int parkCount=0;
			int carportCount=0;
			int carportLeftCount=0;
			int amountMoney=0;
			int realMoney=0;
			int entranceCount=0;
			int outCount=0;
			int onlineParkCount=0;
			int onlineCarportCount=0;
			for (Area area : areas) {
				List<Street> streets=streetService.getByArea(area.getId());
				streetCount+=streets.size();
				for (Street street : streets) {
					List<Park> parks=parkService.getOutsideParkByStreetId(street.getId());
					parkCount+=parks.size();
					for (Park park : parks) {
						Outsideparkinfo parkInfo=posChargeDataService.getOutsideparkinfoByOrigin(park.getId(), day);
						if (parkInfo!=null) {
							carportCount+=parkInfo.getCarportcount();
							carportLeftCount+=parkInfo.getUnusedcarportcount();
							amountMoney+=parkInfo.getAmountmoney();
							realMoney+=parkInfo.getRealmoney();
							entranceCount+=parkInfo.getEntrancecount();
							outCount+=parkInfo.getOutcount();
							if (parkInfo.getPossigndate()==null) {
								continue;
							}
							Long signDate=parkInfo.getPossigndate().getTime();
							Long diffMin=((new Date()).getTime()-signDate)/(1000*60);
							if (diffMin<=30) {
								onlineParkCount+=1;
								onlineCarportCount+=parkInfo.getCarportcount();
							}
						}						
					}
				}
			}
			tmpdata.put("streetcount", streetCount);
			tmpdata.put("parkcount", parkCount);
			tmpdata.put("carportcount", carportCount);
			tmpdata.put("carportleftcount", carportLeftCount);
			tmpdata.put("amountmoney", amountMoney);
			tmpdata.put("realmoney", realMoney);
			tmpdata.put("entrancecount", entranceCount);
			tmpdata.put("outcount", outCount);
			tmpdata.put("onlineparkcount", onlineParkCount);
			tmpdata.put("onlinecarportcount", onlineCarportCount);
			info.add(tmpdata);
		}
		result.put("status", 1001);
		result.put("body", info);
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="/areaInfo/{zoneid}",method=RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String areaInfo(@PathVariable("zoneid")int zoneid){
		Map<String, Object> result=new HashMap<>();
		String day=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		List<Area> areas=areaService.getByZoneCenterId(zoneid);
		List<Map<String, Object>> info=new ArrayList<>();
		for (Area area : areas) {
			Map<String, Object> tmpdata=new HashMap<>();
			tmpdata.put("id", area.getId());
			tmpdata.put("areanum", area.getNumber());
			tmpdata.put("areaname", area.getName());
			int streetCount=0;
			int parkCount=0;
			int carportCount=0;
			int carportLeftCount=0;
			int amountMoney=0;
			int realMoney=0;
			int entranceCount=0;
			int outCount=0;
			int onlineParkCount=0;
			int onlineCarportCount=0;
			List<Street> streets=streetService.getByArea(area.getId());
			streetCount+=streets.size();
			for (Street street : streets) {
				List<Park> parks=parkService.getOutsideParkByStreetId(street.getId());
				parkCount+=parks.size();
				for (Park park : parks) {
					Outsideparkinfo parkInfo=posChargeDataService.getOutsideparkinfoByOrigin(park.getId(), day);
					if (parkInfo==null) {
						continue;
					}
					carportCount+=parkInfo.getCarportcount();
					carportLeftCount+=parkInfo.getUnusedcarportcount();
					amountMoney+=parkInfo.getAmountmoney();
					realMoney+=parkInfo.getRealmoney();
					entranceCount+=parkInfo.getEntrancecount();
					outCount+=parkInfo.getOutcount();
					if (parkInfo.getPossigndate()==null) {
						continue;
					}
					Long signDate=parkInfo.getPossigndate().getTime();
					Long diffMin=((new Date()).getTime()-signDate)/(1000*60);
					if (diffMin<=30) {
						onlineParkCount+=1;
						onlineCarportCount+=parkInfo.getCarportcount();
					}
				}
			}
			tmpdata.put("streetcount", streetCount);
			tmpdata.put("parkcount", parkCount);
			tmpdata.put("carportcount", carportCount);
			tmpdata.put("carportleftcount", carportLeftCount);
			tmpdata.put("amountmoney", amountMoney);
			tmpdata.put("realmoney", realMoney);
			tmpdata.put("entrancecount", entranceCount);
			tmpdata.put("outcount", outCount);
			tmpdata.put("onlineparkcount", onlineParkCount);
			tmpdata.put("onlinecarportcount", onlineCarportCount);
			info.add(tmpdata);
		}
		result.put("status", 1001);
		result.put("body", info);
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="/streetInfo/{areaid}",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String streetInfo(@PathVariable("areaid")int areaid){
		Map<String, Object> result=new HashMap<>();
		String day=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		List<Street> streets=streetService.getByArea(areaid);
		List<Map<String, Object>> info=new ArrayList<>();
		for (Street street : streets) {
			Map<String, Object> tmpdata=new HashMap<>();
			tmpdata.put("id",street.getId());
			tmpdata.put("streetnum", street.getNumber());
			tmpdata.put("streetname", street.getName());
			int parkCount=0;
			int carportCount=0;
			int carportLeftCount=0;
			int amountMoney=0;
			int realMoney=0;
			int entranceCount=0;
			int outCount=0;
			int onlineParkCount=0;
			int onlineCarportCount=0;
			List<Park> parks=parkService.getOutsideParkByStreetId(street.getId());
			parkCount+=parks.size();
			for (Park park : parks) {
				Outsideparkinfo parkInfo=posChargeDataService.getOutsideparkinfoByOrigin(park.getId(), day);
				if (parkInfo==null) {
					continue;
				}
				carportCount+=parkInfo.getCarportcount();
				carportLeftCount+=parkInfo.getUnusedcarportcount();
				amountMoney+=parkInfo.getAmountmoney();
				realMoney+=parkInfo.getRealmoney();
				entranceCount+=parkInfo.getEntrancecount();
				outCount+=parkInfo.getOutcount();
				if (parkInfo.getPossigndate()==null) {
					continue;
				}
				Long signDate=parkInfo.getPossigndate().getTime();
				Long diffMin=((new Date()).getTime()-signDate)/(1000*60);
				if (diffMin<=30) {
					onlineParkCount+=1;
					onlineCarportCount+=parkInfo.getCarportcount();
				}
			}
			tmpdata.put("parkcount", parkCount);
			tmpdata.put("carportcount", carportCount);
			tmpdata.put("carportleftcount", carportLeftCount);
			tmpdata.put("amountmoney", amountMoney);
			tmpdata.put("realmoney", realMoney);
			tmpdata.put("entrancecount", entranceCount);
			tmpdata.put("outcount", outCount);
			tmpdata.put("onlineparkcount", onlineParkCount);
			tmpdata.put("onlinecarportcount", onlineCarportCount);
			info.add(tmpdata);
		}
		result.put("status", 1001);
		result.put("body", info);
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="/parkInfo/{streetid}",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String parkinfo(@PathVariable("streetid")int streetid){
		Map<String, Object> result=new HashMap<>();
		String day=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		List<Park> parks=parkService.getOutsideParkByStreetId(streetid);
		List<Map<String, Object>> info=new ArrayList<>();
		for (Park park : parks) {
			Outsideparkinfo parkInfo=posChargeDataService.getOutsideparkinfoByOrigin(park.getId(), day);
			if (parkInfo==null) {
				continue;
			}
			Map<String, Object> tmpdata=new HashMap<>();
			tmpdata.put("id",park.getId());
			tmpdata.put("contactnum", park.getNumber());
			tmpdata.put("contactname", park.getContact());
			tmpdata.put("parkname", park.getName());
			tmpdata.put("carportcount", parkInfo.getCarportcount());
			tmpdata.put("carportleftcount", parkInfo.getUnusedcarportcount());	
			tmpdata.put("amountmoney", parkInfo.getAmountmoney());
			tmpdata.put("realmoney", parkInfo.getRealmoney());
			tmpdata.put("entrancecount", parkInfo.getEntrancecount());
			tmpdata.put("outcount", parkInfo.getOutcount());
			if (parkInfo.getPossigndate()==null) {
				tmpdata.put("isonline", false);
				info.add(tmpdata);
				continue;
			}
			Long signDate=parkInfo.getPossigndate().getTime();
			Long diffMin=((new Date()).getTime()-signDate)/(1000*60);
			if (diffMin>=30) {
				tmpdata.put("isonline", false);
			}
			else {
				tmpdata.put("isonline", true);
			}
			info.add(tmpdata);
		}
		result.put("status", 1001);
		result.put("body", info);
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="/update",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String update(@RequestBody Outsideparkinfo info){
		Map<String, Object> result=new HashMap<>();
		int num=outsideParkInfoService.updateByPrimaryKeySelective(info);
		if (num==1) {
			result.put("status", 1001);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="/getInfoByParkId/{parkId}",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getInfoByParkId(@PathVariable("parkId")int parkId){
		Map<String, Object> result=new HashMap<>();
		Outsideparkinfo outsideparkinfo=outsideParkInfoService.getByParkidAndDate(parkId);
		if (outsideparkinfo!=null) {
			result.put("status", 1001);
			result.put("body", outsideparkinfo);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
	
	@RequestMapping(value="/posHeartBeat/{parkId}",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String posHeartBeat(@PathVariable("parkId")int parkId){
		Map<String, Object> result=new HashMap<>();
		Outsideparkinfo outsideparkinfo=outsideParkInfoService.getByParkidAndDate(parkId);
		outsideparkinfo.setPossigndate(new Date());
		int num=outsideParkInfoService.updateByPrimaryKeySelective(outsideparkinfo);
		if (num==1) {
			result.put("status", 1001);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
}

package com.park.controller;

import java.util.ArrayList;
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
	private OutsideParkInfoService outsideParkInfoService;
	@RequestMapping(value="zoneCenterInfo",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String zoneCenterInfo(@RequestParam("start")int start,@RequestParam("count")int count){
		Map<String, Object> result=new HashMap<>();
		List<Map<String, Object>> info=new ArrayList<>();
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
			for (Area area : areas) {
				List<Street> streets=streetService.getByArea(area.getId());
				streetCount+=streets.size();
				for (Street street : streets) {
					List<Park> parks=parkService.getOutsideParkByStreetId(street.getId());
					parkCount+=parks.size();
					for (Park park : parks) {
						carportCount+=park.getPortCount();
						carportLeftCount+=park.getPortLeftCount();
					}
				}
			}
			tmpdata.put("streetcount", streetCount);
			tmpdata.put("parkcount", parkCount);
			tmpdata.put("carportcount", carportCount);
			tmpdata.put("carportleftcount", carportLeftCount);
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
			List<Street> streets=streetService.getByArea(area.getId());
			streetCount+=streets.size();
			for (Street street : streets) {
				List<Park> parks=parkService.getOutsideParkByStreetId(street.getId());
				parkCount+=parks.size();
				for (Park park : parks) {
					carportCount+=park.getPortCount();
					carportLeftCount+=park.getPortLeftCount();
				}
			}
			tmpdata.put("streetcount", streetCount);
			tmpdata.put("parkcount", parkCount);
			tmpdata.put("carportcount", carportCount);
			tmpdata.put("carportleftcount", carportLeftCount);
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
			List<Park> parks=parkService.getOutsideParkByStreetId(street.getId());
			parkCount+=parks.size();
			for (Park park : parks) {
				carportCount+=park.getPortCount();
				carportLeftCount+=park.getPortLeftCount();
			}
			tmpdata.put("parkcount", parkCount);
			tmpdata.put("carportcount", carportCount);
			tmpdata.put("carportleftcount", carportLeftCount);
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
		List<Park> parks=parkService.getOutsideParkByStreetId(streetid);
		List<Map<String, Object>> info=new ArrayList<>();
		for (Park park : parks) {
			Map<String, Object> tmpdata=new HashMap<>();
			tmpdata.put("id",park.getId());
			tmpdata.put("parknum", park.getNumber());
			tmpdata.put("parkname", park.getName());
			tmpdata.put("carportcount", park.getPortCount());
			tmpdata.put("carportleftcount", park.getPortLeftCount());		
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
}

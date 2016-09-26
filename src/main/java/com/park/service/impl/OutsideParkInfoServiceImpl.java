package com.park.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.dao.OutsideparkinfoDAO;
import com.park.model.Outsideparkinfo;
import com.park.model.Park;
import com.park.model.ParkDetail;
import com.park.service.OutsideParkInfoService;
import com.park.service.ParkService;
@Service
public class OutsideParkInfoServiceImpl implements OutsideParkInfoService {
	@Autowired
	private OutsideparkinfoDAO outsideParkInfoDAO;
	@Autowired
	private ParkService parkService;
	@Override
	public List<Map<String, Object>> getInfoZoneCenter(int start, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getInfoArea(int zoneid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getInfoStreet(int areaid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getInfoPark(int streetid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return outsideParkInfoDAO.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Outsideparkinfo record) {
		// TODO Auto-generated method stub
		return outsideParkInfoDAO.insert(record);
	}

	@Override
	public int insertSelective(Outsideparkinfo record) {
		// TODO Auto-generated method stub
		return outsideParkInfoDAO.insertSelective(record);
	}

	@Override
	public Outsideparkinfo selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return outsideParkInfoDAO.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Outsideparkinfo record) {
		// TODO Auto-generated method stub
		return outsideParkInfoDAO.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Outsideparkinfo record) {
		// TODO Auto-generated method stub
		return outsideParkInfoDAO.updateByPrimaryKey(record);
	}

	@Override
	public void insertDayParkInfo() {
		// TODO Auto-generated method stub
		List<ParkDetail> parkDetails=parkService.getOutsideParkDetail(0, 9999);
		Date date=new Date();
		for (ParkDetail parkDetail : parkDetails) {
			Outsideparkinfo record=new Outsideparkinfo();
			record.setParkid(parkDetail.getId());
			record.setDate(date);
			record.setCarportcount(parkDetail.getPortCount());
			record.setUnusedcarportcount(parkDetail.getPortCount());
			outsideParkInfoDAO.insertSelective(record);
		}
	}

	@Override
	public Outsideparkinfo getByParkidAndDate(int parkId) {
		
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		String date=sf.format(new Date());
		return outsideParkInfoDAO.getByParkidAndDate(parkId, date);
	}

}

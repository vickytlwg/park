package com.park.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.dao.PosdataDAO;
import com.park.model.Posdata;
import com.park.service.PosdataService;
@Service
public class PosdataServiceImpl implements PosdataService {
	@Autowired
	private PosdataDAO posdataDAO;
	@Override
	public int insert(Posdata record) {
		// TODO Auto-generated method stub
		return posdataDAO.insert(record);
	}
	@Override
	public List<Posdata> selectAll() {
		// TODO Auto-generated method stub
		return posdataDAO.selectAll();
	}
	@Override
	public List<Posdata> selectPosdataByPage(int low, int count) {
		// TODO Auto-generated method stub
		return posdataDAO.selectPosdataByPage(low, count);
	}
	@Override
	public int getPosdataCount() {
		// TODO Auto-generated method stub
		return posdataDAO.getPosdataCount();
	}
	
	@Override
	public Map<String, Object> getCarportCharge(int carportId, Date startDay, Date endDay) {
		List<Posdata> carportData = getCarportData(carportId, startDay, endDay);
		return null;
	}
	
	private List<Posdata> getCarportData(int carportId, Date startDay, Date endDay){
		return posdataDAO.getCarportData(carportId, startDay, endDay);
		
	}
	
	private List<Posdata> getParkData(int parkId, Date startDay, Date endDay){
		return posdataDAO.getParkData(parkId, startDay, endDay);
	}
	
	@Override
	public Map<String, Object> getParkCharge(int parkId, Date startDay, Date endDay) {
		List<Posdata> parkData = getParkData(parkId, startDay, endDay);
		return null;
	}
	@Override
	public List<Posdata> selectPosdataByCarportAndRange(String parkName, Date startDay, Date endDay) {
		// TODO Auto-generated method stub
		return posdataDAO.selectPosdataByCarportAndRange(parkName, startDay, endDay);
	}

}

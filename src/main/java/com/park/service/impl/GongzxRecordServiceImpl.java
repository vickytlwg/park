package com.park.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.park.dao.GongzxRecordDao;
import com.park.model.GongzxRecord;
import com.park.model.Park;
import com.park.model.PosChargeData;
import com.park.service.GongzxRecordService;
import com.park.service.ParkService;

@Transactional
@Service
public class GongzxRecordServiceImpl implements GongzxRecordService{
	@Autowired
	private GongzxRecordDao gongzxrecorddao;
	
	@Autowired
	ParkService parkService;

	@Override
	public int gongcount() {
		// TODO Auto-generated method stub
		return gongzxrecorddao.gongcount();
	}
	
	@Override
	public List<GongzxRecord> getPageByParkId(int parkId, int start, int count) {
		// TODO Auto-generated method stub
		return gongzxrecorddao.getPageByParkId(parkId, start, count);
	}

	@Override
	public List<GongzxRecord> getByParkAuthority(String userName) {
		// TODO Auto-generated method stub
		List<Park> parkList = parkService.getParks();
		if (userName != null)
			parkList = parkService.filterPark(parkList, userName);
		int num = 120 / parkList.size();
		if (num < 1) {
			num = 2;
		}
		List<GongzxRecord> gongzxRecord = new ArrayList<GongzxRecord>();
		for (Park park : parkList) {
			List<GongzxRecord> tmgongzxRecord = getPageByParkId(park.getId(), 0, num);
			gongzxRecord.addAll(tmgongzxRecord);
		}
		return gongzxRecord;
	}


	@Override
	public List<GongzxRecord> getByCarNumber(String carNumber) {
		// TODO Auto-generated method stub
		return gongzxrecorddao.getByCarNumber(carNumber);
	}

	@Override
	public List<GongzxRecord> getByCarNumberAndPark(String carNumber, int parkId) {
		// TODO Auto-generated method stub
		return gongzxrecorddao.getByCarNumberAndPark(carNumber, parkId);
	}

	@Override
	public List<GongzxRecord> getByParkName(String parkName) {
		// TODO Auto-generated method stub
		return gongzxrecorddao.getByParkName(parkName);
	}

	@Override
	public int insert(GongzxRecord record) {
		// TODO Auto-generated method stub
		return gongzxrecorddao.insert(record);
	}

	@Override
	public int update(GongzxRecord record) {
		// TODO Auto-generated method stub
		return gongzxrecorddao.update(record);
	}

}

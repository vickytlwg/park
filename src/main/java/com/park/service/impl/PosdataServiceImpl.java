package com.park.service.impl;

import java.util.List;

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

}

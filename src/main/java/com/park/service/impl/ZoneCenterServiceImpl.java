package com.park.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.park.dao.ZonecenterDAO;
import com.park.model.Zonecenter;
import com.park.service.ZoneCenterService;
@Transactional
@Service
public class ZoneCenterServiceImpl implements ZoneCenterService {

	@Autowired
	private ZonecenterDAO zoneCenterDao;
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return zoneCenterDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Zonecenter record) {
		// TODO Auto-generated method stub
		return zoneCenterDao.insert(record);
	}

	@Override
	public int insertSelective(Zonecenter record) {
		// TODO Auto-generated method stub
		return zoneCenterDao.insertSelective(record);
	}

	@Override
	public Zonecenter selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return zoneCenterDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Zonecenter record) {
		// TODO Auto-generated method stub
		return zoneCenterDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Zonecenter record) {
		// TODO Auto-generated method stub
		return zoneCenterDao.updateByPrimaryKey(record);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return zoneCenterDao.getCount();
	}

	@Override
	public List<Zonecenter> getByStartAndCount(int start, int count) {
		// TODO Auto-generated method stub
		return zoneCenterDao.getByStartAndCount(start, count);
	}

}

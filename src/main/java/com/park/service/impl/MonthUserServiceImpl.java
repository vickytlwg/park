package com.park.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.dao.MonthuserDAO;
import com.park.model.Monthuser;
import com.park.service.MonthUserService;
@Service
public class MonthUserServiceImpl implements MonthUserService {

	@Autowired
	private MonthuserDAO monthUserDao;
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return monthUserDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Monthuser record) {
		// TODO Auto-generated method stub
		return monthUserDao.insert(record);
	}

	@Override
	public int insertSelective(Monthuser record) {
		// TODO Auto-generated method stub
		return monthUserDao.insertSelective(record);
	}

	@Override
	public Monthuser selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return monthUserDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(Monthuser record) {
		// TODO Auto-generated method stub
		return monthUserDao.updateByPrimaryKey(record);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return monthUserDao.getCount();
	}

	@Override
	public List<Monthuser> getByStartAndCount(int start, int count) {
		// TODO Auto-generated method stub
		return monthUserDao.getByStartAndCount(start, count);
	}

}

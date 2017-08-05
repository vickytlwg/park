package com.park.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.dao.AlipayrecordMapper;
import com.park.model.Alipayrecord;
@Service
public class AlipayrecordService implements com.park.service.AlipayrecordService {

	@Autowired
	AlipayrecordMapper alipayrecordMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return alipayrecordMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Alipayrecord record) {
		// TODO Auto-generated method stub
		return alipayrecordMapper.insert(record);
	}

	@Override
	public int insertSelective(Alipayrecord record) {
		// TODO Auto-generated method stub
		return alipayrecordMapper.insertSelective(record);
	}

	@Override
	public Alipayrecord selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return alipayrecordMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Alipayrecord record) {
		// TODO Auto-generated method stub
		return alipayrecordMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Alipayrecord record) {
		// TODO Auto-generated method stub
		return alipayrecordMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<Alipayrecord> getByOutTradeNO(String outTradeNO) {
		// TODO Auto-generated method stub
		return alipayrecordMapper.getByOutTradeNO(outTradeNO);
	}

	@Override
	public List<Alipayrecord> getByPosChargeId(int poschargeId) {
		// TODO Auto-generated method stub
		return alipayrecordMapper.getByPosChargeId(poschargeId);
	}

}

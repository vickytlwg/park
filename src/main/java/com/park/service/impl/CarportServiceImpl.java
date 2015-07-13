package com.park.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.park.dao.CarportDAO;
import com.park.model.Carport;
import com.park.service.CarportService;

@Transactional
@Service
public class CarportServiceImpl implements CarportService{

	@Autowired
	private CarportDAO carportDAO;
	
	@Override
	public int insertCarport(Carport carportItem) {
		
		return carportDAO.insertCarport(carportItem);
	}
//
	@Override
	public Carport getCarportById(int id) {
		
		return carportDAO.getCarportById(id);
	}

	@Override
	public List<Carport> getCarports() {
		
		List<Carport> carports =  carportDAO.getCarports();
		return carports;
	}
	
	@Override
	public List<Carport> getSpecifyCarports(int start, int counts, String field, String order) {
		
		List<Carport> carports = carportDAO.getSpecifyCarports(start, counts, field, order);
		return carports;
	}
	@Override
	public List<Carport> getConditionCarports(int start, int counts,
			String field, String order, String queryCondition) {
		return carportDAO.getConditionCarports(start, counts, field, order, queryCondition);
		
	}

}

package com.park.service;
import java.util.List;

import com.park.model.Carport;

public interface CarportService {
	
	/**
	 * get carport by id
	 * @param id
	 * @return
	 */
	public Carport getCarportById(int id);
	
	/**
	 * get all carports
	 * @return
	 */
	List<Carport> getCarports();
	
	List<Carport> getSpecifyCarports(int low, int high, String field, String order);
	
	/**
	 * 添加车位
	 * @param carport
	 * @return 
	 */	
	public int insertCarport(Carport carportItem);
}

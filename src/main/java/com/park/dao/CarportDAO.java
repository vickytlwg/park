package com.park.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.park.model.Carport;

@Repository
public interface CarportDAO {
	
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
	
	public List<Carport> getSpecifyCarports(@Param("low")int low, @Param("high")int high, @Param("field")String field, @Param("order")String order);
	
	/**
	 * 添加车位
	 * @param carport
	 * @return 
	 */	
	public int insertCarport(Carport carportItem);
}

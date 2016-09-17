package com.park.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.park.model.PosChargeData;

@Repository
public interface PosChargeDataDAO {
	
	public PosChargeData getById(int id);

	public List<PosChargeData> get();
	
	public List<PosChargeData> getPage(int low, int count);
	
	public int count();
	
	public int insert(PosChargeData item);
	
	public int update(PosChargeData item);
	
	public List<PosChargeData> getDebt(String cardNumber);

	public List<PosChargeData> getUnCompleted();
	
	public List<PosChargeData> selectPosdataByParkAndRange(@Param("startDay") Date startDay, @Param("endDay") Date endDay,@Param("parkId")int parkId);
	
}

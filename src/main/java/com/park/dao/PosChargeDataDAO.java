package com.park.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.park.model.PosChargeData;

@Repository
public interface PosChargeDataDAO {
	
	//收费统计
	public int getByDateAndParkCount(@Param("parkId")int parkId,@Param("startDate")String startDate,@Param("endDate")String endDate);
	
	public double getChannelCharge(@Param("parkId")int parkId,@Param("startDate")String startDate,@Param("endDate")String endDate);
	
	public PosChargeData getById(int id);

	public List<PosChargeData> get();
	
	public List<PosChargeData> getPage(int low, int count);
	
	public List<PosChargeData> getByParkIdAndCardNumber(@Param("parkId")int parkId,@Param("cardNumber")String cardNumber);
	
	public List<PosChargeData> getByCardNumberAndPark(@Param("cardNumber")String cardNumber,@Param("parkId")int parkId);
	
	public List<PosChargeData> getPageByParkId(@Param("parkId")int parkId,@Param("start")int start,@Param("count")int count);
	
	public List<PosChargeData> getPageArrearage(int low, int count);
	
	public List<PosChargeData> getPageArrearageByParkId(@Param("parkId")int parkId,@Param("start")int start,@Param("count")int count);
	
	public List<PosChargeData> getByRange(@Param("parkId")int parkId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	public List<PosChargeData> getAllByDay(@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	public List<PosChargeData> getParkCarportStatusToday(@Param("parkId")int parkId,@Param("day")String day);
	
	public int count();
	
	public int deleteById(@Param("id")int id);
	
	public int insert(PosChargeData item);
	
	public int update(PosChargeData item);
	
	public List<PosChargeData> getDebt(String cardNumber);
	
	public List<PosChargeData> getDebtWithParkId(@Param("carNumber")String carNumber,@Param("parkId")int parkId);

	public List<PosChargeData> getLastRecord(@Param("carNumber")String carNumber,@Param("count")int count);
	
	public List<PosChargeData> getLastRecordWithPark(@Param("carNumber")String carNumber,@Param("count")int count,@Param("parkId")int parkId);
	
	public List<PosChargeData> getArrearageByCardNumber(String cardNumber);

	public List<PosChargeData> getUnCompleted();
	
	public List<PosChargeData> selectPosdataByParkAndRangeAndCarportNumber(@Param("startDay") Date startDay, @Param("endDay") Date endDay,@Param("parkId")int parkId,@Param("carportNumber")int carportNumber);
	
	public List<PosChargeData> selectPosdataByParkAndRange(@Param("startDay") Date startDay, @Param("endDay") Date endDay,@Param("parkId")int parkId);
	
	public int deleteByParkIdAndDate(@Param("startDay") String startDay, @Param("endDay") String endDay,@Param("parkId")int parkId);
	
	public List<PosChargeData> getByCardNumber(@Param("cardNumber")String cardNumber);
	
	public List<PosChargeData> getByCardNumberAndPort(@Param("cardNumber")String cardNumber,@Param("portNumber")Integer portNumber);
	
	public List<PosChargeData> getByParkName(@Param("parkName")String parkName);
	
	public Map<String, Object> calInByParkAndRange(@Param("parkId")int parkId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	public Map<String, Object> calOutByParkAndRange(@Param("parkId")int parkId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	public Map<String, Object> selectPosdataSumByParkAndRange(@Param("startDay") Date startDay, @Param("endDay") Date endDay,@Param("parkId")int parkId);
	
	public List<Map<String, Object>> getFeeOperatorChargeData(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

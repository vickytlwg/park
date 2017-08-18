package com.park.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.park.model.Outsideparkinfo;
import com.park.model.PosChargeData;

public interface PosChargeDataService {
	
	public PosChargeData getById(int id);		
	
	public List<PosChargeData> get();
	
	public List<PosChargeData> getUnCompleted();
	
	public List<PosChargeData> getPage(int low, int count);
	
	public List<PosChargeData> getByParkIdAndCardNumber(Integer parkId,String cardNumber);
	
	public Integer deleteByParkIdAndDate(int parkId, String startDate,String endDate);
	
	public List<PosChargeData> getPageByParkId(int parkId,int start,int count);
	
	public List<PosChargeData> getByParkAuthority(String userName);
	
	public List<PosChargeData> getPageArrearage(int low, int count);
	
	public List<PosChargeData> getPageArrearageByParkId(int parkId,int start,int count);
	
	public List<PosChargeData> getByRange(int parkId,Date startDate,Date endDate);
	
	public List<PosChargeData> getAllByDay(String date) throws ParseException;
	
	public List<PosChargeData> getByParkAndDay(int parkId,String date) throws ParseException;
	
	public List<PosChargeData> getDebt (String cardNumber) throws Exception;
	
	public List<PosChargeData> getDebt (String cardNumber,Date exitDate) throws Exception;
	
	public List<PosChargeData> getArrearageByCardNumber(String cardNumber);
	
	public List<PosChargeData> queryDebt (String cardNumber,Date exitDate) throws Exception;
	
	public List<PosChargeData> queryCurrentDebt (String cardNumber,Date exitDate) throws Exception;
	
	public List<PosChargeData> getParkCarportStatusToday(int parkId,Date tmpdate);
	
	public int count();
	
	public int deleteById(int id);
	
	public int insert(PosChargeData item);
	
	public int update(PosChargeData item);
		
	public PosChargeData pay(String cardNumber, double money) throws Exception;
	
	public PosChargeData updateRejectReason(String cardNumber,String rejectReason) throws Exception;
	
	public List<PosChargeData> repay(String cardNumber, double money) throws Exception;
	
	public void calExpense(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception;
	
	public void calExpenseSmallCar(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception;
	
	public void calExpenseLargeCar(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception;
	
	public List<PosChargeData> selectPosdataByParkAndRange(Date startDay, Date endDay,int parkId);
	
	public List<PosChargeData> selectPosdataByParkAndRangeAndCarportNumber(Date startDay, Date endDay,int parkId,int carportNumber);
	
	public Map<String, Object> getParkChargeByDay(int parkId, String day);
	
	public List<PosChargeData> getByCardNumber(String cardNumber);
	
	public List<PosChargeData> getByParkName(String parkName);
	
	public List<PosChargeData> hardwareRecord(Integer parkId,String startDate,String endDate) throws ParseException, Exception;
	
	public List<Map<String, Object>> getFeeOperatorChargeData(Date startDate,Date endDate);

	List<PosChargeData> getByParkAndDayRange(int parkId, String startDate,String endDate) throws ParseException;

	List<PosChargeData> getAllByDayRange(String startDate, String endDate) throws ParseException;
	
	public Outsideparkinfo getOutsideparkinfoByOrigin(int parkId,String day);

	List<PosChargeData> getCharges(String cardNumber) throws Exception;
	
	public List<PosChargeData> getByCardNumberAndPort(String cardNumber,Integer portNumber);

}

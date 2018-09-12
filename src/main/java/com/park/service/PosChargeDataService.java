package com.park.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.park.model.FeeCriterion;
import com.park.model.Monthuser;
import com.park.model.Outsideparkinfo;
import com.park.model.Park;
import com.park.model.Parktoalipark;
import com.park.model.PosChargeData;

public interface PosChargeDataService {
	
	public List<Park> getParkByMoney(Map<String, Object> map);
	
	//查询收费总笔数、收费总金额、各渠道收费统计
	public String getByDateAndParkCount2(int parkId,String startDate,String endDate);
	public String getByDateAndParkCount4(int parkId,String startDate,String endDate);
	//各渠道收费统计
	public String getByDateAndParkCount(int parkId,String startDate,String endDate,int payType);
	public String getByDateAndParkCount3(int parkId,String startDate,String endDate,int payType);
	//收费统计
	/*public int getByDateAndParkCount(int parkId,String startDate,String endDate);
	
	public double getChannelCharge(int parkId,String startDate,String endDate);*/
	
	public PosChargeData getById(int id);		
	
	public List<PosChargeData> get();
	
	public List<PosChargeData> getUnCompleted();
	
	public List<PosChargeData> getPage(int low, int count);
	
	public List<PosChargeData> getByParkIdAndCardNumber(Integer parkId,String cardNumber);
	
	public Integer deleteByParkIdAndDate(int parkId, String startDate,String endDate);
	
	public List<PosChargeData> getPageByParkId(int parkId,int start,int count);
	
	public List<PosChargeData> getByParkAuthority(String userName);
	
	public List<PosChargeData> getPageArrearage(int low, int count);
	
	public List<PosChargeData> getByCardNumberAndPark(String cardNumber,Integer parkId);
	
	public List<PosChargeData> getPageArrearageByParkId(int parkId,int start,int count);
	
	public List<PosChargeData> getByRange(int parkId,Date startDate,Date endDate);
	
	public List<PosChargeData> getAllByDay(String date) throws ParseException;
	
	public List<PosChargeData> getByParkAndDay(int parkId,String date) throws ParseException;
	
	public List<PosChargeData> getDebt (String cardNumber) throws Exception;
	
	public List<PosChargeData> getDebtWithData(String cardNumber,List<Parktoalipark> parktoaliparks,List<Monthuser> monthusers,Park park,Boolean isMultiFeeCtriterion,int carType) throws Exception;

	public void calExpenseSmallCarWithData(PosChargeData charge, Date exitDate,Boolean isQuery,Park park,FeeCriterion criterion );
	
	public void calExpenseLargeCarWithData(PosChargeData charge, Date exitDate,Boolean isQuery,Park park,FeeCriterion criterion );
	
	public List<PosChargeData> getLastRecord(String carNumber,int count);
	
	public List<PosChargeData> getLastRecordWithPark(String carNumber,int count,int parkId);
	
	public List<PosChargeData> getDebt (String cardNumber,Date exitDate) throws Exception;
	
	public List<PosChargeData> getArrearageByCardNumber(String cardNumber);
	
	public List<PosChargeData> queryDebt (String cardNumber,Date exitDate) throws Exception;
	
	public PosChargeData newFeeCalcExpense(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception;
	
	public PosChargeData newFeeCalcExpenseWithData(PosChargeData charge, Date exitDate,Boolean isQuery,Park park,FeeCriterion criterion) throws Exception;;
	
	public PosChargeData newFeeCalcExpense1(PosChargeData charge,FeeCriterion criterion, Date exitDate,Boolean isQuery) throws Exception;
	
	public PosChargeData newFeeCalcExpense2(PosChargeData charge, FeeCriterion criterion,Date exitDate,Boolean isQuery) throws Exception;
	
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
	
	public void calExpensewithData(PosChargeData charge, Date exitDate,Boolean isQuery,List<Monthuser> monthusers,Park park,FeeCriterion criterion) throws Exception;
	
	public void calExpenseType1(PosChargeData charge, Date exitDate,Boolean isQuery) throws ParseException;
	
	public void calExpenseSmallCar(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception;
	
	public void calExpenseLargeCar(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception;
	
	public List<PosChargeData> selectPosdataByParkAndRange(Date startDay, Date endDay,int parkId);
	
	public List<PosChargeData> selectPosdataByParkAndRangeAndCarportNumber(Date startDay, Date endDay,int parkId,int carportNumber);
	
	public Map<String, Object> getParkChargeByDay(int parkId, String day);
	
	public Map<String, Object> selectPosdataSumByParkAndRange(Date startDay, Date endDay,int parkId);
	
	public Map<String, Object> getParkChargeCountByDay(int parkId, String day);
	
	public Map<String, Object> calInByParkAndRange(int parkId, Date startDate, Date endDate);
	
	public Map<String, Object> calOutByParkAndRange(int parkId, Date startDate, Date endDate);
	
	public List<PosChargeData> getByCardNumber(String cardNumber);
	
	public List<PosChargeData> getByParkName(String parkName);
	
	public List<PosChargeData> hardwareRecord(Integer parkId,String startDate,String endDate) throws ParseException, Exception;
	
	public List<Map<String, Object>> getFeeOperatorChargeData(Date startDate,Date endDate);

	List<PosChargeData> getByParkAndDayRange(int parkId, String startDate,String endDate) throws ParseException;

	List<PosChargeData> getAllByDayRange(String startDate, String endDate) throws ParseException;
	
	public Outsideparkinfo getOutsideparkinfoByOrigin(int parkId,String day);

	List<PosChargeData> getCharges(String cardNumber) throws Exception;
	
	public List<PosChargeData> getByCardNumberAndPort(String cardNumber,Integer portNumber);
	void calExpenseMulti(PosChargeData charge, Date exitDate, Boolean isQuery, Boolean isMultiFeeCtriterion, int carType)
			throws Exception;
	PosChargeData payWithOperatorId(String cardNumber, double money, String operatorId) throws Exception;
	List<PosChargeData> queryDebtWithParkId(String cardNumber, Date exitDate, Integer parkId) throws Exception;
	
	PosChargeData newFeeCalcExpense4(PosChargeData charge, FeeCriterion criterion, Date exitDate, Boolean isQuery,
			Park park) throws Exception;
	
	

}

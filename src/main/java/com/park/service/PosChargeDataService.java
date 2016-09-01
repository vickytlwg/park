package com.park.service;

import java.util.Date;
import java.util.List;

import com.park.model.PosChargeData;

public interface PosChargeDataService {
	
	public PosChargeData getById(int id);
	
	public List<PosChargeData> get();
	
	public List<PosChargeData> getUnCompleted();
	
	public List<PosChargeData> getPage(int low, int count);
	
	public List<PosChargeData> getDebt (String cardNumber) throws Exception;
	
	public List<PosChargeData> getDebt (String cardNumber,Date exitDate) throws Exception;
	
	public int count();
	
	public int insert(PosChargeData item);
	
	public int update(PosChargeData item);
		
	public List<PosChargeData> pay(String cardNumber, double money) throws Exception;
	
	public void calExpense(PosChargeData charge, Date exitDate) throws Exception;
	
	public void calExpenseSmallCar(PosChargeData charge, Date exitDate) throws Exception;
	
	public void calExpenseLargeCar(PosChargeData charge, Date exitDate) throws Exception;
	
}

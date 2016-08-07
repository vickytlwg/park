package com.park.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.park.dao.PosChargeDataDAO;
import com.park.model.FeeCriterion;
import com.park.model.Park;
import com.park.model.PosChargeData;
import com.park.service.FeeCriterionService;
import com.park.service.ParkService;
import com.park.service.PosChargeDataService;

@Transactional
@Service
public class PosChargeDataServiceImpl implements PosChargeDataService{

	@Autowired
	PosChargeDataDAO chargeDao;
	
	@Autowired
	ParkService parkService;
	
	@Autowired
	FeeCriterionService criterionService;
	
	@Override
	public PosChargeData getById(int id){
		return chargeDao.getById(id);
	}
	
	@Override
	public List<PosChargeData> get() {
		return chargeDao.get();
	}

	@Override
	public List<PosChargeData> getUnCompleted() {
		return chargeDao.getUnCompleted();
	}
	
	@Override
	public List<PosChargeData> getPage(int low, int count) {
		return chargeDao.getPage(low, count);
	}

	@Override
	public int count() {
		return chargeDao.count();
	}

	@Override
	public int insert(PosChargeData item) {
		return chargeDao.insert(item);
	}

	@Override
	public int update(PosChargeData item) {
		return chargeDao.update(item);
	}

	@Override
	public List<PosChargeData> getDebt(String cardNumber) throws Exception {
		List<PosChargeData> charges =  chargeDao.getDebt(cardNumber);
		for(PosChargeData charge : charges){
			if(charge.getExitDate() == null){
				this.calExpense(charge, new Date());
			}		
		}
		return charges;
	}

	@Override
	public List<PosChargeData> pay(String cardNumber, double money) throws Exception {
		
		List<PosChargeData> charges = this.getDebt(cardNumber);
		for(PosChargeData charge : charges){
			if(money > charge.getUnPaidMoney()){
				money -= charge.getUnPaidMoney();
				charge.setPaidCompleted(true);
				this.update(charge);
			}
		}
		if(money > 0){
			int count = charges.size();
			PosChargeData lastCharge = charges.get(count -1);
			lastCharge.setChangeMoney(lastCharge.getChangeMoney() + money);
			this.update(lastCharge);
		}
		return charges;
				
	}
	
	@Override
	public void calExpense(PosChargeData charge, Date exitDate) throws Exception{
		if(charge.getExitDate() != null)
			return;
		Park park = parkService.getParkById(charge.getParkId());
		
		Integer criterionId = park.getFeeCriterionId();
		
		if(criterionId == null)
			throw new Exception("no fee criterion");
		
		FeeCriterion criterion = criterionService.getById(criterionId);
		
		Date enterDate = charge.getEntranceDate();
		Date now = new Date();
		if(enterDate.getDay() < now.getDay()){
			int nightHour = Integer.parseInt(criterion.getNightStartTime().split(":")[0]);
			Calendar cld = Calendar.getInstance();
			cld.setTime(enterDate);
			cld.set(Calendar.HOUR_OF_DAY, nightHour);
			charge.setExitDate(cld.getTime());
		}else{
			charge.setExitDate(new Date());
		}
			
		
		
		double expense = 0;
		if(charge.getIsOneTimeExpense() == 1){
			expense = criterion.getOneTimeExpense() - charge.getPaidMoney();
			
		}else{
			long diffMin = (charge.getExitDate().getTime() - charge.getEntranceDate().getTime())/(1000 * 60);
			long intervals = 0;
			if(diffMin > criterion.getFreeMins())
				intervals= (diffMin - criterion.getFreeMins()) / criterion.getTimeoutPriceInterval();
			expense = intervals * criterion.getTimeoutPriceInterval() + criterion.getStartExpense();			
		}
		
		charge.setChangeMoney(expense);
		
		if(expense > criterion.getMaxExpense())
			expense = criterion.getMaxExpense();
		
		expense -= charge.getPaidMoney();
		if(expense > 0.01){
			charge.setUnPaidMoney(expense);				
		}
		if(expense < -0.01){
			charge.setUnPaidMoney(0);
			charge.setChangeMoney(-1 * expense);
		}
		
		this.update(charge);
		
	}



}

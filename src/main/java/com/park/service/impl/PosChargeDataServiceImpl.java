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
public class PosChargeDataServiceImpl implements PosChargeDataService {

	@Autowired
	PosChargeDataDAO chargeDao;

	@Autowired
	ParkService parkService;

	@Autowired
	FeeCriterionService criterionService;

	@Override
	public PosChargeData getById(int id) {
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
		List<PosChargeData> charges = chargeDao.getDebt(cardNumber);
		for (PosChargeData charge : charges) {
			if (charge.getExitDate() == null) {
				this.calExpense(charge, new Date(),false);
			}
		}
		return charges;
	}

	@Override
	public List<PosChargeData> pay(String cardNumber, double money) throws Exception {

		List<PosChargeData> charges = this.getDebt(cardNumber);
		for (PosChargeData charge : charges) {
			if (money >= charge.getUnPaidMoney()) {
				money -= charge.getUnPaidMoney();
				charge.setPaidCompleted(true);
				this.update(charge);
			}
		}
		if (money >= 0) {
			int count = charges.size();
			PosChargeData lastCharge = charges.get(count - 1);
			lastCharge.setChangeMoney(lastCharge.getChangeMoney() + money);
			this.update(lastCharge);
		}
		return charges;

	}

	@Override
	public void calExpense(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception {
		if (charge.getIsLargeCar() == false) {
			this.calExpenseSmallCar(charge, exitDate,isQuery);
		} else {
			this.calExpenseLargeCar(charge, exitDate,isQuery);
		}

	}

	@Override
	public List<PosChargeData> getDebt(String cardNumber, Date exitDate) throws Exception {
		// TODO Auto-generated method stub
		List<PosChargeData> charges = chargeDao.getDebt(cardNumber);
		for (PosChargeData charge : charges) {
			if (charge.getExitDate() == null) {
				this.calExpense(charge, exitDate,false);
			}
		}
		return charges;
	}

	@Override
	public void calExpenseLargeCar(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception {
		// TODO Auto-generated method stub
		if (charge.getExitDate() != null)
			return;
		Park park = parkService.getParkById(charge.getParkId());

		Integer criterionId = park.getFeeCriterionId();

		if (criterionId == null)
			throw new Exception("no fee criterion");
		FeeCriterion criterion = criterionService.getById(criterionId);
		Date enterDate = charge.getEntranceDate();
		if (enterDate.getDay() < exitDate.getDay() || enterDate.getMonth() != exitDate.getMonth()) {
			int nightHour = Integer.parseInt(criterion.getNightstarttime().split(":")[0]);
			Calendar cld = Calendar.getInstance();
			cld.setTime(enterDate);
			cld.set(Calendar.HOUR_OF_DAY, nightHour);
			charge.setExitDate(cld.getTime());
		} else {
			charge.setExitDate(exitDate);
		}

		double expense = 0;
		if (charge.getIsOneTimeExpense() == 1) {
			expense = criterion.getOnetimeexpense() - charge.getPaidMoney();

		} else {
			float diffMin = (charge.getExitDate().getTime() - charge.getEntranceDate().getTime()) / (1000 * 60f);
			float firstHour = criterion.getStep1capacity();
			if (diffMin > criterion.getFreemins()) {
				if (diffMin <= firstHour) {
					double intervals = Math
							.ceil((diffMin - criterion.getFreemins()) / criterion.getTimeoutpriceinterval());
					expense = intervals * criterion.getStep1pricelarge();
				} else {
					double intervals1 = Math
							.ceil((firstHour - criterion.getFreemins()) / criterion.getTimeoutpriceinterval());
					expense = intervals1 * criterion.getStep1pricelarge();
					double intervals2 = Math.ceil((diffMin - firstHour) / criterion.getTimeoutpriceinterval());
					expense += intervals2 * criterion.getStep2pricelarge();
				}
			}
		}

		if (expense > criterion.getMaxexpense())
			expense = criterion.getMaxexpense();

		charge.setChargeMoney(expense);
		if (expense == 0) {
			charge.setPaidCompleted(true);
		}
		expense -= charge.getPaidMoney();
		if (expense > 0.01) {
			charge.setUnPaidMoney(expense);
		}
		if (expense < -0.01) {
			charge.setUnPaidMoney(0);
			charge.setPaidCompleted(true);
			charge.setChangeMoney(-1 * expense);
		}

		if (!isQuery) {
			this.update(charge);
		}
	}

	@Override
	public void calExpenseSmallCar(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception {

		if (charge.getExitDate() != null)
			return;
		Park park = parkService.getParkById(charge.getParkId());

		Integer criterionId = park.getFeeCriterionId();

		if (criterionId == null)
			throw new Exception("no fee criterion");

		FeeCriterion criterion = criterionService.getById(criterionId);

		Date enterDate = charge.getEntranceDate();

		if (enterDate.getDay() < exitDate.getDay() || enterDate.getMonth() != exitDate.getMonth()) {
			int nightHour = Integer.parseInt(criterion.getNightstarttime().split(":")[0]);
			Calendar cld = Calendar.getInstance();
			cld.setTime(enterDate);
			cld.set(Calendar.HOUR_OF_DAY, nightHour);
			charge.setExitDate(cld.getTime());
		} else {
			charge.setExitDate(exitDate);
		}
		double expense = 0;
		if (charge.getIsOneTimeExpense() == 1) {
			expense = criterion.getOnetimeexpense() - charge.getPaidMoney();
		} else {
			float diffMin = (charge.getExitDate().getTime() - charge.getEntranceDate().getTime()) / (1000 * 60f);
			float firstHour = criterion.getStep1capacity();

			if (diffMin > criterion.getFreemins()) {
				if (diffMin <= firstHour) {
					double intervals = Math
							.ceil((diffMin - criterion.getFreemins()) / criterion.getTimeoutpriceinterval());
					expense = intervals * criterion.getStep1price();
				} else {
					double intervals1 = Math
							.ceil((firstHour - criterion.getFreemins()) / criterion.getTimeoutpriceinterval());
					expense = intervals1 * criterion.getStep1price();
					double intervals2 = Math.ceil((diffMin - firstHour) / criterion.getTimeoutpriceinterval());
					expense += intervals2 * criterion.getStep2price();
				}
			}
		}
		if (expense > criterion.getMaxexpense())
			expense = criterion.getMaxexpense();
		charge.setChargeMoney(expense);
		if (expense == 0) {
			charge.setPaidCompleted(true);
		}
		expense -= charge.getPaidMoney();
		if (expense > 0) {
			charge.setUnPaidMoney(expense);
		}
		else {
			charge.setUnPaidMoney(0);
			charge.setPaidCompleted(true);
			charge.setChangeMoney(-1 * expense);
		}
		if (!isQuery) {
			this.update(charge);
		}
		
	}

	@Override
	public List<PosChargeData> queryDebt(String cardNumber, Date exitDate) throws Exception {
		// TODO Auto-generated method stub
		List<PosChargeData> charges = chargeDao.getDebt(cardNumber);
		for (PosChargeData charge : charges) {
			if (charge.getExitDate() == null) {
				this.calExpense(charge, exitDate,true);
			}
		}
		return charges;
	}

}

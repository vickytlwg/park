package com.park.service.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.park.dao.PosChargeDataDAO;
import com.park.dao.PosdataDAO;
import com.park.model.Constants;
import com.park.model.FeeCriterion;
import com.park.model.Outsideparkinfo;
import com.park.model.Park;
import com.park.model.PosChargeData;
import com.park.model.Posdata;
import com.park.service.FeeCriterionService;
import com.park.service.OutsideParkInfoService;
import com.park.service.ParkService;
import com.park.service.PosChargeDataService;
import com.park.service.PosdataService;

@Transactional
@Service
public class PosChargeDataServiceImpl implements PosChargeDataService {
	
	

	@Autowired
	PosChargeDataDAO chargeDao;

	@Autowired
	ParkService parkService;
	
	@Autowired
	FormatTime formatTime;
	
	@Autowired
	private PosdataService chargeSerivce;
		
	@Autowired
	FeeCriterionService criterionService;
	
	@Autowired
	PosdataService posdataService;
	
	@Autowired
	private OutsideParkInfoService outsideParkInfoService;
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
	public List<PosChargeData> getCharges(String cardNumber) throws Exception {
		List<PosChargeData> charges = chargeDao.getDebt(cardNumber);
		
		for (PosChargeData tmpcharge:charges){
			if (tmpcharge.getExitDate() == null) {
			this.calExpense(tmpcharge, new Date(),false);}
		}
		return charges;
	}
	@Override
	public List<PosChargeData> getDebt(String cardNumber) throws Exception {
		List<PosChargeData> charges = chargeDao.getDebt(cardNumber);
		List<PosChargeData> tmPosChargeDatas = new ArrayList<>();
		for (PosChargeData charge : charges) {
			if (charge.getExitDate() == null) {
				tmPosChargeDatas.add(charge);				
			}
		}
		for (PosChargeData tmpcharge:tmPosChargeDatas){
			this.calExpense(tmpcharge, new Date(),false);
		}
		return tmPosChargeDatas;
	}

	@Override
	public PosChargeData pay(String cardNumber, double money) throws Exception {
		double theMoney=money;
		List<PosChargeData> charges = this.getCharges(cardNumber);
//		for (PosChargeData charge : charges) {
//			if (money >= charge.getUnPaidMoney()) {
//				money -= charge.getUnPaidMoney();
//				charge.setPaidCompleted(true);
//				this.update(charge);
//			}
//		}
		int count = charges.size();
		PosChargeData lastCharge = charges.get(0);
		money -= lastCharge.getUnPaidMoney();
		
		Outsideparkinfo outsideparkinfo=outsideParkInfoService.getByParkidAndDate(lastCharge.getParkId());
		if (money >= 0) {
			lastCharge.setGivenMoney(theMoney+lastCharge.getGivenMoney());
			lastCharge.setPaidCompleted(true);
			DecimalFormat df = new DecimalFormat("0.00"); 
			String data=df.format(lastCharge.getChangeMoney() + money);
			lastCharge.setChangeMoney(Double.parseDouble(data));				
			outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+lastCharge.getGivenMoney()-lastCharge.getChangeMoney()));
			outsideparkinfo.setPossigndate(new Date());
						
		}
		else {
			lastCharge.setGivenMoney(theMoney+lastCharge.getGivenMoney());
			lastCharge.setUnPaidMoney(lastCharge.getUnPaidMoney()-theMoney);
			outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+theMoney));
			outsideparkinfo.setPossigndate(new Date());
		}
		outsideParkInfoService.updateByPrimaryKeySelective(outsideparkinfo);
		this.update(lastCharge);
		return lastCharge;
	}

	@Override
	public void calExpense(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception {
		String startTime = new SimpleDateFormat(Constants.DATEFORMAT).format(charge.getEntranceDate());
		String endTime = new SimpleDateFormat(Constants.DATEFORMAT).format(exitDate);
		if (charge.getIsLargeCar() == false) {
			
			Map<String,String> dates=formatTime.format(startTime, endTime, "20", "7");
			for(String name:dates.keySet()){
				charge.setEntranceDate(name);
		//		charge.setExitDate(dates.get(name));
			  if (name.substring(11).equals("20:00:00")) {
				charge.setIsOneTimeExpense(1);
				this.calExpenseSmallCar(charge,new SimpleDateFormat(Constants.DATEFORMAT).parse(dates.get(name)),isQuery);
			}
			  else {
				  charge.setIsOneTimeExpense(0);
				  this.calExpenseSmallCar(charge,new SimpleDateFormat(Constants.DATEFORMAT).parse(dates.get(name)),isQuery);
			}
			}
			charge.setEntranceDate(startTime);
			charge.setExitDate(endTime);
			
			if (charge.getPaidMoney()>=charge.getChargeMoney() ) {
				charge.setUnPaidMoney(0);
				charge.setPaidCompleted(true);
				charge.setChangeMoney(charge.getPaidMoney()-charge.getChargeMoney());
			}
			else {
				charge.setUnPaidMoney(charge.getChargeMoney()-charge.getPaidMoney());
			}
			this.update(charge);
			
		} else {
			this.calExpenseLargeCar(charge, exitDate,isQuery);
		}
		
	}

	@Override
	public List<PosChargeData> getDebt(String cardNumber, Date exitDate) throws Exception {
		// TODO Auto-generated method stub
		List<PosChargeData> charges = chargeDao.getDebt(cardNumber);
		List<PosChargeData> tmPosChargeDatas = new ArrayList<>();
		for (PosChargeData charge : charges) {
			if (charge.getExitDate() == null) {
				tmPosChargeDatas.add(charge);				
			}
		}
		for (PosChargeData tmpcharge:tmPosChargeDatas){
			this.calExpense(tmpcharge, exitDate,false);
		}
		return tmPosChargeDatas;
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
			charge.setExitDate1(cld.getTime());
		} else {
			charge.setExitDate1(exitDate);
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
			Outsideparkinfo outsideparkinfo=outsideParkInfoService.getByParkidAndDate(charge.getParkId());
			outsideparkinfo.setUnusedcarportcount(outsideparkinfo.getUnusedcarportcount()+1);
			outsideparkinfo.setOutcount(outsideparkinfo.getOutcount()+1);
			outsideparkinfo.setAmountmoney((float) (outsideparkinfo.getAmountmoney()+charge.getChargeMoney()));
			outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+charge.getPaidMoney()));
			outsideparkinfo.setPossigndate(new Date());
			outsideParkInfoService.updateByPrimaryKeySelective(outsideparkinfo);
			this.update(charge);
		}
	}

	@Override
	public void calExpenseSmallCar(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception {

	//	if (charge.getExitDate() != null)
	//		return;
		Park park = parkService.getParkById(charge.getParkId());

		Integer criterionId = park.getFeeCriterionId();

		if (criterionId == null)
			throw new Exception("no fee criterion");

		FeeCriterion criterion = criterionService.getById(criterionId);

		Date enterDate = charge.getEntranceDate();

//		if (enterDate.getDay() < exitDate.getDay() || enterDate.getMonth() != exitDate.getMonth()) {
//			int nightHour = Integer.parseInt(criterion.getNightstarttime().split(":")[0]);
//			Calendar cld = Calendar.getInstance();
//			cld.setTime(enterDate);
//			cld.set(Calendar.HOUR_OF_DAY, nightHour);
//			charge.setExitDate1(cld.getTime());
//		} else {
//			charge.setExitDate1(exitDate);
//		}
		charge.setExitDate1(exitDate);
		double expense = 0;
		if (charge.getIsOneTimeExpense() == 1) {
			expense = criterion.getOnetimeexpense();
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
	//	if (expense > criterion.getMaxexpense())
	//		expense = criterion.getMaxexpense();
		charge.setChargeMoney(expense+charge.getChargeMoney());
		if (charge.getChargeMoney()>criterion.getMaxexpense()) {
			charge.setChargeMoney(criterion.getMaxexpense());
		}
//		if (charge.getChargeMoney() == 0) {
//			charge.setPaidCompleted(true);
//		}
//		expense -= charge.getPaidMoney();
//		if (expense > 0) {
//			charge.setUnPaidMoney(expense);
//		}
//		else {
//			charge.setUnPaidMoney(0);
//			charge.setPaidCompleted(true);
//			charge.setChangeMoney(-1 * expense);
//		}
		if (!isQuery) {
//			Outsideparkinfo outsideparkinfo=outsideParkInfoService.getByParkidAndDate(charge.getParkId());
//			outsideparkinfo.setUnusedcarportcount(outsideparkinfo.getUnusedcarportcount()+1);
//			outsideparkinfo.setOutcount(outsideparkinfo.getOutcount()+1);
//			outsideparkinfo.setAmountmoney((float) (outsideparkinfo.getAmountmoney()+charge.getChargeMoney()));
//			outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+charge.getPaidMoney()));
//			outsideparkinfo.setPossigndate(new Date());
//			outsideParkInfoService.updateByPrimaryKeySelective(outsideparkinfo);
	//		this.update(charge);
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

	@Override
	public List<PosChargeData> selectPosdataByParkAndRange(Date startDay, Date endDay, int parkId) {
		// TODO Auto-generated method stub
		return chargeDao.selectPosdataByParkAndRange(startDay, endDay, parkId);
	}

	@Override
	public Map<String, Object> getParkChargeByDay(int parkId, String day) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(day + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		Date parsedEndDay  = null;
		try {
			parsedEndDay = sdf.parse(day + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		List<PosChargeData> posChargeDatas=selectPosdataByParkAndRange(parsedStartDay, parsedEndDay, parkId);
		Map<String, Object> retmap=new HashMap<>();
		float chargeTotal=0;
		float realReceiveMoney=0;
		for(PosChargeData posData:posChargeDatas){
			chargeTotal+=posData.getChargeMoney();
			realReceiveMoney+=posData.getGivenMoney()+posData.getPaidMoney()-posData.getChangeMoney();
		}
		retmap.put("totalMoney", chargeTotal);
		retmap.put("realMoney", realReceiveMoney);
		return retmap;
	}

	@Override
	public List<PosChargeData> queryCurrentDebt(String cardNumber, Date exitDate) throws Exception {
		List<PosChargeData> charges = chargeDao.getDebt(cardNumber);
		List<PosChargeData> tmPosChargeDatas = new ArrayList<>();
		for (PosChargeData charge : charges) {
			if (charge.getExitDate() == null) {
				tmPosChargeDatas.add(charge);				
			}
		}
		for (PosChargeData tmpcharge:tmPosChargeDatas){
			this.calExpense(tmpcharge, exitDate,true);
		}
		return tmPosChargeDatas;
	}

	@Override
	public List<PosChargeData> getByCardNumber(String cardNumber) {
		// TODO Auto-generated method stub
		return chargeDao.getByCardNumber(cardNumber);
	}

	@Override
	public List<PosChargeData> getByParkName(String parkName) {
		// TODO Auto-generated method stub
		return chargeDao.getByParkName(parkName);
	}

	@Override
	public List<PosChargeData> repay(String cardNumber, double money) throws Exception {
		double theMoney=money;
		List<PosChargeData> charges = this.getCharges(cardNumber);
		for (PosChargeData charge : charges) {
			if (money >= charge.getUnPaidMoney()) {
				money -= charge.getUnPaidMoney();
				charge.setPaidCompleted(true);
				this.update(charge);
			}
		}

		if (money >= 0) {
			int count = charges.size();
			PosChargeData lastCharge = charges.get(0);
			lastCharge.setChangeMoney(lastCharge.getChangeMoney() + money);
			lastCharge.setGivenMoney(theMoney+lastCharge.getGivenMoney());
			Outsideparkinfo outsideparkinfo=outsideParkInfoService.getByParkidAndDate(lastCharge.getParkId());
			outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+lastCharge.getPaidMoney()+lastCharge.getGivenMoney()-lastCharge.getChangeMoney()));
			outsideparkinfo.setPossigndate(new Date());
			outsideParkInfoService.updateByPrimaryKeySelective(outsideparkinfo);
			this.update(lastCharge);
		}
		return charges;
	}

	@Override
	public List<Map<String, Object>> getFeeOperatorChargeData(Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return chargeDao.getFeeOperatorChargeData(startDate, endDate);
	}

	@Override
	public List<PosChargeData> getByRange(int parkId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return chargeDao.getByRange(parkId, startDate, endDate);
	}

	@Override
	public List<PosChargeData> getByParkAndDay(int parkId, String date) throws ParseException  {
		// TODO Auto-generated method stub
		SimpleDateFormat sFormat=new SimpleDateFormat(Constants.DATEFORMAT);
		Date startDate=sFormat.parse(date+" 00:00:00");
		Date endDate=sFormat.parse(date+" 23:59:59");
		return chargeDao.getByRange(parkId, startDate, endDate);
	}

	@Override
	public List<PosChargeData> getAllByDay(String date) throws ParseException {
		// TODO Auto-generated method stub
		SimpleDateFormat sFormat=new SimpleDateFormat(Constants.DATEFORMAT);
		Date startDate=sFormat.parse(date+" 00:00:00");
		Date endDate=sFormat.parse(date+" 23:59:59");
		return chargeDao.getAllByDay(startDate,endDate);
	}
	@Override
	public List<PosChargeData> getByParkAndDayRange(int parkId, String startDate,String endDate) throws ParseException  {
		// TODO Auto-generated method stub
		SimpleDateFormat sFormat=new SimpleDateFormat(Constants.DATEFORMAT);
		Date dstartDate=sFormat.parse(startDate+" 00:00:00");
		Date dendDate=sFormat.parse(endDate+" 23:59:59");
		return chargeDao.getByRange(parkId, dstartDate, dendDate);
	}

	@Override
	public List<PosChargeData> getAllByDayRange(String startDate,String endDate) throws ParseException {
		// TODO Auto-generated method stub
		SimpleDateFormat sFormat=new SimpleDateFormat(Constants.DATEFORMAT);
		Date dstartDate=sFormat.parse(startDate+" 00:00:00");
		Date dendDate=sFormat.parse(endDate+" 23:59:59");
		return chargeDao.getAllByDay(dstartDate,dendDate);
	}

	@Override
	public List<PosChargeData> getPageArrearage(int low, int count) {
		// TODO Auto-generated method stub
		return chargeDao.getPageArrearage(low, count);
	}

	@Override
	public List<PosChargeData> getPageArrearageByParkId(int parkId, int start, int count) {
		// TODO Auto-generated method stub
		return chargeDao.getPageArrearageByParkId(parkId, start, count);
	}

	@Override
	public List<PosChargeData> getPageByParkId(int parkId, int start, int count) {
		// TODO Auto-generated method stub
		return chargeDao.getPageByParkId(parkId, start, count);
	}

	@Override
	public List<PosChargeData> getByParkIdAndCardNumber(Integer parkId, String cardNumber) {
		// TODO Auto-generated method stub
		return chargeDao.getByParkIdAndCardNumber(parkId, cardNumber);
	}

	@Override
	public List<PosChargeData> getParkCarportStatusToday(int parkId,Date tmpdate) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
		
		String day=sdf.format(tmpdate)+" 00:00:00";
		return chargeDao.getParkCarportStatusToday(parkId, day);
	}

	@Override
	public Outsideparkinfo getOutsideparkinfoByOrigin(int parkId, String day) {
		// TODO Auto-generated method stub
		Park park = parkService.getParkById(parkId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(day + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date parsedEndDay = null;
		try {
			parsedEndDay = sdf.parse(day + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Outsideparkinfo outsideparkinfo=new Outsideparkinfo();
		List<PosChargeData> posChargeDatas = selectPosdataByParkAndRange(parsedStartDay, parsedEndDay, parkId);
		outsideparkinfo.setParkid(parkId);
		outsideparkinfo.setCarportcount(park.getPortCount());
		outsideparkinfo.setUnusedcarportcount(park.getPortCount());
		if (!posChargeDatas.isEmpty()) {			
			outsideparkinfo.setPossigndate(posChargeDatas.get(0).getEntranceDate());
			for(PosChargeData posChargeData : posChargeDatas){
				outsideparkinfo.setEntrancecount(outsideparkinfo.getEntrancecount()+1);
				outsideparkinfo.setAmountmoney((float) (outsideparkinfo.getAmountmoney()+posChargeData.getChargeMoney()));
				if (posChargeData.getExitDate()==null) {
					outsideparkinfo.setUnusedcarportcount(outsideparkinfo.getUnusedcarportcount()-1);
				}
				else {				
					outsideparkinfo.setOutcount(outsideparkinfo.getOutcount()+1);
				}
				if (posChargeData.isPaidCompleted()) {
					outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+posChargeData.getGivenMoney()+posChargeData.getPaidMoney()-posChargeData.getChangeMoney()));
				}
				else {
					outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+posChargeData.getPaidMoney()));
				}
			}	
		}
		else {
			List<Posdata> posdatas=posdataService.selectPosdataByParkAndRange(park.getName(), parsedStartDay, parsedEndDay);			
			//outsideparkinfo=outsideParkInfoService.getByParkidAndDate(parkId);
			//从数据表中获取数据
			if (posdatas.isEmpty()) {
				return outsideparkinfo;
			}
			outsideparkinfo.setPossigndate(posdatas.get(0).getStarttime());
			for(Posdata posdata:posdatas){
				outsideparkinfo.setAmountmoney(outsideparkinfo.getAmountmoney()+posdata.getMoney().floatValue());
				
				if (posdata.getIsarrearage()==false) {
					if (posdata.getMode()==0) {	
					outsideparkinfo.setEntrancecount(outsideparkinfo.getEntrancecount()+1);															
					outsideparkinfo.setUnusedcarportcount(outsideparkinfo.getUnusedcarportcount()-1);																				
				}
					else {
						outsideparkinfo.setOutcount(outsideparkinfo.getOutcount()+1);
						outsideparkinfo.setUnusedcarportcount(outsideparkinfo.getUnusedcarportcount()+1);
						outsideparkinfo.setRealmoney(outsideparkinfo.getRealmoney()+posdata.getGiving().floatValue()+posdata.getRealmoney().floatValue()-posdata.getReturnmoney().floatValue());
					}
				}
				
				else {
					outsideparkinfo.setRealmoney(outsideparkinfo.getRealmoney()+posdata.getGiving().floatValue()+posdata.getRealmoney().floatValue()-posdata.getReturnmoney().floatValue());
				}
				
			}
			/*List<Posdata> posdatas=posdataService.selectPosdataByParkAndRange(park.getName(), parsedStartDay, parsedEndDay);
			outsideparkinfo.setPossigndate(posdatas.get(0).getStarttime());
			for(Posdata posdata :posdatas){
				outsideparkinfo.setEntrancecount(outsideparkinfo.getEntrancecount()+1);
				outsideparkinfo.setAmountmoney(outsideparkinfo.getAmountmoney()+posdata.getMoney().floatValue());
				if (posdata.getEndtime()==null) {
					outsideparkinfo.setUnusedcarportcount(outsideparkinfo.getUnusedcarportcount()-1);
				}
				else{
					outsideparkinfo.setOutcount(outsideparkinfo.getOutcount()+1);
				}
				if (posdata.get) {
					
				}
			}*/
		}
		return outsideparkinfo;
	}

	@Override
	public List<PosChargeData> getArrearageByCardNumber(String cardNumber) {
		// TODO Auto-generated method stub
		return chargeDao.getArrearageByCardNumber(cardNumber);
	}

	@Override
	public List<PosChargeData> getByParkAuthority(String userName) {
		// TODO Auto-generated method stub
		List<Park> parkList = parkService.getParks();
		if (userName != null)
			parkList = parkService.filterPark(parkList, userName);
		int num=120/parkList.size();
		if (num<1) {
			num=2;
		}
		List<PosChargeData> posChargeDatas=new ArrayList<PosChargeData>();
		for(Park park:parkList){
			List<PosChargeData> tmPosChargeDatas=getPageByParkId(park.getId(), 0, num);
			posChargeDatas.addAll(tmPosChargeDatas);
		}
		return posChargeDatas;
	}

	@Override
	public Integer deleteByParkIdAndDate(int parkId, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return chargeDao.deleteByParkIdAndDate(startDate, endDate, parkId);
	}

	@Override
	public int deleteById(int id) {
		// TODO Auto-generated method stub
		return chargeDao.deleteById(id);
	}
}

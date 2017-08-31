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

import com.park.dao.CarportStatusDetailDAO;
import com.park.dao.PosChargeDataDAO;
import com.park.dao.PosdataDAO;
import com.park.model.CarportStatusDetail;
import com.park.model.Constants;
import com.park.model.FeeCriterion;
import com.park.model.Outsideparkinfo;
import com.park.model.Park;
import com.park.model.Parktoalipark;
import com.park.model.PosChargeData;
import com.park.model.Posdata;
import com.park.service.AliParkFeeService;
import com.park.service.FeeCriterionService;
import com.park.service.OutsideParkInfoService;
import com.park.service.ParkService;
import com.park.service.ParkToAliparkService;
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
	private CarportStatusDetailDAO carportStatusDetailDAO;
		
	@Autowired
	FeeCriterionService criterionService;
	
	@Autowired
	PosdataService posdataService;
	
	@Autowired
	private OutsideParkInfoService outsideParkInfoService;
	
	@Autowired
	AliParkFeeService aliparkFeeService;
	@Autowired
	ParkToAliparkService parkToAliparkService;
	
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
				//信息录入支付宝
				List<Parktoalipark> parktoaliparks=parkToAliparkService.getByParkId(charge.getParkId());
				if (!parktoaliparks.isEmpty()) {
					Parktoalipark parktoalipark=parktoaliparks.get(0);
					Map<String, String> argstoali=new HashMap<>();
					argstoali.put("parking_id", parktoalipark.getAliparkingid());
					argstoali.put("car_number", cardNumber);
					argstoali.put("out_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					aliparkFeeService.parkingExitinfoSync(argstoali);
				}
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
		
	//	Outsideparkinfo outsideparkinfo=outsideParkInfoService.getByParkidAndDate(lastCharge.getParkId());
		if (money >= 0) {
			lastCharge.setGivenMoney(theMoney+lastCharge.getGivenMoney());
			lastCharge.setPaidCompleted(true);
			DecimalFormat df = new DecimalFormat("0.00"); 
			String data=df.format(lastCharge.getChangeMoney() + money);
			lastCharge.setChangeMoney(Double.parseDouble(data));				
//			outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+lastCharge.getGivenMoney()-lastCharge.getChangeMoney()));
//			outsideparkinfo.setPossigndate(new Date());
						
		}
		else {
			lastCharge.setGivenMoney(theMoney+lastCharge.getGivenMoney());
			lastCharge.setUnPaidMoney(lastCharge.getUnPaidMoney()-theMoney);
	//		outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+theMoney));
	//		outsideparkinfo.setPossigndate(new Date());
		}
	// 	outsideParkInfoService.updateByPrimaryKeySelective(outsideparkinfo);
		this.update(lastCharge);
		return lastCharge;
	}

	@Override
	public void calExpense(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception {
		String startTime = new SimpleDateFormat(Constants.DATEFORMAT).format(charge.getEntranceDate());
		String endTime = new SimpleDateFormat(Constants.DATEFORMAT).format(exitDate);
		Park park = parkService.getParkById(charge.getParkId());
		Integer criterionId = park.getFeeCriterionId();
		FeeCriterion criterion = criterionService.getById(criterionId);
		String nightStartHour = "20";
		String nightEndHour = "7";
		if (criterion.getNightstarttime()!=null&&criterion.getNightendtime()!=null) {
			 nightStartHour = criterion.getNightstarttime().split(":")[0];
			 nightEndHour = criterion.getNightendtime().split(":")[0].substring(1);
		}	
		int isOneTimeExpense=charge.getIsOneTimeExpense();
		if (charge.getIsLargeCar() == false) {			
			Map<String,String> dates=formatTime.format(startTime, endTime, nightStartHour,nightEndHour);
			for(String name:dates.keySet()){
				charge.setEntranceDate(name);
			  if (name.substring(11,13).equals(nightStartHour)) {
				charge.setIsOneTimeExpense(1);
				this.calExpenseSmallCar(charge,new SimpleDateFormat(Constants.DATEFORMAT).parse(dates.get(name)),isQuery);
			}
			  else if(isOneTimeExpense==0) {
				  charge.setIsOneTimeExpense(0);
				  this.calExpenseSmallCar(charge,new SimpleDateFormat(Constants.DATEFORMAT).parse(dates.get(name)),isQuery);
			}
			  else {
					charge.setIsOneTimeExpense(1);
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
			if (!isQuery) {
				this.update(charge);
			}
			
			
		} else {
			Map<String,String> dates=formatTime.format(startTime, endTime, nightStartHour,nightEndHour);
			for(String name:dates.keySet()){
				charge.setEntranceDate(name);
			  if (name.substring(11,13).equals(nightStartHour)) {
				charge.setIsOneTimeExpense(1);
				this.calExpenseLargeCar(charge,new SimpleDateFormat(Constants.DATEFORMAT).parse(dates.get(name)),isQuery);
			}
			  else if(isOneTimeExpense==0) {
				  charge.setIsOneTimeExpense(0);
				  this.calExpenseLargeCar(charge,new SimpleDateFormat(Constants.DATEFORMAT).parse(dates.get(name)),isQuery);
			}
			  else {
					charge.setIsOneTimeExpense(1);
					this.calExpenseLargeCar(charge,new SimpleDateFormat(Constants.DATEFORMAT).parse(dates.get(name)),isQuery);
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
			if (!isQuery) {
				this.update(charge);
			}
			
		}
		
	}

	@Override
	public List<PosChargeData> getDebt(String cardNumber, Date exitDate) throws Exception {
		// TODO Auto-generated method stub
		List<PosChargeData> charges = chargeDao.getDebt(cardNumber);
		List<PosChargeData> tmPosChargeDatas = new ArrayList<>();
		for (PosChargeData charge : charges) {
			//信息录入支付宝
			if (charge.getExitDate() == null) {
				List<Parktoalipark> parktoaliparks=parkToAliparkService.getByParkId(charge.getParkId());
				if (!parktoaliparks.isEmpty()) {
					Parktoalipark parktoalipark=parktoaliparks.get(0);
					Map<String, String> argstoali=new HashMap<>();
					argstoali.put("parking_id", parktoalipark.getAliparkingid());
					argstoali.put("car_number", cardNumber);
					argstoali.put("out_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(exitDate));
					aliparkFeeService.parkingExitinfoSync(argstoali);
				}
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
		Park park = parkService.getParkById(charge.getParkId());

		Integer criterionId = park.getFeeCriterionId();

		if (criterionId == null)
			throw new Exception("no fee criterion");
		FeeCriterion criterion = criterionService.getById(criterionId);
		if (criterion.getIsonetimeexpense().intValue()==1) {
			charge.setIsOneTimeExpense(1); 
		}
		charge.setExitDate1(exitDate);
		double expense = 0;
		float diffMin = (charge.getExitDate().getTime() - charge.getEntranceDate().getTime()) / (1000 * 60f);
		if (charge.getIsOneTimeExpense() == 1&&diffMin >= criterion.getFreemins()) {
			expense = criterion.getOnetimeexpense();
		} else {	
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
					double intervals2 = Math.ceil((diffMin - firstHour) / (criterion.getTimeoutpriceinterval()));
			//		double intervals2 = Math.ceil((diffMin - firstHour) / (criterion.getTimeoutpriceinterval()/2));

					expense += intervals2 * criterion.getStep2pricelarge();
				}
			}
		}
		charge.setChargeMoney(expense+charge.getChargeMoney());
		if (charge.getChargeMoney()>(criterion.getMaxexpense()*2)) {
			charge.setChargeMoney(criterion.getMaxexpense()*2);
		}
	}

	@Override
	public void calExpenseSmallCar(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception {

		Park park = parkService.getParkById(charge.getParkId());

		Integer criterionId = park.getFeeCriterionId();

		if (criterionId == null)
			throw new Exception("no fee criterion");

		FeeCriterion criterion = criterionService.getById(criterionId);

		charge.setExitDate1(exitDate);
		double expense = 0;
		if (criterion.getIsonetimeexpense().intValue()==1) {
			charge.setIsOneTimeExpense(1); 
		}
		float diffMin = (float) Math.ceil((charge.getExitDate().getTime() - charge.getEntranceDate().getTime()) / (1000 * 60f));
		if (charge.getIsOneTimeExpense() == 1&&diffMin >= criterion.getFreemins()) {
			expense = criterion.getOnetimeexpense();
		} else {
			
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
					double intervals2 = Math.ceil((diffMin - firstHour) / (criterion.getTimeoutpriceinterval()));
					//double intervals2 = Math.ceil((diffMin - firstHour) / (criterion.getTimeoutpriceinterval()/2));
					expense += intervals2 * criterion.getStep2price();
				}
			}
		}
		charge.setChargeMoney(expense+charge.getChargeMoney());
		if (charge.getChargeMoney()>criterion.getMaxexpense()) {
			charge.setChargeMoney(criterion.getMaxexpense());
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
//			Outsideparkinfo outsideparkinfo=outsideParkInfoService.getByParkidAndDate(lastCharge.getParkId());
//			outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+lastCharge.getPaidMoney()+lastCharge.getGivenMoney()-lastCharge.getChangeMoney()));
//			outsideparkinfo.setPossigndate(new Date());
//			outsideParkInfoService.updateByPrimaryKeySelective(outsideparkinfo);
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
		Outsideparkinfo outsideparkinfo2=new Outsideparkinfo();		
		outsideparkinfo2 = outsideParkInfoService.getByParkidAndDate(parkId,new Date());
		 
		if (outsideparkinfo2!=null&&outsideparkinfo2.getPossigndate()!=null) {
			outsideparkinfo.setPossigndate(outsideparkinfo2.getPossigndate());
		}
		List<PosChargeData> posChargeDatas = selectPosdataByParkAndRange(parsedStartDay, parsedEndDay, parkId);
		outsideparkinfo.setParkid(parkId);
		outsideparkinfo.setCarportcount(park.getPortCount());
		outsideparkinfo.setUnusedcarportcount(park.getPortLeftCount());
		if (!posChargeDatas.isEmpty()) {			
			if (outsideparkinfo2!=null&&outsideparkinfo2.getPossigndate()==null) {
				outsideparkinfo.setPossigndate(posChargeDatas.get(0).getEntranceDate());
			}
			else if(outsideparkinfo2!=null&&posChargeDatas.get(0).getEntranceDate().getTime()>outsideparkinfo2.getPossigndate().getTime()){
				outsideparkinfo.setPossigndate(posChargeDatas.get(0).getEntranceDate());
			}
			
			for(PosChargeData posChargeData : posChargeDatas){
				outsideparkinfo.setEntrancecount(outsideparkinfo.getEntrancecount()+1);
				outsideparkinfo.setAmountmoney((float) (outsideparkinfo.getAmountmoney()+posChargeData.getChargeMoney()));
				if (posChargeData.getExitDate()==null) {
					outsideparkinfo.setUnusedcarportcount(outsideparkinfo.getUnusedcarportcount()-1);
				}
				else {				
					outsideparkinfo.setOutcount(outsideparkinfo.getOutcount()+1);
				}				
				outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+posChargeData.getGivenMoney()+posChargeData.getPaidMoney()-posChargeData.getChangeMoney()));			
				if(!posChargeData.isPaidCompleted()) 
				{
					if (posChargeData.getChargeMoney()>(posChargeData.getPaidMoney()+posChargeData.getGivenMoney())) {
						outsideparkinfo.setArrearage((float) (outsideparkinfo.getArrearage()+posChargeData.getChargeMoney()-posChargeData.getPaidMoney()-posChargeData.getGivenMoney()));
					}
			//		outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+posChargeData.getPaidMoney()));
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

	@Override
	public List<PosChargeData> selectPosdataByParkAndRangeAndCarportNumber(Date startDay, Date endDay, int parkId,
			int carportNumber) {
		// TODO Auto-generated method stub
		return chargeDao.selectPosdataByParkAndRangeAndCarportNumber(startDay, endDay, parkId, carportNumber);
	}

	@Override
	public List<PosChargeData> hardwareRecord(Integer parkId, String startDate, String endDate) throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> carportStatusDetails=carportStatusDetailDAO.getDetailByParkIdAndDateRange(parkId, startDate, endDate);
		List<PosChargeData> posChargeDatas=new ArrayList<>();
		SimpleDateFormat sFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Map<String, Object> c:carportStatusDetails){
			if (c.get("endTime")!=null) {
				PosChargeData tmPosChargeData=new PosChargeData();
			//	tmPosChargeData.setCardNumber(Integer.toString(c.getCarportId()));
				tmPosChargeData.setEntranceDate(sFormat.format(c.get("startTime")));
				tmPosChargeData.setExitDate(sFormat.format(c.get("endTime")));
				tmPosChargeData.setParkId(parkId);
				tmPosChargeData.setPortNumber(String.valueOf(c.get("carportNumber")));
				tmPosChargeData.setIsLargeCar(false);
				tmPosChargeData.setIsOneTimeExpense(0);	
				posChargeDatas.add(tmPosChargeData);
			}			
		}
		for(PosChargeData p:posChargeDatas){
			this.calExpense(p, p.getExitDate(), true);
		}
		return posChargeDatas;
	}

	@Override
	public PosChargeData updateRejectReason(String cardNumber, String rejectReason) throws Exception {
		// TODO Auto-generated method stub
		List<PosChargeData> charges = this.getCharges(cardNumber);
		if(charges.isEmpty()){
			return null;
		}
		PosChargeData lastCharge = charges.get(0);
		lastCharge.setRejectReason(rejectReason);
		this.update(lastCharge);
		return lastCharge;
	}

	@Override
	public List<PosChargeData> getByCardNumberAndPort(String cardNumber, Integer portNumber) {
		// TODO Auto-generated method stub
		return chargeDao.getByCardNumberAndPort(cardNumber, portNumber);
	}
}

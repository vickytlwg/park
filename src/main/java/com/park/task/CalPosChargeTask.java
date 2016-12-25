package com.park.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.park.model.Constants;
import com.park.model.FeeCriterion;
import com.park.model.Outsideparkinfo;
import com.park.model.PosChargeData;
import com.park.service.FeeCriterionService;
import com.park.service.OutsideParkInfoService;
import com.park.service.ParkService;
import com.park.service.PosChargeDataService;

@Component
public class CalPosChargeTask {
	
	@Autowired
	PosChargeDataService chargeService;
	
	@Autowired
	ParkService parkService;
	
	@Autowired
	FeeCriterionService criterionService;
	@Autowired
	private OutsideParkInfoService outsideParkInfoService;
	@Scheduled(cron="0 0 18/1  * * ? ")
	public void cal(){			
		List<PosChargeData> charges = chargeService.getUnCompleted();
		for(PosChargeData charge : charges){
			Date now = new Date();	
			Integer criterionId = parkService.getParkById(charge.getParkId()).getFeeCriterionId();
			FeeCriterion criterion = null;
			if(criterionId != null)
				criterion = criterionService.getById(criterionId);
			if(criterion == null)
				continue;
			int nightStartHour = Integer.parseInt(criterion.getNightstarttime().split(":")[0]);
			if(nightStartHour <= now.getHours()){
				now.setHours(nightStartHour);
				try {
//					chargeService.calExpense(charge, now, false);
//					charge.setChangeMoney(0);
//					charge.setChargeMoney(0);
//					charge.setPaidMoney(0);
//					charge.setGivenMoney(0);
//					charge.setIsOneTimeExpense(1);
//					charge.setEntranceDate(new SimpleDateFormat(Constants.DATEFORMAT).format(new Date()));
//					chargeService.insert(charge);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	}
	@Scheduled(cron="0 30 0 * * ? ")
	public void dayInfo(){
		outsideParkInfoService.insertDayParkInfo();		
	}
	@Scheduled(cron="0 0 8 * * ? ")
	public void out(){
//		List<PosChargeData> charges = chargeService.getUnCompleted();		
//		for(PosChargeData charge : charges){
//			Date now = new Date();
//			try {
//				chargeService.calExpense(charge, now, false);
//				charge.setChangeMoney(0);
//				charge.setChargeMoney(0);
//				charge.setPaidMoney(0);
//				charge.setGivenMoney(0);
//				charge.setEntranceDate(new SimpleDateFormat(Constants.DATEFORMAT).format(new Date()));
//				chargeService.insert(charge);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
}

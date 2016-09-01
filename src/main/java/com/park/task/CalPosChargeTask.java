package com.park.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.park.model.FeeCriterion;
import com.park.model.PosChargeData;
import com.park.service.FeeCriterionService;
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
					chargeService.calExpense(charge, now);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

}

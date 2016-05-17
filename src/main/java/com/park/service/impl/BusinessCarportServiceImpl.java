package com.park.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.park.dao.BusinessCarportDAO;
import com.park.dao.CarportStatusDetailDAO;
import com.park.dao.HardwareDAO;
import com.park.dao.ParkDAO;
import com.park.model.BusinessCarport;
import com.park.model.BusinessCarportDetail;
import com.park.model.CarportStatusDetail;
import com.park.model.Hardware;
import com.park.model.HardwareType;
import com.park.model.Status;
import com.park.service.BusinessCarportService;
import com.park.service.HardwareService;
import com.park.service.Utility;

@Transactional
@Service
public class BusinessCarportServiceImpl implements BusinessCarportService{
	@Autowired
	private BusinessCarportDAO businessCarportDAO;
	
	@Autowired
	private HardwareDAO hardwareDAO;
	
	@Autowired
	private HardwareService hardwareService;
	
	@Autowired
	private CarportStatusDetailDAO carportStatusDetailDAO;
	
	@Autowired
	private ParkDAO parkDAO;
	
	private static Log logger = LogFactory.getLog(BusinessCarportServiceImpl.class);

	@Override
	public List<BusinessCarport> getBusinessCarports() {
		return businessCarportDAO.getBusinessCarports();
	}

	@Override
	public BusinessCarport getBusinessCarportById(int id) {
		return businessCarportDAO.getBusinessCarportById(id);
	}
	
	@Override
	public int getBusinessCarportCount(Integer parkId) {
		if(parkId == null || parkId.intValue() == -1)
			return businessCarportDAO.getBusinessCarportCount();
		else
			return businessCarportDAO.getParkBusinessCarportCount(parkId.intValue());
	}

	@Override
	public List<BusinessCarportDetail> getBusinessCarportDetail(int low,
			int count, Integer parkId) {
		if(parkId == null || parkId.intValue() == -1)
			return businessCarportDAO.getBusinessCarportDetail(low, count);
		else
			return businessCarportDAO.getParkBusinessCarportDetail(low, count, parkId.intValue());
	}

	@Override
	public int insertBusinessCarport(BusinessCarport businessCarport) {
		
		int carportRet = 0;
		Integer macId = businessCarport.getMacId();
		//check if mackId exist 
		if(businessCarportDAO.checkMacExist(businessCarport.getParkId(), macId) > 0)
			return 0;
		if(macId < 0)
			businessCarport.setMacId(null);
		carportRet =  businessCarportDAO.insertBusinessCarport(businessCarport);
		if(carportRet >0)
		{
			if(macId >= 0)
				return hardwareService.bindHardware(macId) ? 1 : 0;
			return 1;
		}else
			return 0;
	}
	

	@Override
	public int updateBusinessCarport(BusinessCarport businessCarport) {
		//update park left port
		BusinessCarport oldBusinessCarport = this.getBusinessCarportById(businessCarport.getId());
		Integer oldMacId = oldBusinessCarport.getMacId();
		Integer newMacId = businessCarport.getMacId();
		if(newMacId < 0)
			businessCarport.setMacId(null);
		if(businessCarportDAO.checkMacExist(businessCarport.getParkId(), newMacId) > 0)
			return 0;
				
		if(oldMacId!=null && oldMacId > 0)
			hardwareService.changeHardwareStatus(oldBusinessCarport.getMacId(), Status.UNUSED.getValue());
		if(newMacId > 0)
			hardwareService.changeHardwareStatus(businessCarport.getMacId(), Status.USED.getValue());
	
		
		int ret = businessCarportDAO.updateBusinessCarport(businessCarport);
			
		return ret;
	}

	@Override
	public int deleteBusinessCarport(int id) {
		BusinessCarport carport = this.getBusinessCarportById(id);
		if(carport.getMacId() != null && carport.getMacId() > 0)
			hardwareService.changeHardwareStatus(carport.getMacId(), Status.UNUSED.getValue());
		return businessCarportDAO.deleteBusinessCarport(id);
	}

	@Override
	public int updateBusinessCarportStatus(String mac, int status) {
		int macId = hardwareDAO.macToId(mac);
		Hardware hardware = hardwareDAO.getHardwareById(macId);
		if(hardware.getStatus() == Status.UNUSED.getValue()){
			logger.info("hardware is unused" );
			return 0;
		}
		
//		if(hardware.getType() != HardwareType.CARPORT.getValue()){
//			logger.info("hardware is not bound to carport");
//			return 0;
//		}
		
		BusinessCarport carport = businessCarportDAO.getBusinessCarportByMacId(macId);
		carport.setStatus(status);
		
		int ret = businessCarportDAO.updateBusinessCarportStatus(macId, status, new Date());
		int parkId = carport.getParkId();
		int leftPort = businessCarportDAO.getParkLeftPort(parkId);
		parkDAO.updateLeftPortCount(parkId, leftPort);
		
		//get carport status detail
		if(status == 1){//have a car , insert new record
			CarportStatusDetail detail= new CarportStatusDetail();
			detail.setCarportId(carport.getId());
			detail.setStartTime(new Date());
			detail.setCharged(0);
			carportStatusDetailDAO.insert(detail);
			
		}else{ //car leave, update old record
			CarportStatusDetail detail = carportStatusDetailDAO.getLatestDetailByCarportId(carport.getId());
			if(detail != null)
			{
				detail.setEndTime(new Date());
				carportStatusDetailDAO.update(detail);	
				
			}
					
		}
			
		return ret;
	}

	@Override
	public int getCarportStatusDetailCount() {
		
		return carportStatusDetailDAO.count();
	}

	@Override
	public List<CarportStatusDetail> getCarportStatusDetailByCarportId(int carportId) {
		return carportStatusDetailDAO.getByCarportId(carportId);
	}

	@Override
	public List<CarportStatusDetail> getLimitCarportStatusDetail(int start,
			int len) {
		
		return carportStatusDetailDAO.limitGet(start, len);
	}

	@Override
	public List<CarportStatusDetail> getDetailByCarportId(int carportId) {
		
		return carportStatusDetailDAO.getDetailByCarportId(carportId);
	}

	@Override
	public CarportStatusDetail getLatestDetailByCarportId(int carportId) {
		
		return carportStatusDetailDAO.getLatestDetailByCarportId(carportId);
	}

	@Override
	public List<CarportStatusDetail> getDayCarportStatusDetail(int carportId, Date startDay, Date endDay) {
		return carportStatusDetailDAO.getDayCarportStatusDetail(carportId, startDay, endDay);
	}

	@Override
	public double getCarportUsage(int carportId, Date startDay, Date endDay) {
		List<CarportStatusDetail> carportUsages = getDayCarportStatusDetail(carportId, startDay, endDay);
		
		long usage = 0;
		for (CarportStatusDetail c : carportUsages)
		{
			if(c.getStartTime() == null || c.getEndTime() == null)
				continue;
			Date carportStart = c.getStartTime();
			Date carportEnd = c.getEndTime();
			if(carportStart.before(startDay)){
				carportStart = startDay;
			}
			if(carportEnd.after(endDay))
				carportEnd = endDay;
			usage = usage + carportEnd.getTime() - carportStart.getTime();
		}
		
		double rate = usage / (endDay.getTime() - startDay.getTime()) * 1.0;
		return rate;
	}

	@Override
	public double getParkUsage(int parkId, Date startDay, Date endDay) {
		int count = getBusinessCarportCount(parkId);
		List<BusinessCarport> carports = businessCarportDAO.getBusinessCarportByParkId(parkId, 0, count);
		double rate = 0.0;
		for(BusinessCarport carport : carports){
			rate += this.getCarportUsage(carport.getId(), startDay, endDay);
		}
		return rate / count;
	}

	
}

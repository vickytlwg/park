package com.park.service.impl;

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
		int macId = businessCarport.getMacId();
		//check if mackId exist 
		if(businessCarportDAO.checkMacExist(businessCarport.getParkId(), macId) > 0)
			return 0;
		carportRet =  businessCarportDAO.insertBusinessCarport(businessCarport);
 		if(carportRet > 0 && hardwareService.bindHardware(macId)){
			return 1;
		}else
			return 0;
	}
	

	@Override
	public int updateBusinessCarport(BusinessCarport businessCarport) {
		//update park left port
		BusinessCarport oldBusinessCarport = this.getBusinessCarportById(businessCarport.getId());
		int oldMacId = oldBusinessCarport.getMacId();
		int newMacId = businessCarport.getMacId();
		if(businessCarportDAO.checkMacExist(businessCarport.getParkId(), newMacId) > 0)
			return 0;
		if(oldMacId != newMacId){			
			hardwareService.changeHardwareStatus(oldBusinessCarport.getMacId(), Status.UNUSED.getValue());
			hardwareService.changeHardwareStatus(businessCarport.getMacId(), Status.USED.getValue());
		}
		
		int ret = this.updateBusinessCarport(businessCarport);
			
		return ret;
	}

	@Override
	public int deleteBusinessCarport(int id) {
		return businessCarportDAO.deleteBusinessCarport(id);
	}

	@Override
	public int updateBusinessCarportStatus(String mac, int status) {
		int macId = hardwareDAO.macToId(mac);
		Hardware hardware = hardwareDAO.getHardwareById(macId);
		logger.info("hardware : " + hardware.getId() + ", mac: " + hardware.getMac() + " status: " + hardware.getStatus() + " hardware: " );
		if(hardware.getStatus() == Status.UNUSED.getValue()){
			logger.info("hardware is unused" );
			return 0;
		}
		
		if(hardware.getType() != HardwareType.CARPORT.getValue()){
			logger.info("hardware is not bound to carport");
			return 0;
		}
		
		BusinessCarport carport = businessCarportDAO.getBusinessCarportByMacId(macId);
		carport.setStatus(status);
		
		int ret = businessCarportDAO.updateBusinessCarportStatus(macId, status, new Date());
		int parkId = carport.getParkId();
		int leftPort = businessCarportDAO.getParkLeftPort(parkId);
		parkDAO.updateLeftPortCount(parkId, leftPort);
		
		//get carport status detail
		if(status == 0){//have a car , insert new record
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
	public List<CarportStatusDetail> getCarportStatusDetail() {
		return carportStatusDetailDAO.get();
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


}

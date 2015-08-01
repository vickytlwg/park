package com.park.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.park.dao.BusinessCarportDAO;
import com.park.dao.HardwareDAO;
import com.park.dao.ParkDAO;
import com.park.model.BusinessCarport;
import com.park.model.BusinessCarportDetail;
import com.park.model.Hardware;
import com.park.model.HardwareType;
import com.park.model.Status;
import com.park.service.BusinessCarportService;

@Transactional
@Service
public class BusinessCarportServiceImpl implements BusinessCarportService{
	@Autowired
	private BusinessCarportDAO businessCarportDAO;
	
	@Autowired
	private HardwareDAO hardwareDAO;
	
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
		return businessCarportDAO.insertBusinessCarport(businessCarport);
	}

	@Override
	public int updateBusinessCarport(BusinessCarport businessCarport) {
		//update park left port
		
		
		return businessCarportDAO.updateBusinessCarport(businessCarport);
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
			
		return ret;
	}

}

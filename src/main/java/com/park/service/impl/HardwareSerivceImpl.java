package com.park.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.park.dao.HardwareDAO;
import com.park.model.Hardware;
import com.park.model.HardwareDetail;
import com.park.model.Status;
import com.park.service.HardwareService;

@Transactional
@Service
public class HardwareSerivceImpl implements HardwareService{

	@Autowired
	private HardwareDAO hardwareDAO;
	
	//private static Log logger = LogFactory.getLog(UserController.class);
	@Override
	public List<Hardware> getHardwares() {
		return hardwareDAO.getHardwares();
	}

	@Override
	public int macToId(String mac){
		return hardwareDAO.macToId(mac);
	}
	
	@Override
	public Hardware getHardwareById(int id) {
		return hardwareDAO.getHardwareById(id);
	}
	
	@Override
	public int getHardwareCount() {
		return hardwareDAO.getHardwareCount();
	}

	@Override
	public List<HardwareDetail> getHardwareDetail(int low, int count) {
		return hardwareDAO.getHardwareDetail(low, count);
	}

	@Override
	public List<Hardware> getUnboundHardwares(int type) {
		return hardwareDAO.getUnboundHardwares(type);
	}
	
	@Override
	public int insertHardware(Hardware hardware) {
		return hardwareDAO.insertHardware(hardware);
	}

	@Override
	public int updateHardware(Hardware hardware) {
		return hardwareDAO.updateHardware(hardware);
	}

	@Override
	public int deleteHardware(int id) {
		return hardwareDAO.deleteHardware(id);
	}

	@Override
	public List<Hardware> getUnusedHardwares() {
		return hardwareDAO.getUnusedHardwares();
	}

	@Override
	public boolean bindHardware(int id) {
		Hardware hardware = this.getHardwareById(id);
		if(hardware.getStatus() == Status.USED.getValue())
			return false;
		hardware.setStatus(Status.USED.getValue());
		int ret = hardwareDAO.updateHardware(hardware);
		return ret > 0;
	}

	@Override
	public boolean changeHardwareStatus(int id, int status) {
		Hardware hardware = this.getHardwareById(id);
		int ret = 0;
		if(hardware.getStatus() != status){
			hardware.setStatus(status);
			ret = this.updateHardware(hardware);
		}
		return ret > 0;
	}

	@Override
	public List<Hardware> searchHardware(String mac) {
		// TODO Auto-generated method stub
		
		return hardwareDAO.searchHardware(mac);
	}

	@Override
	public Map<String, Object> getInfoByMac(String mac) {
		// TODO Auto-generated method stub
		return hardwareDAO.getInfoByMac(mac);
	}

	



}

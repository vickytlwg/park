package com.park.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.park.model.Hardware;
import com.park.model.HardwareDetail;

public interface HardwareService {
	
	public List<Hardware> getHardwares();
	
	public int macToId(String mac);
	
	public Hardware getHardwareById(int id);
	
	public List<Hardware> getUnusedHardwares();
	
	public List<Hardware> getUnboundHardwares(int type);
	
	public int getHardwareCount();
	
	public List<HardwareDetail> getHardwareDetail(int low, int count);
	
	public int insertHardware(Hardware hardware);
	public int macToType(String mac);
	public int updateHardware(Hardware hardware);
	
	public int deleteHardware(int id);
	public Map<String,Object>getInfoByMac(String mac);
	public boolean bindHardware(int id);
	public List<Hardware> searchHardware(String mac);
	public boolean changeHardwareStatus(int id, int status);
}

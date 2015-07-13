package com.park.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.park.model.Hardware;
import com.park.model.HardwareDetail;

@Repository
public interface HardwareDAO {
	
	public List<Hardware> getHardwares();
	
	public Hardware getHardwareById(int id);
	
	public List<Hardware> getUnusedHardwares();
	
	public List<Hardware> getUnboundHardwares(@Param("type") int type);
	
	public int getHardwareCount();
	
	public int macToId(String mac);
	
	public List<HardwareDetail> getHardwareDetail(@Param("low")int low, @Param("count")int count);
	
	public int insertHardware(Hardware hardware);
	
	public int updateHardware(Hardware hardware);
	
	public int deleteHardware(int id);
}

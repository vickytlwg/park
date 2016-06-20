package com.park.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.park.model.Channel;
import com.park.model.ChannelDetail;

@Repository
public interface ChannelDAO {
	
	public List<Channel> getChannels();
	
	public Channel getChannelById(int id);
	
	public int getChannelCount();
	
	public List<ChannelDetail> getChannelDetail(@Param("low")int low, @Param("count")int count);
	public List<ChannelDetail> getChannelDetailByKeywords(@Param("keywords")String keywords);
	public List<ChannelDetail> getParkChannelDetail(@Param("low")int low, @Param("count")int count,@Param("parkId")int parkId);
	
	public List<ChannelDetail> getChannelDetailByDate(@Param("startday")String startday,@Param("endday")String endday);
	
	public int getChannelIdByMacId(@Param("macId")int macId);
	
	public int insertChannel(Channel channel);
	
	public int updateChannel(Channel channel);
	public void updateDateByHardwareMac(@Param("mac")String mac);
	public int deleteChannel(int id);

}

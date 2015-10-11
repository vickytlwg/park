package com.park.service;

import java.util.List;

import com.park.model.Channel;
import com.park.model.ChannelDetail;

public interface ChannelService {
	
	public List<Channel> getChannels();
	
	public Channel getChannelsById(int id);
	
	public int getchannelCount();
	
	public List<ChannelDetail> getChannelDetail(int low, int count);
	public List<ChannelDetail> getParkChannelDetail(int low, int count,Integer parkId);
	public int getChannelIdByMacId(int macId);
	
	public int getChannelIdByMac(String mac);
	
	public boolean insertChannel(Channel channel);
	
	public String insertChannelList(List<Channel> channels);
	
	public boolean updateChannel(Channel channel);
	
	public String deleteChannel(int Id);
	
	public void updateChannelDateByMac(String mac);

}

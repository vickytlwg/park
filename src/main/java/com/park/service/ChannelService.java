package com.park.service;

import java.util.List;

import com.park.model.Channel;

public interface ChannelService {
	
	public List<Channel> getChannels();
	
	public String insertChannel(Channel channel);
	
	public String insertChannelList(List<Channel> channels);
	
	public String updateChannel(Channel channel);
	
	public String deleteChannel(int Id);

}

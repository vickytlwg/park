package com.park.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.park.model.Channel;

@Repository
public interface ChannelDAO {
	
	public List<Channel> getChannels();
	
	public int insertChannel(Channel channel);
	
	
	public int updateChannel(Channel channel);
	
	public int deleteChannel(int Id);

}

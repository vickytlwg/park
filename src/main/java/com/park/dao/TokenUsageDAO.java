package com.park.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.park.model.TokenUsage;
import com.park.model.UserTokenUsage;

@Repository
public interface TokenUsageDAO {
	
	public int count();
	
	public List<UserTokenUsage> get(int begin, int len);
	
	public List<UserTokenUsage> getUsage(int userId);
	
	public int insert(TokenUsage usage);

}

package com.park.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface TokenDAO {
	
	public int getTokenCount(String token);

}

package com.park.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface UserParkDAO {

	public List<Integer> getOwnParkId(int userId);
}

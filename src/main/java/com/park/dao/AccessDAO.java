package com.park.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.park.model.Access;

@Repository
public interface AccessDAO {
	
	public List<Access> getAccesses();
	
	public int insertAccess(Access item);
	
	
	public int updateAccess(Access access);
	
	public int deleteAccess(int Id);

}

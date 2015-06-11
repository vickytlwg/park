package com.park.service;

import java.util.List;

import com.park.model.Access;

public interface AccessService {
	
	public List<Access> getAccesses();
	
	public String insertAccess(Access item);
	
	public String insertAccessList(List<Access> accesses);
	
	public String updateAccess(Access access);
	
	public String deleteAccess(int Id);

}

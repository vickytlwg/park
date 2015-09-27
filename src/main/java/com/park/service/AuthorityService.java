package com.park.service;

public interface AuthorityService {

	boolean checkUserAccess(String username, String password);
}

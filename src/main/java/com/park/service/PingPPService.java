package com.park.service;

import java.util.Map;


public interface PingPPService {
	
	String saomaPay(Map<String, Object> argMap);
	
	String saomaPayByPosId(Map<String, Object> argMap);
	
	String query(Map<String, Object> argMap) ;
}

package com.park.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.park.model.Posdata;

public interface PosdataService {
 public int insert(Posdata record);
 public List<Posdata> selectAll();
 public List<Posdata> selectPosdataByPage(int low,int count);
 public int getPosdataCount();
 public List<Posdata> selectPosdataByCarportAndRange(String parkName, Date startDay, Date endDay);
 
 public Map<String, Object> getCarportCharge(int carportId, Date startDay, Date endDay);
 
 public Map<String, Object> getParkCharge(int parkId, Date startDay, Date endDay);
 
 
}

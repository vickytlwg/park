package com.park.service;

import java.util.List;

import com.park.model.Posdata;

public interface PosdataService {
 public int insert(Posdata record);
 public List<Posdata> selectAll();
 public List<Posdata> selectPosdataByPage(int low,int count);
 public int getPosdataCount();
}

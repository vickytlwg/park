package com.park.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.park.model.Posdata;

public interface PosdataDAO {   
    int insert(Posdata record);   
    List<Posdata> selectAll();
    List<Posdata> selectPosdataByPage(@Param("low")int low,@Param("count")int count);
    int getPosdataCount();
    
    public List<Posdata> getCarportData(int carportId, Date startDay, Date endDay);
    
    public  List<Posdata> getParkData(int parkId, Date startDay, Date endDay);
}
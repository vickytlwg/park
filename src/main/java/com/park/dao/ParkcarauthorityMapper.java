package com.park.dao;

import java.util.List;

import com.park.model.Parkcarauthority;

public interface ParkcarauthorityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Parkcarauthority record);

    int insertSelective(Parkcarauthority record);

    Parkcarauthority selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Parkcarauthority record);

    int updateByPrimaryKey(Parkcarauthority record);
    
    List<Parkcarauthority> getByParkId(Integer parkId);
    
    List<Parkcarauthority> getByStartAndCount(Integer start,Integer count);
}
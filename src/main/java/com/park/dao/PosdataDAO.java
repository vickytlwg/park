package com.park.dao;

import com.park.model.Posdata;

public interface PosdataDAO {
    int deleteByPrimaryKey(Long id);

    int insert(Posdata record);

    int insertSelective(Posdata record);

    Posdata selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Posdata record);

    int updateByPrimaryKey(Posdata record);
}
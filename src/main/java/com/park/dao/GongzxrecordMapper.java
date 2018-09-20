package com.park.dao;

import com.park.model.Gongzxrecord2;

public interface GongzxrecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Gongzxrecord2 record);

    int insertSelective(Gongzxrecord2 record);

    Gongzxrecord2 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Gongzxrecord2 record);

    int updateByPrimaryKey(Gongzxrecord2 record);
}
package com.park.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.park.model.Monthuser;
@Repository
public interface MonthuserDAO {
	
    int deleteByPrimaryKey(Integer id);

    int insert(Monthuser record);

    int insertSelective(Monthuser record);

    Monthuser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Monthuser record);

    int updateByPrimaryKey(Monthuser record);
    
    int getCount();
    
    List<Monthuser> getByStartAndCount(@Param("start")int start,@Param("count")int count);
}
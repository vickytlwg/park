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
    
    int getCountByParkId(@Param("parkId")int parkId);
    
    List<Monthuser> getByStartAndCount(@Param("start")int start,@Param("count")int count);
    
    List<Monthuser> getByUsernameAndPark(@Param("username")String username,@Param("parkId")int parkId);
    
    List<Monthuser> getByParkAndPort(@Param("parkId")int parkId,@Param("portNumber")String portNumber);
    
    List<Monthuser> getByUserNameAndParkAndPort(@Param("username")String username,@Param("parkId")int parkId,@Param("portNumber")String portNumber);
    
    List<Monthuser> getByCarnumberAndPark(@Param("carnumber")String carnumber,@Param("parkId")int parkId);
    
    List<Monthuser> getByStartAndCountAndOrder(@Param("start")int start,@Param("count")int count,@Param("type")int type);
    
    List<Monthuser> getByCardNumber(@Param("cardNumber")String cardNumber);
    
    List<Monthuser> getByParkIdAndCount(@Param("parkId")int parkId,@Param("start")int start,@Param("count")int count);
    
    List<Monthuser> getByParkIdAndCountOrder(@Param("parkId")int parkId,@Param("start")int start,@Param("count")int count,@Param("type")int type);

}
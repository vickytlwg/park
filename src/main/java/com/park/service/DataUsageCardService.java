package com.park.service;

import java.util.List;

import com.park.model.DataUsageCard;
import com.park.model.DataUsageCardDetail;

public interface DataUsageCardService {
	
	public int getCardCount();
	
	public List<DataUsageCardDetail> getCardDetail(int low, int count);
	
	public List<DataUsageCardDetail> getCardById(int id);
	
	public List<DataUsageCard> getUsageCardById(int id);
	
	public List<DataUsageCardDetail> getCardByCardNumber(String cardNumber);
	
	public List<DataUsageCardDetail> getCardByPhoneNumber(String phoneNumber);
	
	public int insertCard(DataUsageCard card);
	
	public int updateCard(DataUsageCard card);
	
	public int deleteCard(int id);

}

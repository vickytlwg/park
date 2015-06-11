package com.park.model;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonIgnore;


public class Access {
	
	@JsonIgnore
	private int Id;
	private int channelId;
	@JsonIgnore
	private Date date;
	@JsonIgnore
	private int isDeleted;
	
	
	public int getId() {
		return Id;
	}
	public int getChannelId() {
		return channelId;
	}
	public Date getDate() {
		return date;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setId(int id) {
		Id = id;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	

}

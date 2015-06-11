package com.park.model;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonIgnore;


public class Channel {
	
	@JsonIgnore
	private int Id;
	private int parkId;
	private String mac;
	private int channelId;
	private int channelFlag;
	@JsonIgnore
	private Date date;
	private int status;
	private int isEffective;
	@JsonIgnore
	private String description;
	@JsonIgnore
	private int isDeleted;
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public int getIsEffective() {
		return isEffective;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setIsEffective(int isEffective) {
		this.isEffective = isEffective;
	}
	
	
	public String getMac() {
		return mac;
	}

	public int getChannelId() {
		return channelId;
	}

	public int getChannelFlag() {
		return channelFlag;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public void setChannelFlag(int channelFlag) {
		this.channelFlag = channelFlag;
	}
	
	
	
	public int getId() {
		return Id;
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
	public void setDate(Date date) {
		this.date = date;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public int getParkId() {
		return parkId;
	}
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	
	

}

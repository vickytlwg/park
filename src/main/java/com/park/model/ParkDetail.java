package com.park.model;


public class ParkDetail {
	
	
	private int id;
	private String name;
	private int portCount;
	private int channelCount;
	private int portLeftCount;
	private double charge;
	private double charge1;
	private double charge2;
	private int status;
	private int isFree;
	private int floor;
	private int type;
	private double longitude = -1;
	private double latitude = -1;
	private String alias = "";
	private String position;
	private String date;
	
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getPortCount() {
		return portCount;
	}
	public int getChannelCount() {
		return channelCount;
	}
	public int getPortLeftCount() {
		return portLeftCount;
	}
	public double getCharge() {
		return charge;
	}
	public double getCharge1() {
		return charge1;
	}
	public double getCharge2() {
		return charge2;
	}
	public int getStatus() {
		return status;
	}
	public int getIsFree() {
		return isFree;
	}
	public int getFloor() {
		return floor;
	}
	public int getType() {
		return type;
	}
	public String getPosition() {
		return position;
	}
	public String getDate() {
		return date;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPortCount(int portCount) {
		this.portCount = portCount;
	}
	public void setChannelCount(int channelCount) {
		this.channelCount = channelCount;
	}
	public void setPortLeftCount(int portLeftCount) {
		this.portLeftCount = portLeftCount;
	}
	public void setCharge(double charge) {
		this.charge = charge;
	}
	public void setCharge1(double charge1) {
		this.charge1 = charge1;
	}
	public void setCharge2(double charge2) {
		this.charge2 = charge2;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setIsFree(int isFree) {
		this.isFree = isFree;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getLongitude() {
		return longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public String getAlias() {
		return alias;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	

}

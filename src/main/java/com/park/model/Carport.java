package com.park.model;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Carport {
	
	private int Id;
	private String name;
	private String province;
	private String city;
	private String district;
	private String street;
	private String coordinate;
	@JsonIgnore
	private int parkId;
	private double price;
	private String tenancy;
	private String contact;
	private String number;
	private String description;
	private Date date;
	
	
	public int getId() {
		return Id;
	}
	public String getName() {
		return name;
	}
	public String getProvince() {
		return province;
	}
	public String getCity() {
		return city;
	}
	public String getDistrict() {
		return district;
	}
	public String getStreet() {
		return street;
	}
	public String getCoordinate() {
		return coordinate;
	}
	public int getParkId() {
		return parkId;
	}
	public double getPrice() {
		return price;
	}
	public String getTenancy() {
		return tenancy;
	}
	public String getContact() {
		return contact;
	}
	public String getNumber() {
		return number;
	}
	public String getDescription() {
		return description;
	}
	public Date getDate() {
		return date;
	}
	public void setId(int id) {
		Id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public void setTenancy(String tenancy) {
		this.tenancy = tenancy;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	

	

}

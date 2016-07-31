package com.park.model;

public class FeeCriterion {
	
	private int id;
	private String name;
	private int freeMins;
	private double startExpense;
	private double timeoutPrice;
	private int timeoutPriceInterval;
	private double maxExpense;
	private String nightStartTime;
	private String nightEndTime;
	private int isOneTimeExpense;
	private double oneTimeExpense;
	
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFreeMins() {
		return freeMins;
	}
	public void setFreeMins(int freeMins) {
		this.freeMins = freeMins;
	}
	public double getStartExpense() {
		return startExpense;
	}
	public void setStartExpense(double startExpense) {
		this.startExpense = startExpense;
	}
	public double getTimeoutPrice() {
		return timeoutPrice;
	}
	public void setTimeoutPrice(double timeoutPrice) {
		this.timeoutPrice = timeoutPrice;
	}
	public int getTimeoutPriceInterval() {
		return timeoutPriceInterval;
	}
	public void setTimeoutPriceInterval(int timeoutPriceInterval) {
		this.timeoutPriceInterval = timeoutPriceInterval;
	}
	public double getMaxExpense() {
		return maxExpense;
	}
	public void setMaxExpense(double maxExpense) {
		this.maxExpense = maxExpense;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNightStartTime() {
		return nightStartTime;
	}
	public void setNightStartTime(String nightStartTime) {
		this.nightStartTime = nightStartTime;
	}
	public String getNightEndTime() {
		return nightEndTime;
	}
	public void setNightEndTime(String nightEndTime) {
		this.nightEndTime = nightEndTime;
	}
	public double getOneTimeExpense() {
		return oneTimeExpense;
	}
	public void setOneTimeExpense(double oneTimeExpense) {
		this.oneTimeExpense = oneTimeExpense;
	}
	public int getIsOneTimeExpense() {
		return isOneTimeExpense;
	}
	public void setIsOneTimeExpense(int isOneTimeExpense) {
		this.isOneTimeExpense = isOneTimeExpense;
	}
	

	
	
	

}

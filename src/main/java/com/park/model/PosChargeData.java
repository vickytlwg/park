package com.park.model;

import java.util.Date;

public class PosChargeData {

	int id;
	
	String cardNumber;
	
	int parkId;
	
	String parkDesc;
	
	String portNumber;
	
	boolean isEntrance;
	
	String operatorId;
	
	String posId;
	
	double chargeMoney;
	
	double paidMoney;
	
	double unPaidMoney;
	
	double changeMoney;
	
	int isOneTimeExpense;
	
	boolean paidCompleted;
	
	Date entranceDate;
	
	Date exitDate;
	
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getParkId() {
		return parkId;
	}

	public void setParkId(int parkId) {
		this.parkId = parkId;
	}

	public String getParkDesc() {
		return parkDesc;
	}

	public void setParkDesc(String parkDesc) {
		this.parkDesc = parkDesc;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public boolean isEntrance() {
		return isEntrance;
	}

	public void setEntrance(boolean isEntrance) {
		this.isEntrance = isEntrance;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public double getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(double chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public double getPaidMoney() {
		return paidMoney;
	}

	public void setPaidMoney(double paidMoney) {
		this.paidMoney = paidMoney;
	}

	public double getUnPaidMoney() {
		return unPaidMoney;
	}

	public void setUnPaidMoney(double unPaidMoney) {
		this.unPaidMoney = unPaidMoney;
	}

	public double getChangeMoney() {
		return changeMoney;
	}

	public void setChangeMoney(double changeMoney) {
		this.changeMoney = changeMoney;
	}

	public Date getEntranceDate() {
		return entranceDate;
	}

	public void setEntranceDate(Date entranceDate) {
		this.entranceDate = entranceDate;
	}

	public Date getExitDate() {
		return exitDate;
	}

	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
	}


	public int getIsOneTimeExpense() {
		return isOneTimeExpense;
	}

	public void setIsOneTimeExpense(int isOneTimeExpense) {
		this.isOneTimeExpense = isOneTimeExpense;
	}

	public boolean isPaidCompleted() {
		return paidCompleted;
	}

	public void setPaidCompleted(boolean paidCompleted) {
		this.paidCompleted = paidCompleted;
	}
	
	
	
}

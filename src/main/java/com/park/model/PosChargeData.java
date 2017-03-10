package com.park.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PosChargeData {

	int id;
	
	String cardNumber;
	
	int parkId;
	
	String parkDesc;
	
	String portNumber;
	
	String rejectReason="";

    int payType=2;
	
	boolean isEntrance;
	
	String operatorId;
	
	String posId;
	
	double chargeMoney;
	
	double paidMoney;
	
	double unPaidMoney;
	
	double givenMoney;

	double changeMoney;
	
	int isOneTimeExpense;
	
	boolean paidCompleted=false;
	
	boolean isLargeCar;
	
	Date entranceDate;
	
	Date exitDate;

	String url="";
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	} 
	
	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
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

	
	public double getGivenMoney() {
		return givenMoney;
	}

	public void setGivenMoney(double givenMoney) {
		this.givenMoney = givenMoney;
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

	public void setEntranceDate(String entranceDate) throws ParseException {
		this.entranceDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entranceDate);
	}
	
	public Date getExitDate() {
		return exitDate;
	}

	public void setExitDate(String exitDate) throws ParseException {
		this.exitDate =new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
	}
	public void setExitDate1(Date exitDate){
		this.exitDate =exitDate;
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

	public boolean getIsLargeCar() {
		return isLargeCar;
	}

	public void setIsLargeCar(boolean isLargeCar) {
		this.isLargeCar = isLargeCar;
	}	
	
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
}

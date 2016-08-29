package com.park.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Feeoperator {
    private Integer id;

    private String account;

    private String name;

    private String phone;

    private Boolean signstatus;

    private Date lastsigndate;

    private Integer laststreetid;

    private String lastposnum;

    private Date registerdate;

    private String other;
    
    private String number;
    
    private String streetname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Boolean getSignstatus() {
        return signstatus;
    }

    public void setSignstatus(Boolean signstatus) {
        this.signstatus = signstatus;
    }

    public Date getLastsigndate() {
        return lastsigndate;
    }

    public void setLastsigndate(String lastsigndate) throws ParseException {
        this.lastsigndate = new SimpleDateFormat(Constants.DATEFORMAT).parse(lastsigndate);
    }

    public Integer getLaststreetid() {
        return laststreetid;
    }

    public void setLaststreetid(Integer laststreetid) {
        this.laststreetid = laststreetid;
    }

    public String getLastposnum() {
        return lastposnum;
    }

    public void setLastposnum(String lastposnum) {
        this.lastposnum = lastposnum == null ? null : lastposnum.trim();
    }

    public Date getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(String registerdate) throws ParseException {
        this.registerdate = new SimpleDateFormat(Constants.DATEFORMAT).parse(registerdate);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }
    
    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname == null ? null : streetname.trim();
    }
    
    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other == null ? null : other.trim();
    }
}
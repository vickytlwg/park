package com.park.model;

public class FeeCriterion {
	
	private Integer id;

    private String name;

    private Integer freemins=0;

    private Float step1price=(float) 1;

    private Float step2price=(float) 2;

    private Float step1pricelarge=(float) 2;

    private Float step2pricelarge=(float) 4;

    private Integer step1capacity=60;

    private Integer step2capacity=480;

    private Integer timeoutpriceinterval=15;

    private Float maxexpense=(float) 9999;

    private String nightstarttime;

    private String nightendtime;

    private Integer isonetimeexpense=0;

    private Float onetimeexpense;
    
    private String explaination;


	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getFreemins() {
        return freemins;
    }

    public void setFreemins(Integer freemins) {
        this.freemins = freemins;
    }

    public Float getStep1price() {
        return step1price;
    }

    public void setStep1price(Float step1price) {
        this.step1price = step1price;
    }

    public Float getStep2price() {
        return step2price;
    }

    public void setStep2price(Float step2price) {
        this.step2price = step2price;
    }

    public Float getStep1pricelarge() {
        return step1pricelarge;
    }

    public void setStep1pricelarge(Float step1pricelarge) {
        this.step1pricelarge = step1pricelarge;
    }

    public Float getStep2pricelarge() {
        return step2pricelarge;
    }

    public void setStep2pricelarge(Float step2pricelarge) {
        this.step2pricelarge = step2pricelarge;
    }

    public Integer getStep1capacity() {
        return step1capacity;
    }

    public void setStep1capacity(Integer step1capacity) {
        this.step1capacity = step1capacity;
    }

    public Integer getStep2capacity() {
        return step2capacity;
    }

    public void setStep2capacity(Integer step2capacity) {
        this.step2capacity = step2capacity;
    }

    public Integer getTimeoutpriceinterval() {
        return timeoutpriceinterval;
    }

    public void setTimeoutpriceinterval(Integer timeoutpriceinterval) {
        this.timeoutpriceinterval = timeoutpriceinterval;
    }

    public Float getMaxexpense() {
        return maxexpense;
    }

    public void setMaxexpense(Float maxexpense) {
        this.maxexpense = maxexpense;
    }

    public String getNightstarttime() {
        return nightstarttime;
    }

    public void setNightstarttime(String nightstarttime) {
        this.nightstarttime = nightstarttime == null ? null : nightstarttime.trim();
    }

    public String getNightendtime() {
        return nightendtime;
    }

    public void setNightendtime(String nightendtime) {
        this.nightendtime = nightendtime == null ? null : nightendtime.trim();
    }

    public Integer getIsonetimeexpense() {
        return isonetimeexpense;
    }

    public void setIsonetimeexpense(Boolean isonetimeexpense) {
    	if (isonetimeexpense==false) {
    		this.isonetimeexpense = 0;
		}
        else {
			this.isonetimeexpense=1;
		}
    }

    public Float getOnetimeexpense() {
        return onetimeexpense;
    }

    public void setOnetimeexpense(Float onetimeexpense) {
        this.onetimeexpense = onetimeexpense;
    }
	

    public String getExplaination() {
		return explaination;
	}

	public void setExplaination(String explain) {
		this.explaination = explain;
	}
	
	

}

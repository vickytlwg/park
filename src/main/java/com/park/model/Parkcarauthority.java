package com.park.model;

public class Parkcarauthority {
    private Integer id;

    private Integer parkid;

    private Boolean typea=true;

    private Boolean typeb=true;

    private Boolean typec=true;

    private Boolean typed=true;

    private Boolean month=true;

    private String other;

    private String other2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParkid() {
        return parkid;
    }

    public void setParkid(Integer parkid) {
        this.parkid = parkid;
    }

    public Boolean getTypea() {
        return typea;
    }

    public void setTypea(Boolean typea) {
        this.typea = typea;
    }

    public Boolean getTypeb() {
        return typeb;
    }

    public void setTypeb(Boolean typeb) {
        this.typeb = typeb;
    }

    public Boolean getTypec() {
        return typec;
    }

    public void setTypec(Boolean typec) {
        this.typec = typec;
    }

    public Boolean getTyped() {
        return typed;
    }

    public void setTyped(Boolean typed) {
        this.typed = typed;
    }

    public Boolean getMonth() {
        return month;
    }

    public void setMonth(Boolean month) {
        this.month = month;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other == null ? null : other.trim();
    }

    public String getOther2() {
        return other2;
    }

    public void setOther2(String other2) {
        this.other2 = other2 == null ? null : other2.trim();
    }
}
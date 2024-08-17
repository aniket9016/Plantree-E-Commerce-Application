package com.weare.plantree.Model;

public class ORDERMODEL {
    private String totalamt,name,phone,adress,city,date,time,state;

    public ORDERMODEL() {
    }

    public ORDERMODEL(String totalamt, String name, String phone, String adress, String city, String date, String time, String state) {
        this.totalamt = totalamt;
        this.name = name;
        this.phone = phone;
        this.adress = adress;
        this.city = city;
        this.date = date;
        this.time = time;
        this.state = state;
    }

    public String getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(String totalamt) {
        this.totalamt = totalamt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

package com.muskankataria2408.blooddonationapp.models;

public class Model {
    String name, bloodGroup,mobile,hospitalAdress,landMark,token,userid;

    public Model(String name,String bloodGroup,String mobile,String hospitalAdress,String landMark,String token,String userid){
        this.name=name;
        this.bloodGroup=bloodGroup;
        this.mobile=mobile;
        this.hospitalAdress=hospitalAdress;
        this.landMark=landMark;
        this.token=token;
        this.userid=userid;

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Model() {
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHospitalAdress() {
        return hospitalAdress;
    }

    public void setHospitalAdress(String hospitalAdress) {
        this.hospitalAdress = hospitalAdress;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

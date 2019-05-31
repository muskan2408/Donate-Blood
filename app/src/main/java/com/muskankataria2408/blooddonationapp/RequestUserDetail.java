package com.muskankataria2408.blooddonationapp;

import java.util.ArrayList;

class RequestUserDetail {

    private  String  name,mobile,token,bloodgroup,hospitalAddress,landmark,userId;
    public ArrayList<NotificationListModel> notificationList=new ArrayList<>();
    public class NotificationListModel{
        public String name;
        public String mobile;
        public String bloodgroup;
        public String hospitalAddress;
        public String landmark;
        public String userId;


    }
//
    public RequestUserDetail(String name, String mobile, String bloodgroup, String hospitalAddress, String landmark, String userId) {
        this.name = name;
        this.mobile = mobile;
        this.token = token;
        this.bloodgroup = bloodgroup;
        this.hospitalAddress = hospitalAddress;
        this.landmark = landmark;
        this.userId =userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}

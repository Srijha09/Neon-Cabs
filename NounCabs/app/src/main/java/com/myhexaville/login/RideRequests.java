package com.myhexaville.login;

import java.io.Serializable;

public class RideRequests implements Serializable {
    private String pickupPoint;
    private String dropPoint;
    private String distance;
    private String otp;
    private String timeStamp;
    private String fare;
    private String customerPhoneNumber;

    public RideRequests() {
        pickupPoint="some pickup";
        dropPoint="some drop";
        distance="0";
        otp="0000";
        timeStamp="some timeStamp";
        fare="0";
        customerPhoneNumber="111111111";

    }

    public RideRequests(String pickupPoint, String dropPoint, String distance, String otp, String timeStamp, String fare, String customerPhoneNumber) {
        this.pickupPoint = pickupPoint;
        this.dropPoint = dropPoint;
        this.distance = distance;
        this.otp = otp;
        this.timeStamp = timeStamp;
        this.fare = fare;
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public String getDropPoint() {
        return dropPoint;
    }

    public void setDropPoint(String dropPoint) {
        this.dropPoint = dropPoint;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }
}

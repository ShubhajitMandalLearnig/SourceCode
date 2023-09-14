package com.TwiloOtp.Twilio.Dto;

public class ReceivedDto {
    private String phoneNumber;
    private String enteredOTP;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEnteredOTP() {
        return enteredOTP;
    }

    public void setEnteredOTP(String enteredOTP) {
        this.enteredOTP = enteredOTP;
    }
}

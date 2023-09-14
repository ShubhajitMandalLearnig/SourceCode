package com.TwiloOtp.Twilio.Timer;


import java.util.concurrent.TimeUnit;

public class OTPData {
    private String otp;
    private long creationTimeMillis;
    private static final long EXPIRATION_TIME_SECONDS = 60;

    public OTPData(String otp, long creationTimeMillis) {
        this.otp = otp;
        this.creationTimeMillis = creationTimeMillis;
    }

    public String getOtp() {
        return otp;
    }

    public boolean isExpired() {
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedTimeSeconds = TimeUnit.MILLISECONDS.toSeconds(currentTimeMillis - creationTimeMillis);
        return elapsedTimeSeconds >= EXPIRATION_TIME_SECONDS;
    }
}

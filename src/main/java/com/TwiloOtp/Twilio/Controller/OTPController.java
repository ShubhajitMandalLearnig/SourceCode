package com.TwiloOtp.Twilio.Controller;


import com.TwiloOtp.Twilio.Dto.ReceivedDto;
import com.TwiloOtp.Twilio.Dto.SentDto;
import com.TwiloOtp.Twilio.Service.SMSService;
import com.TwiloOtp.Twilio.Timer.OTPData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.lang3.RandomStringUtils;


import java.util.HashMap;
import java.util.Map;

@RestController
public class OTPController {

    @Autowired
    private SMSService smsService;

    // Store OTPs and their creation timestamps temporarily
    private Map<String, OTPData> otpStorage = new HashMap<>();

    // http://localhost:8080/send-otp?phoneNumber=%2B918486702021
    // (%2B) == "+"
    @PostMapping("/send-otp")
    public String sendOTP(@RequestBody SentDto sentDto) {
        // Generate OTP (you can use a library like Apache Commons Text)
        String otp = generateOTP();

        // Store OTP along with the timestamp
        long currentTimeMillis = System.currentTimeMillis();
        otpStorage.put(sentDto.getPhoneNumber(), new OTPData(otp, currentTimeMillis));

        // Send OTP via SMS
        smsService.sendSMS(sentDto.getPhoneNumber(), "Your OTP is: " + otp);

        return "OTP sent successfully!";
    }

    // http://localhost:8080/verify-otp?phoneNumber=%2B918486702021&enteredOTP=083406
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody ReceivedDto receivedDto) {
        // Retrieve the stored OTPData object
        OTPData otpData = otpStorage.get(receivedDto.getPhoneNumber());

        // Check if the OTP data exists and is not expired
        if (otpData != null && !otpData.isExpired()) {
            String storedOTP = otpData.getOtp();

            // Check if the entered OTP matches the stored OTP
            if (storedOTP != null && storedOTP.equals(receivedDto.getEnteredOTP())) {
                // If verification is successful, remove the OTPData from storage
                otpStorage.remove(receivedDto.getPhoneNumber());

                // Redirect to the welcome page or return a success message
                return ResponseEntity.ok("Verification successful! Redirecting to welcome page.");
            }
        }

        // If verification fails or the OTP is expired, return an error response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP verification failed.");
    }

    private String generateOTP() {
        // Generate a random OTP, for example, using Apache Commons Text
        return RandomStringUtils.randomNumeric(6); // 6-digit OTP
    }
}
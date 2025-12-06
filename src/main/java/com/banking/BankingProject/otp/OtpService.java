package com.banking.BankingProject.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final JavaMailSender mailSender;
    private final OtpRepository otpRepository;

    public void generateOtp(String email){
        String otp = String.format("%06d", new Random().nextInt(999999));

        OtpVerification otpData = new OtpVerification();
        otpData.setEmail(email);
        otpData.setOtp(otp);
        otpData.setExpiry(LocalDateTime.now().plusMinutes(5));
        otpData.setUsed(false);

        otpRepository.save(otpData);
        sendOtpMail(email, otp);
    }

    private void sendOtpMail(String email, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Banking Login OTP");
        message.setText("Your OTP: " + otp + "\nValid for 5 minutes.");

        mailSender.send(message);
    }

    public boolean verifyOtp(String email, String otp){
        return otpRepository.findValidOtp(email, otp, LocalDateTime.now()).isPresent();
    }
}

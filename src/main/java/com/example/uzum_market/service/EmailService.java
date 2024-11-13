package com.example.uzum_market.service;

import com.example.uzum_market.repository.VerificationUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VerificationUrlRepository verificationUrlRepository;


    public void sendVerificationEmail(String email, String token) {
        String url = "http://localhost:8080/auth/verify?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Verification ");
        message.setText("Please verify your email by clicking the link: " + url);
        message.setFrom("tolibmasyupov0515@gmail.com");

        mailSender.send(message);
    }

    public void sendVerificationEmailPassword(String email, String token) {
        String url = "http://localhost:8080/auth/verify-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Verification ");
        message.setText("Please verify your email by clicking the link for New Password: " + url);
        message.setFrom("tolibmasyupov0515@gmail.com");

        mailSender.send(message);
    }

//    public void signup(String email) {
//       // Token valid for 24 hours
//
//        VerificationUrl verificationUrl = new VerificationUrl();
//        verificationUrl.setEmail(email);
//        verificationUrl.setToken(token);
//        verificationUrl.setExpiryDate(expiryDate);
//        verificationUrlRepository.save(verificationUrl);
//
//        sendVerificationEmail(email, token);
//    }


    //    public String generateVerificationCode() {
//        Random random = new Random();
//        int code = random.nextInt(999999); // 6 xonali raqam yaratish
//        return String.format("%06d", code);
//    }


}
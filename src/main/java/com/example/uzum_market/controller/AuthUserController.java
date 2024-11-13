package com.example.uzum_market.controller;

import com.example.uzum_market.entity.User;
import com.example.uzum_market.payload.dto.LoginDto;
import com.example.uzum_market.payload.dto.PasswordDto;
import com.example.uzum_market.payload.dto.ResendVerificationDto;
import com.example.uzum_market.payload.dto.UserDto;
import com.example.uzum_market.security.JwtProvider;
import com.example.uzum_market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/auth")
public class AuthUserController {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private UserService userService;


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public HttpEntity<?> loginPage(@RequestBody LoginDto loginDto) {
        return userService.loginPage(loginDto);
    }

    @PostMapping("/resend-verification")
    public HttpEntity<?> resendVerification(@RequestBody ResendVerificationDto verificationDto) {
        return userService.resendVerification(verificationDto);
    }

    @PostMapping("/forgot-password")
    public HttpEntity<?> forgotPassword(@RequestBody ResendVerificationDto email) {
        return userService.forgotPassword(email);
    }

    @PutMapping("/forgot-password-write-new-password")
    public HttpEntity<?> forgotPassword(@RequestBody PasswordDto passwordDto, @RequestParam("token") String token, HttpServletResponse response) {
        return userService.forgotPasswordWriteNewPassword(passwordDto, token, response);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/create")
    public HttpEntity<?> signUp(@RequestBody UserDto userDto) {
        return userService.singUp(userDto);
    }

    @GetMapping("/verify")
    public HttpEntity<?> verifyEmail(@RequestParam("token") String token, HttpServletResponse response) {
        return userService.verifyEmail(token, response);
    }

    @GetMapping("/verify-password")
    public HttpEntity<?> verifyPassword(@RequestParam("token") String token, HttpServletResponse responce) {
        return userService.verifyPassword(token, responce);
    }

    @GetMapping("/about-me")
    public HttpEntity<?> aboutMe(@RequestBody String token) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(200).body(user);

    }

    @GetMapping("/all-users ")
    public HttpEntity<?> getAllUsers() {
        return userService.getAll();
    }


}

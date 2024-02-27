package com.vishal.EmailSender.controller;

import com.vishal.EmailSender.dto.EmailDto;
import com.vishal.EmailSender.dto.LoginDto;
import com.vishal.EmailSender.dto.RegisterDto;
import com.vishal.EmailSender.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
public class MyController {

    @Autowired
    public JavaMailSender javaMailSender;

    @Autowired
    public UserService userService;

    @PostMapping("/sent-email")
    public String sendEmail(@RequestBody EmailDto emailDto){

        SimpleMailMessage simpleMailMessage= new SimpleMailMessage();
        simpleMailMessage.setTo(emailDto.getTo());
        simpleMailMessage.setSubject(emailDto.getSubject());
        simpleMailMessage.setText(emailDto.getText());

        javaMailSender.send(simpleMailMessage);

        return "Email sent sucessfully";
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto) throws MessagingException {
        return new ResponseEntity<>(userService.register(registerDto), HttpStatus.OK);
    }

    @PutMapping("/verify-account")
    public  ResponseEntity<String> verifyAccount(@RequestParam String email , @RequestParam String otp){
        return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
    }

    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(userService.login(loginDto), HttpStatus.OK);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetpassword(@RequestParam String email) throws MessagingException {
        return new ResponseEntity<>(userService.forgetPassword(email), HttpStatus.OK);
    }

    @PostMapping("/set-password")
    public ResponseEntity<String> setPassword(@RequestParam String email,@RequestHeader String newPAssword) throws MessagingException {
        return new ResponseEntity<>(userService.setpassword(email,newPAssword), HttpStatus.OK);
    }

}

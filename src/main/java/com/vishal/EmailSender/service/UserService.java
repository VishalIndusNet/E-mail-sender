package com.vishal.EmailSender.service;

import com.vishal.EmailSender.dto.LoginDto;
import com.vishal.EmailSender.dto.RegisterDto;
import com.vishal.EmailSender.entity.User;
import com.vishal.EmailSender.repo.UserRepository;
import com.vishal.EmailSender.utils.EmailUtil;
import com.vishal.EmailSender.utils.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private EmailUtil emailUtil;

    public String register(RegisterDto registerDto) throws MessagingException {
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(registerDto.getEmail(), otp);
        }
         catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);

        return "User registration successful";
    }

    public String verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));

        if(user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (2*60))
        {
            user.setActive(true);
            userRepository.save(user);
            return "OTP verified you can login";
        }
        return "Please regenerate otp and try again";
    }

        public String regenerateOtp(String email) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
            String otp = otpUtil.generateOtp();
            try {
                emailUtil.sendOtpEmail(email, otp);
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to send otp please try again");
            }

            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            userRepository.save(user);
            return "Email sent... please verify account within 1 minute";
    }
    public String login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(
                        () -> new RuntimeException("User not found with this email: " + loginDto.getEmail()));
        if (!loginDto.getPassword().equals(user.getPassword())) {
            return "Password is incorrect";
        } else if (!user.isActive()) {
            return "your account is not verified";
        }
        return "Login successful";
    }

    public String forgetPassword(String email) throws MessagingException {
    User user = userRepository.findByEmail(email)
            .orElseThrow(
                    () -> new RuntimeException("User not found with this email: " + email));

        try {
            emailUtil.sendSetpasswordEmail(email);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        return "please check your email to verify otp";

    }

    public String setpassword(String email, String newPAssword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("User not found with this email: " + email));

        user.setPassword(newPAssword);
        userRepository.save(user);
        return "new Password save successfully";
    }
}

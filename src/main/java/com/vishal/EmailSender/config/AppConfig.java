package com.vishal.EmailSender.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class AppConfig {

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private String mailPort;
    @Value("${spring.mail.username}")
    private  String mailUserName;
    @Value("${spring.mail.password}")
    private  String mailPassword;

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailHost);
        javaMailSender.setPort(Integer.parseInt(mailPort));
        javaMailSender.setUsername(mailUserName);
        javaMailSender.setPassword(mailPassword);

        Properties javaMailProperties = javaMailSender.getJavaMailProperties();
        javaMailProperties.put("mail.smtp.starttls.enable","true");

        return javaMailSender;
    }
}

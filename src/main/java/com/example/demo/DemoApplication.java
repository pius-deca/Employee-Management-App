package com.example.demo;

import com.example.demo.security.SecurityConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

import static com.example.demo.security.SecurityConstants.PASSWORD;
import static com.example.demo.security.SecurityConstants.USERNAME;

@SpringBootApplication
public class DemoApplication {

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailMessage = new JavaMailSenderImpl();
        mailMessage.setHost("smtp.gmail.com");
        mailMessage.setPort(587);
        mailMessage.setUsername(USERNAME);
        mailMessage.setPassword(PASSWORD);

        Properties properties = mailMessage.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        return mailMessage;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}


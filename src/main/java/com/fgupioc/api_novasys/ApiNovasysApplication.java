package com.fgupioc.api_novasys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ApiNovasysApplication implements CommandLineRunner {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String password = "12345678";
        System.out.println("Hash: " + passwordEncoder.encode(password));
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiNovasysApplication.class, args);
    }

}

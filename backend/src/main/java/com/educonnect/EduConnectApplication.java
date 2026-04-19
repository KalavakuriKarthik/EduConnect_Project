package com.educonnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class EduConnectApplication {

    private static final Logger logger = LoggerFactory.getLogger(EduConnectApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EduConnectApplication.class, args);
        logger.info("EduConnect Application started successfully!");
    }
}

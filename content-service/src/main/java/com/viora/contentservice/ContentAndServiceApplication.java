package com.viora.contentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@SpringBootApplication
public class ContentAndServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentAndServiceApplication.class);
    }
}

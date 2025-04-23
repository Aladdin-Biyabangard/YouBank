package com.aladdin.youbank001;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class YouBank001Application {

    public static void main(String[] args) {
        SpringApplication.run(YouBank001Application.class, args);
    }

}

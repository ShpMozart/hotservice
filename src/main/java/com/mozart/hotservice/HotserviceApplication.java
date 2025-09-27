package com.mozart.hotservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HotserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotserviceApplication.class, args);
    }

}

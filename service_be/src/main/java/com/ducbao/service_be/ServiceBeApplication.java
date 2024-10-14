package com.ducbao.service_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ServiceBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBeApplication.class, args);
    }

}

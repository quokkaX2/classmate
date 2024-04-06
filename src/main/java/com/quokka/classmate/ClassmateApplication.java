package com.quokka.classmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ClassmateApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassmateApplication.class, args);
    }

}

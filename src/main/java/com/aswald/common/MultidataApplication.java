package com.aswald.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MultidataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultidataApplication.class, args);
    }

}

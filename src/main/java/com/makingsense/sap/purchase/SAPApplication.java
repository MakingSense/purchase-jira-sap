package com.makingsense.sap.purchase;

import com.makingsense.sap.purchase.configuration.SAPConfigurationProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Entry point of the Application.
 */
@SpringBootApplication
@EnableConfigurationProperties(SAPConfigurationProperties.class)
public class SAPApplication {

    public static void main(String[] args) {
        SpringApplication.run(SAPApplication.class, args);
    }

}

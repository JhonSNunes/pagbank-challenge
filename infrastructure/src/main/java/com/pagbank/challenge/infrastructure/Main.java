package com.pagbank.challenge.infrastructure;

import com.pagbank.challenge.application.customer.create.CreateCustomerUseCase;
import com.pagbank.challenge.infrastructure.configuration.WebServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }

    @Bean
    @DependsOnDatabaseInitialization
    public ApplicationRunner runner(@Autowired CreateCustomerUseCase useCase) {
        return args -> {
        };
    }
}
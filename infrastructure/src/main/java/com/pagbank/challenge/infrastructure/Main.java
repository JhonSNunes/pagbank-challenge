package com.pagbank.challenge.infrastructure;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.infrastructure.configuration.WebServerConfig;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }

//    @Bean
//    public ApplicationRunner runner(CustomerRepository repository) {
//        return args -> {
//            List<CustomerJpaEntity> all = repository.findAll();
//
//            Customer customer = Customer.registerCustomer(
//                    "José",
//                    33,
//                    "Masculino",
//                    "9999999",
//                    "Joinville",
//                    "Santa Catarina",
//                    "Brasil",
//                    "Rua blabla",
//                    "222",
//                    "23232323"
//            );
//
//            repository.saveAndFlush(CustomerJpaEntity.from(customer));
//
//            repository.deleteAll();
//        };
//    }
}
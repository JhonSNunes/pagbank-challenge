package com.pagbank.challenge.infrastructure.customer;

import com.pagbank.challenge.application.customer.create.CreateCustomerCommand;
import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.infrastructure.MySQLGatewayTest;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class CustomerMySQLGatewayTest {

    @Autowired
    private CustomerMySQLGateway customerMySQLGateway;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void givenAValidCustomer_whenCallsCreate_shouldReturnANewCustomer() {
        final var expectedName = "José";
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";
        final var expectedIsActive = true;

        final var customer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        Assertions.assertEquals(0, customerRepository.count());

        final var customerId = customer.getId();
        final var actualCustomer = customerMySQLGateway.create(customer);

        Assertions.assertEquals(1, customerRepository.count());
        Assertions.assertEquals(customerId, actualCustomer.getId());
        Assertions.assertEquals(expectedName, actualCustomer.getName());
        Assertions.assertEquals(expectedAge, actualCustomer.getAge());
        Assertions.assertEquals(expectedGenre, actualCustomer.getGenre());
        Assertions.assertEquals(expectedPhone, actualCustomer.getPhone());
        Assertions.assertEquals(expectedCity, actualCustomer.getCity());
        Assertions.assertEquals(expectedState, actualCustomer.getState());
        Assertions.assertEquals(expectedCountry, actualCustomer.getCountry());
        Assertions.assertEquals(expectedAddress, actualCustomer.getAddress());
        Assertions.assertEquals(expectedNumber, actualCustomer.getNumber());
        Assertions.assertEquals(expectedZipcode, actualCustomer.getZipcode());
        Assertions.assertEquals(expectedIsActive, actualCustomer.isActive());
        Assertions.assertEquals(customer.getCreatedAt(), actualCustomer.getCreatedAt());
        Assertions.assertEquals(customer.getUpdatedAt(), actualCustomer.getUpdatedAt());
        Assertions.assertNull(actualCustomer.getDeletedAt());

        final var persistedCustomer = customerRepository.findById(customerId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedCustomer.getName());
        Assertions.assertEquals(expectedAge, persistedCustomer.getAge());
        Assertions.assertEquals(expectedGenre, persistedCustomer.getGenre());
        Assertions.assertEquals(expectedPhone, persistedCustomer.getPhone());
        Assertions.assertEquals(expectedCity, persistedCustomer.getCity());
        Assertions.assertEquals(expectedState, persistedCustomer.getState());
        Assertions.assertEquals(expectedCountry, persistedCustomer.getCountry());
        Assertions.assertEquals(expectedAddress, persistedCustomer.getAddress());
        Assertions.assertEquals(expectedNumber, persistedCustomer.getNumber());
        Assertions.assertEquals(expectedZipcode, persistedCustomer.getZipcode());
        Assertions.assertEquals(expectedIsActive, persistedCustomer.getActive());
        Assertions.assertEquals(customer.getCreatedAt(), persistedCustomer.getCreatedAt());
        Assertions.assertEquals(customer.getUpdatedAt(), persistedCustomer.getUpdatedAt());
        Assertions.assertEquals(customer.getDeletedAt(), persistedCustomer.getDeletedAt());
        Assertions.assertNull(persistedCustomer.getDeletedAt());
    }
}

package com.pagbank.challenge.infrastructure.customer;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.customer.CustomerSearchQuery;
import com.pagbank.challenge.MySQLGatewayTest;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    @Test
    public void givenAValidCustomer_whenCallsUpdate_shouldReturnAUpdatedCustomer() {
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
                "Josélindo da Silva",
                34,
                "masculino",
                "(47) 99900-4583",
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        Assertions.assertEquals(0, customerRepository.count());

        customerRepository.saveAndFlush(CustomerJpaEntity.from(customer));

        Assertions.assertEquals(1, customerRepository.count());

        final var updatedCustomer = customer.clone().update(
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

        final var customerId = customer.getId();
        final var actualCustomer = customerMySQLGateway.update(updatedCustomer);

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
        Assertions.assertTrue(customer.getUpdatedAt().isBefore(actualCustomer.getUpdatedAt()));
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
        Assertions.assertEquals(actualCustomer.getUpdatedAt(), persistedCustomer.getUpdatedAt());
        Assertions.assertTrue(customer.getUpdatedAt().isBefore(persistedCustomer.getUpdatedAt()));
        Assertions.assertNull(persistedCustomer.getDeletedAt());
    }

    @Test
    public void givenAPersistedCustomer_whenCallsDeleteById_shouldDeleteCustomer() {
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

        customerRepository.saveAndFlush(CustomerJpaEntity.from(customer));

        Assertions.assertEquals(1, customerRepository.count());

        final var customerId = customer.getId();

        customerMySQLGateway.deleteById(customerId);

        Assertions.assertEquals(0, customerRepository.count());
        Assertions.assertTrue(customerRepository.findById(customerId.getValue()).isEmpty());
    }

    @Test
    public void givenAInvalidCustomer_whenCallsDeleteById_shouldDoNothing() {
        Assertions.assertEquals(0, customerRepository.count());

        customerMySQLGateway.deleteById(CustomerID.from("invalid"));

        Assertions.assertEquals(0, customerRepository.count());
    }

    @Test
    public void givenAValidCustomer_whenCallsFindById_shouldReturnCustomer() {
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

        customerRepository.saveAndFlush(CustomerJpaEntity.from(customer));

        Assertions.assertEquals(1, customerRepository.count());

        final var customerId = customer.getId();
        final var actualCustomer = customerMySQLGateway.findById(customerId).get();

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
        Assertions.assertEquals(customer.getDeletedAt(), actualCustomer.getDeletedAt());
        Assertions.assertNull(actualCustomer.getDeletedAt());
    }

    @Test
    public void givenAInvalidCustomer_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, customerRepository.count());

        final var findResult = customerMySQLGateway.findById(CustomerID.from("invalid"));

        Assertions.assertTrue(findResult.isEmpty());
    }

    @Test
    public void givenPrePersistedCustomers_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var customer1 = Customer.registerCustomer("João", 45, "masculino", "(47) 99900-3333", "Joinville","Santa Catarina", "Brasil", "Rua teste da silva", "232333", "895952225");
        final var customer2 = Customer.registerCustomer("Maria", 23, "feminino", "(47) 99900-3124", "Joinville","Santa Catarina", "Brasil", "Rua teste da silva", "232333", "895952225");
        final var customer3 = Customer.registerCustomer("José", 76, "masculino", "(47) 99900-3345", "Joinville","Santa Catarina", "Brasil", "Rua teste da silva", "232333", "895952225");

        Assertions.assertEquals(0, customerRepository.count());

        customerRepository.saveAll(List.of(
                CustomerJpaEntity.from(customer1),
                CustomerJpaEntity.from(customer2),
                CustomerJpaEntity.from(customer3)
        ));

        Assertions.assertEquals(3, customerRepository.count());

        final var query = new CustomerSearchQuery(0, 1, "", "name", "asc");
        final var actualResult = customerMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(customer3.getId(), actualResult.items().getFirst().getId());
    }

    @Test
    public void givenEmptyCustomersTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, customerRepository.count());

        final var query = new CustomerSearchQuery(0, 1, "", "name", "asc");
        final var actualResult = customerMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(0, actualResult.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var customer1 = Customer.registerCustomer("João", 45, "masculino", "(47) 99900-3333", "Joinville","Santa Catarina", "Brasil", "Rua teste da silva", "232333", "895952225");
        final var customer2 = Customer.registerCustomer("Maria", 23, "feminino", "(47) 99900-3124", "Joinville","Santa Catarina", "Brasil", "Rua teste da silva", "232333", "895952225");
        final var customer3 = Customer.registerCustomer("José", 76, "masculino", "(47) 99900-3345", "Joinville","Santa Catarina", "Brasil", "Rua teste da silva", "232333", "895952225");

        Assertions.assertEquals(0, customerRepository.count());

        customerRepository.saveAll(List.of(
                CustomerJpaEntity.from(customer1),
                CustomerJpaEntity.from(customer2),
                CustomerJpaEntity.from(customer3)
        ));

        Assertions.assertEquals(3, customerRepository.count());

        var query = new CustomerSearchQuery(0, 1, "", "name", "asc");
        var actualResult = customerMySQLGateway.findAll(query);

        // Page 0
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(customer3.getId(), actualResult.items().getFirst().getId());

        // Page 1
        expectedPage = 1;

        query = new CustomerSearchQuery(1, 1, "", "name", "asc");
        actualResult = customerMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(customer1.getId(), actualResult.items().getFirst().getId());

        // Page 2
        expectedPage = 2;

        query = new CustomerSearchQuery(2, 1, "", "name", "asc");
        actualResult = customerMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(customer2.getId(), actualResult.items().getFirst().getId());
    }

    @Test
    public void givenPrePersistedCustomersAndSomeTerm_whenCallsFindAllAndTermsMatchsCustomersName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var customer1 = Customer.registerCustomer("João", 45, "masculino", "(47) 99900-3333", "Joinville","Santa Catarina", "Brasil", "Rua teste da silva", "232333", "895952225");
        final var customer2 = Customer.registerCustomer("Maria", 23, "feminino", "(47) 99900-3124", "Joinville","Santa Catarina", "Brasil", "Rua teste da silva", "232333", "895952225");
        final var customer3 = Customer.registerCustomer("José", 76, "masculino", "(47) 99900-3345", "Joinville","Santa Catarina", "Brasil", "Rua teste da silva", "232333", "895952225");

        Assertions.assertEquals(0, customerRepository.count());

        customerRepository.saveAll(List.of(
                CustomerJpaEntity.from(customer1),
                CustomerJpaEntity.from(customer2),
                CustomerJpaEntity.from(customer3)
        ));

        Assertions.assertEquals(3, customerRepository.count());

        final var query = new CustomerSearchQuery(0, 1, "maria", "name", "asc");
        final var actualResult = customerMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(customer2.getId(), actualResult.items().getFirst().getId());
    }
}

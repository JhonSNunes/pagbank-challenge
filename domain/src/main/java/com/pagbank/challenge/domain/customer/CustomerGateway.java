package com.pagbank.challenge.domain.customer;

import com.pagbank.challenge.domain.pagination.Pagination;

import java.util.Optional;

public interface CustomerGateway {
    Customer create(Customer customer);

    Customer update(Customer customer);

    void deleteById(CustomerID id);

    Optional<Customer> findById(CustomerID id);

    Pagination<Customer> findAll(CustomerSearchQuery query);
}

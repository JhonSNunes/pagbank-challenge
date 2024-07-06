package com.pagbank.challenge.infrastructure.customer;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerGateway;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.customer.CustomerSearchQuery;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerMySQLGateway implements CustomerGateway {
    private final CustomerRepository repository;

    public CustomerMySQLGateway(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer create(final Customer customer) {
        return this.repository.save(CustomerJpaEntity.from(customer)).toAggregate();
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }

    @Override
    public void deleteById(CustomerID id) {

    }

    @Override
    public Optional<Customer> findById(CustomerID id) {
        return Optional.empty();
    }

    @Override
    public Pagination<Customer> findAll(CustomerSearchQuery query) {
        return null;
    }
}

package com.pagbank.challenge.application.customer.retrieve.list;


import com.pagbank.challenge.domain.customer.CustomerGateway;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.customer.CustomerSearchQuery;
import com.pagbank.challenge.domain.exceptions.DomainException;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultCustomerListUseCase extends CustomerListUseCase {

    private final CustomerGateway customerGateway;

    public DefaultCustomerListUseCase(final CustomerGateway customerGateway) {
        this.customerGateway = Objects.requireNonNull(customerGateway);
    }

    @Override
    public Pagination<CustomerListOutput> execute(final CustomerSearchQuery query) {

        return this.customerGateway.findAll(query)
                .map(CustomerListOutput::from);
    }
}

package com.pagbank.challenge.application.customer.retrieve.list;


import com.pagbank.challenge.domain.customer.CustomerGateway;
import com.pagbank.challenge.domain.customer.CustomerSearchQuery;
import com.pagbank.challenge.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCustomerUseCase extends ListCustomerUseCase {

    private final CustomerGateway customerGateway;

    public DefaultListCustomerUseCase(final CustomerGateway customerGateway) {
        this.customerGateway = Objects.requireNonNull(customerGateway);
    }

    @Override
    public Pagination<ListCustomerOutput> execute(final CustomerSearchQuery query) {
        return this.customerGateway.findAll(query)
                .map(ListCustomerOutput::from);
    }
}

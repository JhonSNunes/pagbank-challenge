package com.pagbank.challenge.application.customer.retrieve.get;


import com.pagbank.challenge.application.customer.create.GetCustomerByIdOutput;
import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerGateway;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.exceptions.DomainException;
import com.pagbank.challenge.domain.validation.Error;
import com.pagbank.challenge.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCustomerByIdUseCase extends GetCustomerByIdUseCase {

    private final CustomerGateway customerGateway;

    public DefaultGetCustomerByIdUseCase(final CustomerGateway customerGateway) {
        this.customerGateway = Objects.requireNonNull(customerGateway);
    }

    @Override
    public GetCustomerByIdOutput execute(final String anIn) {
        final var customerId = CustomerID.from(anIn);

        return this.customerGateway.findById(customerId)
                .map(GetCustomerByIdOutput::from)
                .orElseThrow(notFound(customerId));
    }

    private Supplier<DomainException> notFound(CustomerID id) {
        return () -> DomainException.with(
                new Error("Customer with ID %s was not found".formatted(id.getValue()))
        );
    }
}

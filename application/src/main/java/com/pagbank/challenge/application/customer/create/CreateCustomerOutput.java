package com.pagbank.challenge.application.customer.create;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerID;

public record CreateCustomerOutput(
        String id
) {
    public static CreateCustomerOutput from(final String id) {
        return new CreateCustomerOutput(id);
    }

    public static CreateCustomerOutput from(final Customer customer) {
        return new CreateCustomerOutput(customer.getId().getValue());
    }
}

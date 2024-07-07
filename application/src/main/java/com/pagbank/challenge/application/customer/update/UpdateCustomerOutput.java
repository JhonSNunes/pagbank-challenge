package com.pagbank.challenge.application.customer.update;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerID;

public record UpdateCustomerOutput(
        String id
) {
    public static UpdateCustomerOutput from(final String id) {
        return new UpdateCustomerOutput(id);
    }

    public static UpdateCustomerOutput from(final Customer customer) {
        return new UpdateCustomerOutput(customer.getId().getValue());
    }
}

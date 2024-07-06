package com.pagbank.challenge.application.customer.create;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerID;

public record CreateCustomerOutput(
        CustomerID id
) {
    public static CreateCustomerOutput from(final Customer customer) {
        return new CreateCustomerOutput(customer.getId());
    }
}

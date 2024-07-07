package com.pagbank.challenge.infrastructure.customer.presenters;

import com.pagbank.challenge.application.customer.create.GetCustomerByIdOutput;
import com.pagbank.challenge.infrastructure.customer.models.CustomerResponse;

public interface CustomerApiPresenter {
    static CustomerResponse present(final GetCustomerByIdOutput output) {
        return new CustomerResponse(
                output.id().getValue(),
                output.name(),
                output.age(),
                output.genre(),
                output.phone(),
                output.city(),
                output.state(),
                output.country(),
                output.address(),
                output.number(),
                output.zipcode(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }
}

package com.pagbank.challenge.application.customer.update;

import com.pagbank.challenge.application.customer.create.CreateCustomerCommand;
import com.pagbank.challenge.application.customer.create.CreateCustomerOutput;
import com.pagbank.challenge.application.customer.create.CreateCustomerUseCase;
import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerGateway;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.exceptions.DomainException;
import com.pagbank.challenge.domain.validation.Error;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultUpdateCustomerUseCase extends UpdateCustomerUseCase {

    private final CustomerGateway customerGateway;

    public DefaultUpdateCustomerUseCase(final CustomerGateway customerGateway) {
        this.customerGateway = Objects.requireNonNull(customerGateway);
    }

    @Override
    public Either<Notification, UpdateCustomerOutput> execute(final UpdateCustomerCommand command) {
        final var customerId = CustomerID.from(command.id());

        final var customer = this.customerGateway.findById(customerId)
                .orElseThrow(notFound(customerId));
        Notification notification = Notification.create();

        customer.update(
            command.name(),
            command.age(),
            command.genre(),
            command.phone(),
            command.city(),
            command.state(),
            command.country(),
            command.address(),
            command.number(),
            command.zipcode()
        );
        customer.validate(notification);

        return notification.hasError() ? Left(notification) : update(customer);
    }

    private Supplier<DomainException> notFound(CustomerID id) {
        return () -> DomainException.with(
                new Error("Customer with ID %s was not found".formatted(id.getValue()))
        );
    }

    private Either<Notification, UpdateCustomerOutput> update(final Customer customer) {
        return Try(() -> this.customerGateway.update(customer))
                .toEither()
                .bimap(Notification::create, UpdateCustomerOutput::from);
    }
}

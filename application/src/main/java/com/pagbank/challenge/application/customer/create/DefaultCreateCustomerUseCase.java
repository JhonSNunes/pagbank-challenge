package com.pagbank.challenge.application.customer.create;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerGateway;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultCreateCustomerUseCase extends CreateCustomerUseCase {

    private final CustomerGateway customerGateway;

    public DefaultCreateCustomerUseCase(final CustomerGateway customerGateway) {
        this.customerGateway = Objects.requireNonNull(customerGateway);
    }

    @Override
    public Either<Notification, CreateCustomerOutput> execute(final CreateCustomerCommand command) {
        final var customer = Customer.registerCustomer(
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
        Notification notification = Notification.create();

        customer.validate(notification);

        return notification.hasError() ? Left(notification) : create(customer);
    }

    private Either<Notification, CreateCustomerOutput> create(final Customer customer) {
        return Try(() -> this.customerGateway.create(customer))
                .toEither()
                .bimap(Notification::create, CreateCustomerOutput::from);
    }
}

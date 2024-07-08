package com.pagbank.challenge.application.cdborder.create;

import com.pagbank.challenge.application.cdborder.CdbOrderOutput;
import com.pagbank.challenge.domain.cdborder.CdbOrder;
import com.pagbank.challenge.domain.cdborder.CdbOrderGateway;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultCreateCdbOrderUseCase extends CreateCdbOrderUseCase {

    private final CdbOrderGateway cdbOrderGateway;

    public DefaultCreateCdbOrderUseCase(final CdbOrderGateway cdbOrderGateway) {
        this.cdbOrderGateway = Objects.requireNonNull(cdbOrderGateway);
    }

    @Override
    public Either<Notification, CdbOrderOutput> execute(final CreateCdbOrderCommand command) {
        final var order = CdbOrder.createOrder(
                command.customerId(),
                command.productId(),
                command.amount(),
                command.transactionType()
        );

        Notification notification = Notification.create();

        order.validate(notification);

        return notification.hasError() ? Left(notification) : create(order);
    }

    private Either<Notification, CdbOrderOutput> create(final CdbOrder order) {
        return Try(() -> this.cdbOrderGateway.create(order))
                .toEither()
                .bimap(Notification::create, CdbOrderOutput::from);
    }
}

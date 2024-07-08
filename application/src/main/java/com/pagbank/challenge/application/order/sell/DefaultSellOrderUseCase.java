package com.pagbank.challenge.application.order.sell;

import com.pagbank.challenge.application.order.OrderOutput;
import com.pagbank.challenge.domain.order.Order;
import com.pagbank.challenge.domain.order.OrderGateway;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultSellOrderUseCase extends SellOrderUseCase {

    private final OrderGateway orderGateway;

    public DefaultSellOrderUseCase(final OrderGateway orderGateway) {
        this.orderGateway = Objects.requireNonNull(orderGateway);
    }

    @Override
    public Either<Notification, OrderOutput> execute(final SellOrderCommand command) {
        final var order = Order.createOrder(
                command.customerId(),
                command.productId(),
                command.amount(),
                command.transactionType()
        );
        Notification notification = Notification.create();

        order.validate(notification);

        return notification.hasError() ? Left(notification) : create(order);
    }

    private Either<Notification, OrderOutput> create(final Order order) {
        return Try(() -> this.orderGateway.create(order))
                .toEither()
                .bimap(Notification::create, OrderOutput::from);
    }
}

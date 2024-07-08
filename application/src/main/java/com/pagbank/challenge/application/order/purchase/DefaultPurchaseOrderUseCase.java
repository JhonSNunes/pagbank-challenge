package com.pagbank.challenge.application.order.purchase;

import com.pagbank.challenge.application.order.OrderOutput;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.order.Order;
import com.pagbank.challenge.domain.order.OrderGateway;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultPurchaseOrderUseCase extends PurchaseOrderUseCase {

    private final OrderGateway orderGateway;

    public DefaultPurchaseOrderUseCase(final OrderGateway orderGateway) {
        this.orderGateway = Objects.requireNonNull(orderGateway);
    }

    @Override
    public Either<Notification, OrderOutput> execute(final PurchaseOrderCommand command) {
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

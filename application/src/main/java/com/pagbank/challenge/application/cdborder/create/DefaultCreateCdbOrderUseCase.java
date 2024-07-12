package com.pagbank.challenge.application.cdborder.create;

import com.pagbank.challenge.application.cdborder.CdbOrderOutput;
import com.pagbank.challenge.domain.cdborder.CdbOrder;
import com.pagbank.challenge.domain.cdborder.CdbOrderGateway;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.exceptions.DomainException;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.validation.Error;
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
        Notification notification = Notification.create();

        final var customerId = CustomerID.from(command.customerId());
        final var productId = ProductID.from(command.productId());

        if (command.transactionType().isSell()) {
            final var amountOfProduct = cdbOrderGateway.findBalanceByCustomerAndProduct(customerId, productId);

            if (amountOfProduct != null && amountOfProduct.compareTo(command.amount()) < 0 ) {
                throw DomainException.with(new Error("You don't have balance with this product to sell this amount value"));
            }
        }

        final var order = CdbOrder.createOrder(
                customerId,
                productId,
                command.amount(),
                command.transactionType()
        );

        order.validate(notification);

        return notification.hasError() ? Left(notification) : create(order);
    }

    private Either<Notification, CdbOrderOutput> create(final CdbOrder order) {
        return Try(() -> this.cdbOrderGateway.create(order))
                .toEither()
                .bimap(Notification::create, CdbOrderOutput::from);
    }
}

package com.pagbank.challenge.application.cdborder.create;

import com.pagbank.challenge.application.cdborder.CdbOrderOutput;
import com.pagbank.challenge.domain.cdborder.CdbOrder;
import com.pagbank.challenge.domain.cdborder.CdbOrderGateway;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.exceptions.DomainException;
import com.pagbank.challenge.domain.product.ProductGateway;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.validation.Error;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultCreateCdbOrderUseCase extends CreateCdbOrderUseCase {

    private final CdbOrderGateway cdbOrderGateway;

    private final ProductGateway productGateway;

    public DefaultCreateCdbOrderUseCase(final CdbOrderGateway cdbOrderGateway, final ProductGateway productGateway) {
        this.cdbOrderGateway = Objects.requireNonNull(cdbOrderGateway);
        this.productGateway = Objects.requireNonNull(productGateway);
    }

    @Override
    public Either<Notification, CdbOrderOutput> execute(final CreateCdbOrderCommand command) {
        Notification notification = Notification.create();

        final var customerId = CustomerID.from(command.customerId());
        final var productId = ProductID.from(command.productId());

        final var order = CdbOrder.createOrder(
                customerId,
                productId,
                command.amount(),
                command.transactionType()
        );

        order.validate(notification);

        if (notification.hasError()) {
            return Left(notification);
        }

        if (command.transactionType().isSell()) {
            final var amountOfProduct = cdbOrderGateway.findBalanceByCustomerAndProduct(customerId, productId);

            if (amountOfProduct != null && amountOfProduct.compareTo(command.amount()) < 0 ) {
                throw DomainException.with(new Error("You don't have balance with this product to sell this amount value"));
            }
        } else if (!this.productGateway.findIsActive(productId)) {
            throw DomainException.with(new Error("Product must be active to be purchased!"));
        }

        return create(order);
    }

    private Either<Notification, CdbOrderOutput> create(final CdbOrder order) {
        return Try(() -> this.cdbOrderGateway.create(order))
                .toEither()
                .bimap(Notification::create, CdbOrderOutput::from);
    }
}

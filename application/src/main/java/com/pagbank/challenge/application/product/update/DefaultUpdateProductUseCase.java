package com.pagbank.challenge.application.product.update;

import com.pagbank.challenge.domain.exceptions.NotFoundException;
import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.domain.product.ProductGateway;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultUpdateProductUseCase extends UpdateProductUseCase {

    private final ProductGateway productGateway;

    public DefaultUpdateProductUseCase(final ProductGateway productGateway) {
        this.productGateway = Objects.requireNonNull(productGateway);
    }

    @Override
    public Either<Notification, UpdateProductOutput> execute(final UpdateProductCommand command) {
        final var productID = ProductID.from(command.id());

        final var product = this.productGateway.findById(productID)
                .orElseThrow(notFound(productID));
        Notification notification = Notification.create();

        product.update(command.name(), command.rate(), command.isActive());
        product.validate(notification);

        return notification.hasError() ? Left(notification) : update(product);
    }

    private Supplier<NotFoundException> notFound(ProductID id) {
        return () -> NotFoundException.with(Product.class, id);
    }

    private Either<Notification, UpdateProductOutput> update(final Product product) {
        return Try(() -> this.productGateway.update(product))
                .toEither()
                .bimap(Notification::create, UpdateProductOutput::from);
    }
}

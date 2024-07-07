package com.pagbank.challenge.application.product.create;

import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.domain.product.ProductGateway;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultCreateProductUseCase extends CreateProductUseCase {

    private final ProductGateway productGateway;

    public DefaultCreateProductUseCase(final ProductGateway productGateway) {
        this.productGateway = Objects.requireNonNull(productGateway);
    }

    @Override
    public Either<Notification, CreateProductOutput> execute(final CreateProductCommand command) {
        final var product = Product.createProduct(
                command.name(),
                command.rate(),
                command.isActive()
        );
        Notification notification = Notification.create();

        product.validate(notification);

        return notification.hasError() ? Left(notification) : create(product);
    }

    private Either<Notification, CreateProductOutput> create(final Product product) {
        return Try(() -> this.productGateway.create(product))
                .toEither()
                .bimap(Notification::create, CreateProductOutput::from);
    }
}

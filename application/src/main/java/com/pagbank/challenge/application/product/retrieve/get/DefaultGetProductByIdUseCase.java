package com.pagbank.challenge.application.product.retrieve.get;

import com.pagbank.challenge.domain.exceptions.NotFoundException;
import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.domain.product.ProductGateway;
import com.pagbank.challenge.domain.product.ProductID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetProductByIdUseCase extends GetProductByIdUseCase {

    private final ProductGateway gateway;

    public DefaultGetProductByIdUseCase(final ProductGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public GetProductByIdOutput execute(final String anIn) {
        final var id = ProductID.from(anIn);

        return this.gateway.findById(id)
                .map(GetProductByIdOutput::from)
                .orElseThrow(notFound(id));
    }

    private Supplier<NotFoundException> notFound(ProductID id) {
        return () -> NotFoundException.with(Product.class, id);
    }
}

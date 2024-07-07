package com.pagbank.challenge.application.product.delete;

import com.pagbank.challenge.domain.product.ProductGateway;
import com.pagbank.challenge.domain.product.ProductID;

import java.util.Objects;

public class DefaultDeleteProductUseCase extends DeleteProductUseCase {

    private final ProductGateway gateway;

    public DefaultDeleteProductUseCase(final ProductGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(final String anIn) {
        this.gateway.deleteById(ProductID.from(anIn));
    }
}

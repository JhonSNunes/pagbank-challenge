package com.pagbank.challenge.application.product.update;

import com.pagbank.challenge.domain.product.Product;

public record UpdateProductOutput(
        String id
) {
    public static UpdateProductOutput from(final String id) {
        return new UpdateProductOutput(id);
    }

    public static UpdateProductOutput from(final Product product) {
        return new UpdateProductOutput(product.getId().getValue());
    }
}

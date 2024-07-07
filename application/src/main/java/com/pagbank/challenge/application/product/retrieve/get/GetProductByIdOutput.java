package com.pagbank.challenge.application.product.retrieve.get;

import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.domain.product.ProductID;

import java.math.BigDecimal;
import java.time.Instant;

public record GetProductByIdOutput(
        ProductID id,
        String name,
        BigDecimal rate,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static GetProductByIdOutput from(final Product product) {
        return new GetProductByIdOutput(
                product.getId(),
                product.getName(),
                product.getRate(),
                product.isActive(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getDeletedAt()
        );
    }
}

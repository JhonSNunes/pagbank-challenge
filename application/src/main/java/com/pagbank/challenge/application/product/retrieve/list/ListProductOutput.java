package com.pagbank.challenge.application.product.retrieve.list;

import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.domain.product.ProductID;

import java.math.BigDecimal;
import java.time.Instant;

public record ListProductOutput(
        ProductID id,
        String name,
        BigDecimal rate,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static ListProductOutput from(final Product product) {
        return new ListProductOutput(
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

package com.pagbank.challenge.application.product.update;

import java.math.BigDecimal;

public record UpdateProductCommand(
        String id,
        String name,
        BigDecimal rate,
        Boolean isActive
) {
    public static UpdateProductCommand with(
            final String id,
            final String name,
            final BigDecimal rate,
            final Boolean isActive
    ) {
        return new UpdateProductCommand(id, name, rate, isActive);
    }
}

package com.pagbank.challenge.application.product.create;

import java.math.BigDecimal;

public record CreateProductCommand(
        String name,
        BigDecimal rate,
        Boolean isActive
) {
    public static CreateProductCommand with(
            final String name,
            final BigDecimal rate,
            final Boolean isActive
    ) {
        return new CreateProductCommand(name, rate, isActive);
    }
}

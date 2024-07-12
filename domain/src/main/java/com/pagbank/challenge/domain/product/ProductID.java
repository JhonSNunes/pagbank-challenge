package com.pagbank.challenge.domain.product;

import com.pagbank.challenge.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class ProductID extends Identifier {
    private final String value;

    private ProductID(final String value) {
        this.value = value;
    }

    public static ProductID unique() {
        return ProductID.from(UUID.randomUUID());
    }

    public static ProductID from(final String id) {
        return new ProductID(id);
    }

    public static ProductID from(final UUID id) {
        return new ProductID(id.toString().toUpperCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProductID that = (ProductID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}

package com.pagbank.challenge.domain.order;

import com.pagbank.challenge.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class OrderID extends Identifier {
    private final String value;

    private OrderID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static OrderID unique() {
        return OrderID.from(UUID.randomUUID());
    }

    public static OrderID from(final String id) {
        return new OrderID(id);
    }

    public static OrderID from(final UUID id) {
        return new OrderID(id.toString().toUpperCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderID that = (OrderID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}

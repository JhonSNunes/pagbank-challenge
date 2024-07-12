package com.pagbank.challenge.domain.customer;

import com.pagbank.challenge.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CustomerID extends Identifier {
    private final String value;

    private CustomerID(final String value) {
        this.value = value;
    }

    public static CustomerID unique() {
        return CustomerID.from(UUID.randomUUID());
    }

    public static CustomerID from(final String id) {
        return new CustomerID(id);
    }

    public static CustomerID from(final UUID id) {
        return new CustomerID(id.toString().toUpperCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CustomerID that = (CustomerID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}

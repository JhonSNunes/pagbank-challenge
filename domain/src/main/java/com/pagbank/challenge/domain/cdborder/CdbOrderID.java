package com.pagbank.challenge.domain.cdborder;

import com.pagbank.challenge.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CdbOrderID extends Identifier {
    private final String value;

    private CdbOrderID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static CdbOrderID unique() {
        return CdbOrderID.from(UUID.randomUUID());
    }

    public static CdbOrderID from(final String id) {
        return new CdbOrderID(id);
    }

    public static CdbOrderID from(final UUID id) {
        return new CdbOrderID(id.toString().toUpperCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CdbOrderID that = (CdbOrderID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}

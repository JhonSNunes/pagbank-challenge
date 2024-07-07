package com.pagbank.challenge.domain.product;

import com.pagbank.challenge.domain.AggregateRoot;
import com.pagbank.challenge.domain.validation.ValidationHandler;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Product extends AggregateRoot<ProductID> implements Cloneable {
    private String name;
    private BigDecimal rate;
    private Boolean active = false;
    private Instant createdAt = null;
    private Instant updatedAt = null;
    private Instant deletedAt = null;

    private Product(
            final ProductID id,
            final String name,
            final BigDecimal rate,
            final Boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        super(id);
        this.name = name;
        this.rate = rate;
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
        this.deletedAt = deletedAt;
    }

    public static Product createProduct(
            final String name,
            final BigDecimal rate,
            final Boolean isActive
    ) {
        Instant now = Instant.now();
        ProductID id = ProductID.unique();

        return new Product(id, name, rate, isActive, now, now, null);
    }

    public static Product with(
            final ProductID id,
            final String name,
            final BigDecimal rate,
            final Boolean isActive,
            final Instant createdAt,
            final Instant updatedAtAt,
            final Instant deletedAtAt
    ) {
        return new Product(id, name, rate, isActive, createdAt, updatedAtAt, deletedAtAt);
    }

    public Product update(
            final String name,
            final BigDecimal rate,
            final Boolean isActive
    ) {
        Instant now = Instant.now();

        if (isActive) {
            activate();
        } else {
            deactivate();
        }

        this.name = name;
        this.rate = rate;
        this.active = isActive;

        this.updatedAt = now;

        return this;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new ProductValidator(this, handler).validate();
    }

    public Product activate() {
        Instant now = Instant.now();

        this.active = true;
        this.updatedAt = now;
        this.deletedAt = null;

        return this;
    }

    public Product deactivate() {
        Instant now = Instant.now();

        this.active = false;
        this.updatedAt = now;

        if (getDeletedAt() == null) {
            this.deletedAt = now;
        }

        return this;
    }

    public ProductID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public Boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Product clone() {
        try {
            Product clone = (Product) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
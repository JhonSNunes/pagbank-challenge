package com.pagbank.challenge.domain.cdborder;

import com.pagbank.challenge.domain.AggregateRoot;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.validation.ValidationHandler;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class CdbOrder extends AggregateRoot<CdbOrderID> implements Cloneable {
    private CustomerID customerId;
    private ProductID productId;
    private BigDecimal amount;
    private Instant transactionDate = null;
    private CdbOrderTransactionType transactionType = null;

    private CdbOrder(
            final CdbOrderID id,
            final CustomerID customerId,
            final ProductID productId,
            final BigDecimal amount,
            final Instant transactionDate,
            final CdbOrderTransactionType transactionType
    ) {
        super(id);
        this.customerId = customerId;
        this.productId = productId;
        this.amount = amount;
        this.transactionDate = Objects.requireNonNull(transactionDate);
        this.transactionType = Objects.requireNonNull(transactionType);
    }

    public static CdbOrder createOrder(
            final CustomerID customerId,
            final ProductID productId,
            final BigDecimal amount,
            final CdbOrderTransactionType cdbOrderTransactionType
    ) {
        CdbOrderID id = CdbOrderID.unique();
        Instant transactionDate = Instant.now();

        return new CdbOrder(id, customerId, productId, amount, transactionDate, cdbOrderTransactionType);
    }

    public static CdbOrder with(
            final CdbOrderID id,
            final CustomerID customerId,
            final ProductID productId,
            final BigDecimal amount,
            final Instant transactionDate,
            final CdbOrderTransactionType transactionType
    ) {
        return new CdbOrder(id, customerId, productId, amount, transactionDate, transactionType);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CdbOrderValidator(this, handler).validate();
    }

    public CdbOrderID getId() {
        return id;
    }

    public CustomerID getCustomerId() {
        return customerId;
    }

    public ProductID getProductId() {
        return productId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public CdbOrderTransactionType getTransactionType() {
        return transactionType;
    }

    @Override
    public CdbOrder clone() {
        try {
            CdbOrder clone = (CdbOrder) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
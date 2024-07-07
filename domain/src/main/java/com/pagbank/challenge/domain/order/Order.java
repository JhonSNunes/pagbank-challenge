package com.pagbank.challenge.domain.order;

import com.pagbank.challenge.domain.AggregateRoot;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.product.ProductValidator;
import com.pagbank.challenge.domain.validation.ValidationHandler;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public class Order extends AggregateRoot<OrderID> implements Cloneable {
    private CustomerID customerId;
    private ProductID productId;
    private BigDecimal amount;
    private Instant transactionDate = null;
    private OrderTransactionType transactionType = null;

    private Order(
            final OrderID id,
            final CustomerID customerId,
            final ProductID productId,
            final BigDecimal amount,
            final Instant transactionDate,
            final OrderTransactionType transactionType
    ) {
        super(id);
        this.customerId = customerId;
        this.productId = productId;
        this.amount = amount;
        this.transactionDate = Objects.requireNonNull(transactionDate);
        this.transactionType = Objects.requireNonNull(transactionType);
    }

    public static Order createSellOrder(
            final CustomerID customerId,
            final ProductID productId,
            final BigDecimal amount
    ) {
        OrderID id = OrderID.unique();
        Instant transactionDate = Instant.now();

        return new Order(id, customerId, productId, amount, transactionDate, OrderTransactionType.SELL);
    }

    public static Order createBuyOrder(
            final CustomerID customerId,
            final ProductID productId,
            final BigDecimal amount
    ) {
        OrderID id = OrderID.unique();
        Instant transactionDate = Instant.now();

        return new Order(id, customerId, productId, amount, transactionDate, OrderTransactionType.BUY);
    }

    public static Order with(
            final OrderID id,
            final CustomerID customerId,
            final ProductID productId,
            final BigDecimal amount,
            final Instant transactionDate,
            final OrderTransactionType transactionType
    ) {
        return new Order(id, customerId, productId, amount, transactionDate, transactionType);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new OrderValidator(this, handler).validate();
    }

    public OrderID getId() {
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

    public OrderTransactionType getTransactionType() {
        return transactionType;
    }

    @Override
    public Order clone() {
        try {
            Order clone = (Order) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
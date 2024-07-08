package com.pagbank.challenge.infrastructure.order.persistence;

import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.order.Order;
import com.pagbank.challenge.domain.order.OrderID;
import com.pagbank.challenge.domain.order.OrderTransactionType;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity;
import com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "order")
public class OrderJpaEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerJpaEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductJpaEntity product;

    @Column(name = "amount", columnDefinition = "DECIMAL(19, 4)")
    private BigDecimal amount;

    @Column(name = "transaction_date", columnDefinition = "DATETIME(6)")
    private Instant transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private OrderTransactionType transactionType;

    public OrderJpaEntity() {
    }

    private OrderJpaEntity(
            final String id,
            final CustomerJpaEntity customer,
            final ProductJpaEntity product,
            final BigDecimal amount,
            final Instant transactionDate,
            final OrderTransactionType transactionType
    ) {
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
    }

    public static OrderJpaEntity from(
            final Order order,
            final CustomerJpaEntity customer,
            final ProductJpaEntity product
    ) {
        return new OrderJpaEntity(
                order.getId().getValue(),
                customer,
                product,
                order.getAmount(),
                order.getTransactionDate(),
                order.getTransactionType()
        );
    }

    public Order toAggregate() {
        return Order.with(
                OrderID.from(getId()),
                CustomerID.from(getCustomer().getId()),
                ProductID.from(getProduct().getId()),
                getAmount(),
                getTransactionDate(),
                getTransactionType()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CustomerJpaEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerJpaEntity customer) {
        this.customer = customer;
    }

    public ProductJpaEntity getProduct() {
        return product;
    }

    public void setProduct(ProductJpaEntity product) {
        this.product = product;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public OrderTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(OrderTransactionType transactionType) {
        this.transactionType = transactionType;
    }
}

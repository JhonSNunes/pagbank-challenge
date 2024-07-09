package com.pagbank.challenge.infrastructure.cdborder.persistence;

import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.cdborder.CdbOrder;
import com.pagbank.challenge.domain.cdborder.CdbOrderID;
import com.pagbank.challenge.domain.cdborder.CdbOrderTransactionType;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity;
import com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "cdb_order")
public class CdbOrderJpaEntity implements Serializable {

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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "transaction_type", nullable = false)
    private CdbOrderTransactionType transactionType;

    public CdbOrderJpaEntity() {
    }

    private CdbOrderJpaEntity(
            final String id,
            final CustomerJpaEntity customer,
            final ProductJpaEntity product,
            final BigDecimal amount,
            final Instant transactionDate,
            final CdbOrderTransactionType transactionType
    ) {
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
    }

    public static CdbOrderJpaEntity from(
            final CdbOrder order,
            final CustomerJpaEntity customer,
            final ProductJpaEntity product
    ) {
        return new CdbOrderJpaEntity(
                order.getId().getValue(),
                customer,
                product,
                order.getAmount(),
                order.getTransactionDate(),
                order.getTransactionType()
        );
    }

    public CdbOrder toAggregate() {
        return CdbOrder.with(
                CdbOrderID.from(getId()),
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

    public CdbOrderTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(CdbOrderTransactionType transactionType) {
        this.transactionType = transactionType;
    }
}

package com.pagbank.challenge.domain.cdborder;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.product.Product;

import java.math.BigDecimal;
import java.time.Instant;

public record CdbOrderView(
        String orderId,
        String customerId,
        String customerName,
        String productId,
        String productName,
        BigDecimal amount,
        Instant transactionDate,
        CdbOrderTransactionType transactionType
) {
    public CdbOrderView(final CdbOrder order, final Customer customer, final Product product) {
        this(
                order.getId().getValue(),
                customer.getId().getValue(),
                customer.getName(),
                product.getId().getValue(),
                product.getName(),
                order.getAmount(),
                order.getTransactionDate(),
                order.getTransactionType()
        );
    }
}
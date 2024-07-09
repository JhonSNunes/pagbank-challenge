package com.pagbank.challenge.application.cdborder.retrieve.list;

import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.cdborder.CdbOrder;
import com.pagbank.challenge.domain.cdborder.CdbOrderID;
import com.pagbank.challenge.domain.cdborder.CdbOrderTransactionType;
import com.pagbank.challenge.domain.product.ProductID;

import java.math.BigDecimal;
import java.time.Instant;

public record ListOrderOutput(
        CdbOrderID id,
        CustomerID customerId,
        ProductID productId,
        BigDecimal amount,
        Instant transactionDate,
        CdbOrderTransactionType transactionType
) {
    public static ListOrderOutput from(final CdbOrder order) {
        return new ListOrderOutput(
                order.getId(),
                order.getCustomerId(),
                order.getProductId(),
                order.getAmount(),
                order.getTransactionDate(),
                order.getTransactionType()
        );
    }
}
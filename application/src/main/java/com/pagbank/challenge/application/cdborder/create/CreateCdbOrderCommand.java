package com.pagbank.challenge.application.cdborder.create;

import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.cdborder.CdbOrderTransactionType;
import com.pagbank.challenge.domain.product.ProductID;

import java.math.BigDecimal;

public record CreateCdbOrderCommand(
        CustomerID customerId,
        ProductID productId,
        BigDecimal amount,
        CdbOrderTransactionType transactionType
) {
    public static CreateCdbOrderCommand with(
            final CustomerID customerId,
            final ProductID productId,
            final BigDecimal amount,
            final CdbOrderTransactionType transactionType
    ) {
        return new CreateCdbOrderCommand(customerId, productId, amount, transactionType);
    }
}
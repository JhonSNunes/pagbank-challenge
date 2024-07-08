package com.pagbank.challenge.application.order.purchase;

import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.order.OrderTransactionType;
import com.pagbank.challenge.domain.product.ProductID;

import java.math.BigDecimal;

public record PurchaseOrderCommand(
        CustomerID customerId,
        ProductID productId,
        BigDecimal amount,
        OrderTransactionType transactionType
) {
    public static PurchaseOrderCommand with(
            final CustomerID customerId,
            final ProductID productId,
            final BigDecimal amount
    ) {
        return new PurchaseOrderCommand(customerId, productId, amount, OrderTransactionType.PURCHASE);
    }
}

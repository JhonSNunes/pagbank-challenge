package com.pagbank.challenge.application.order.sell;

import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.order.OrderTransactionType;
import com.pagbank.challenge.domain.product.ProductID;

import java.math.BigDecimal;

public record SellOrderCommand(
        CustomerID customerId,
        ProductID productId,
        BigDecimal amount,
        OrderTransactionType transactionType
) {
    public static SellOrderCommand with(
            final CustomerID customerId,
            ProductID productId,
            BigDecimal amount
    ) {
        return new SellOrderCommand(customerId, productId, amount, OrderTransactionType.SELL);
    }
}

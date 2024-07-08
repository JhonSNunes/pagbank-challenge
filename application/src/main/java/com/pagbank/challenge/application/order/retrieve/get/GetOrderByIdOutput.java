package com.pagbank.challenge.application.order.retrieve.get;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.order.Order;
import com.pagbank.challenge.domain.order.OrderID;
import com.pagbank.challenge.domain.order.OrderTransactionType;
import com.pagbank.challenge.domain.product.ProductID;

import java.math.BigDecimal;
import java.time.Instant;

public record GetOrderByIdOutput(
        OrderID id,
        CustomerID customerId,
        ProductID productId,
        BigDecimal amount,
        Instant transactionDate,
        OrderTransactionType transactionType
) {
    public static GetOrderByIdOutput from(final Order order) {
        return new GetOrderByIdOutput(
                order.getId(),
                order.getCustomerId(),
                order.getProductId(),
                order.getAmount(),
                order.getTransactionDate(),
                order.getTransactionType()
        );
    }
}

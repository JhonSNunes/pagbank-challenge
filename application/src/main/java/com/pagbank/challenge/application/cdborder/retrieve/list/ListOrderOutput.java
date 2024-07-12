package com.pagbank.challenge.application.cdborder.retrieve.list;

import com.pagbank.challenge.domain.cdborder.CdbOrder;
import com.pagbank.challenge.domain.cdborder.CdbOrderTransactionType;
import com.pagbank.challenge.domain.cdborder.CdbOrderView;
import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.product.Product;

import java.math.BigDecimal;
import java.time.Instant;

public record ListOrderOutput(
        String id,
        String customerId,
        String customerName,
        String productId,
        String productName,
        BigDecimal amount,
        Instant transactionDate,
        CdbOrderTransactionType transactionType
) {
    public static ListOrderOutput from(
            final CdbOrder order,
            final Customer customer,
            final Product product
    ) {
        return new ListOrderOutput(
                order.getId().getValue(),
                order.getCustomerId().getValue(),
                customer.getName(),
                order.getProductId().getValue(),
                product.getName(),
                order.getAmount(),
                order.getTransactionDate(),
                order.getTransactionType()
        );
    }

    public static ListOrderOutput from(final CdbOrderView orderView) {
        return new ListOrderOutput(
                orderView.orderId(),
                orderView.customerId(),
                orderView.customerName(),
                orderView.productId(),
                orderView.productName(),
                orderView.amount(),
                orderView.transactionDate(),
                orderView.transactionType()
        );
    }
}

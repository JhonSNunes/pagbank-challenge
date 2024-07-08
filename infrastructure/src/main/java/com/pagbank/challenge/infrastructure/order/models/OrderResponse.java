package com.pagbank.challenge.infrastructure.order.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pagbank.challenge.domain.cdborder.CdbOrderTransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
        @JsonProperty("order_id") String id,
        @JsonProperty("customer_id") String customerId,
        @JsonProperty("product_id") String productId,
        @JsonProperty("amount") BigDecimal amount,
        @JsonProperty("transaction_date") Instant transactionDate,
        @JsonProperty("transaction_type") CdbOrderTransactionType transactionType
) {
}

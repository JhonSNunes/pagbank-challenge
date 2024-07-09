package com.pagbank.challenge.infrastructure.cdborder.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pagbank.challenge.domain.cdborder.CdbOrderTransactionType;

import java.math.BigDecimal;

public record CdbOrderRequest(
        @JsonProperty("customer_id") String customerId,
        @JsonProperty("product_id") String productId,
        @JsonProperty("amount") BigDecimal amount,
        @JsonProperty("transaction_type") CdbOrderTransactionType transactionType
) {
}

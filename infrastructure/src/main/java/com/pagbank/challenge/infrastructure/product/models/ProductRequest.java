package com.pagbank.challenge.infrastructure.product.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ProductRequest(
        @JsonProperty("name") String name,
        @JsonProperty("rate") BigDecimal rate,
        @JsonProperty("is_active") Boolean isActive
) {
}

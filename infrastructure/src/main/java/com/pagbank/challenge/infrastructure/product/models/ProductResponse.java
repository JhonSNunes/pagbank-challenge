package com.pagbank.challenge.infrastructure.product.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("rate") BigDecimal rate,
        @JsonProperty("is_active") Boolean isActive,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {
}

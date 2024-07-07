package com.pagbank.challenge.infrastructure.customer.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CustomerResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("age") Integer age,
        @JsonProperty("genre") String genre,
        @JsonProperty("phone") String phone,
        @JsonProperty("city") String city,
        @JsonProperty("state") String state,
        @JsonProperty("country") String country,
        @JsonProperty("address") String address,
        @JsonProperty("number") String number,
        @JsonProperty("zipcode") String zipcode,
        @JsonProperty("is_active") Boolean isActive,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {
}

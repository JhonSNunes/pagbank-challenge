package com.pagbank.challenge.infrastructure.customer.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerRequest(
        @JsonProperty("name") String name,
        @JsonProperty("age") Integer age,
        @JsonProperty("genre") String genre,
        @JsonProperty("phone") String phone,
        @JsonProperty("city") String city,
        @JsonProperty("state") String state,
        @JsonProperty("country") String country,
        @JsonProperty("address") String address,
        @JsonProperty("number") String number,
        @JsonProperty("zipcode") String zipcode
) {
}

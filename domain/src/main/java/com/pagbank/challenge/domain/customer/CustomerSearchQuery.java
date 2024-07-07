package com.pagbank.challenge.domain.customer;

public record CustomerSearchQuery(
        Integer page,
        Integer perPage,
        String terms,
        String sort,
        String direction
) {
}

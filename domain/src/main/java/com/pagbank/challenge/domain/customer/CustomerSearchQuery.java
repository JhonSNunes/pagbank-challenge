package com.pagbank.challenge.domain.customer;

public record CustomerSearchQuery(
        Integer page,
        Integer itemsPerPage,
        String terms,
        String sort,
        String direction
) {
}
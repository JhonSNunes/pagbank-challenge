package com.pagbank.challenge.domain.product;

public record ProductSearchQuery(
        Integer page,
        Integer perPage,
        String terms,
        String sort,
        String direction
) {
}

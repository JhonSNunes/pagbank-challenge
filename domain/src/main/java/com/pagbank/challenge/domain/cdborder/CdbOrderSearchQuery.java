package com.pagbank.challenge.domain.cdborder;

public record CdbOrderSearchQuery(
        Integer page,
        Integer perPage,
        String terms,
        String customerId,
        String productId
) {
}

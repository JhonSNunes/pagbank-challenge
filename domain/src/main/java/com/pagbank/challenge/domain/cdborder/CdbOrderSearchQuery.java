package com.pagbank.challenge.domain.cdborder;

import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.product.ProductID;

public record CdbOrderSearchQuery(
        Integer page,
        Integer perPage,
        String terms,
        String direction,
        CustomerID customerId,
        ProductID productId
) {
}

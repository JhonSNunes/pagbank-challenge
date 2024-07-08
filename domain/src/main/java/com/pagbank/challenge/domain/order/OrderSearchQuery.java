package com.pagbank.challenge.domain.order;

import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.product.ProductID;

public record OrderSearchQuery(
        Integer page,
        Integer perPage,
        String terms,
        String sort,
        String direction,
        CustomerID customerId,
        ProductID productId
) {
}

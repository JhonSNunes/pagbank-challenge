package com.pagbank.challenge.domain.cdborder;

import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.ProductID;

import java.math.BigDecimal;
import java.util.Optional;

public interface CdbOrderGateway {
    CdbOrder create(CdbOrder order);

    void deleteById(CdbOrderID id);

    Optional<CdbOrder> findById(CdbOrderID id);

    Pagination<CdbOrderView> findAll(CdbOrderSearchQuery query);

    BigDecimal findBalanceByCustomerAndProduct(CustomerID customerId, ProductID productId);
}

package com.pagbank.challenge.domain.order;

import com.pagbank.challenge.domain.pagination.Pagination;

import java.util.Optional;

public interface OrderGateway {
    Order create(Order order);

    void deleteById(OrderID id);

    Optional<Order> findById(OrderID id);

    Pagination<Order> findAll(OrderSearchQuery query);
}

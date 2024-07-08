package com.pagbank.challenge.application.order.retrieve.list;

import com.pagbank.challenge.domain.order.OrderGateway;
import com.pagbank.challenge.domain.order.OrderSearchQuery;
import com.pagbank.challenge.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListOrderUseCase extends ListOrderUseCase {

    private final OrderGateway orderGateway;

    public DefaultListOrderUseCase(final OrderGateway orderGateway) {
        this.orderGateway = Objects.requireNonNull(orderGateway);
    }

    @Override
    public Pagination<ListOrderOutput> execute(final OrderSearchQuery query) {
        return this.orderGateway.findAll(query)
                .map(ListOrderOutput::from);
    }
}

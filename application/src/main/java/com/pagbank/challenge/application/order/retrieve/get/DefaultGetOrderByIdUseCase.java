package com.pagbank.challenge.application.order.retrieve.get;

import com.pagbank.challenge.domain.exceptions.NotFoundException;
import com.pagbank.challenge.domain.order.Order;
import com.pagbank.challenge.domain.order.OrderGateway;
import com.pagbank.challenge.domain.order.OrderID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetOrderByIdUseCase extends GetOrderByIdUseCase {

    private final OrderGateway orderGateway;

    public DefaultGetOrderByIdUseCase(final OrderGateway orderGateway) {
        this.orderGateway = Objects.requireNonNull(orderGateway);
    }

    @Override
    public GetOrderByIdOutput execute(final String anIn) {
        final var orderId = OrderID.from(anIn);

        return this.orderGateway.findById(orderId)
                .map(GetOrderByIdOutput::from)
                .orElseThrow(notFound(orderId));
    }

    private Supplier<NotFoundException> notFound(OrderID id) {
        return () -> NotFoundException.with(Order.class, id);
    }
}

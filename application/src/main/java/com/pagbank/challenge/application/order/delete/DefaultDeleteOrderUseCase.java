package com.pagbank.challenge.application.order.delete;

import com.pagbank.challenge.domain.order.OrderGateway;
import com.pagbank.challenge.domain.order.OrderID;

import java.util.Objects;

public class DefaultDeleteOrderUseCase extends DeleteOrderUseCase {

    private final OrderGateway orderGateway;

    public DefaultDeleteOrderUseCase(final OrderGateway orderGateway) {
        this.orderGateway = Objects.requireNonNull(orderGateway);
    }

    @Override
    public void execute(final String anIn) {
        this.orderGateway.deleteById(OrderID.from(anIn));
    }
}

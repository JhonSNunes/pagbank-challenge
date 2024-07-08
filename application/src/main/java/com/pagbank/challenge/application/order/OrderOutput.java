package com.pagbank.challenge.application.order;

import com.pagbank.challenge.domain.order.Order;

public record OrderOutput(
        String id
) {
    public static OrderOutput from(final String id) {
        return new OrderOutput(id);
    }

    public static OrderOutput from(final Order order) {
        return new OrderOutput(order.getId().getValue());
    }
}

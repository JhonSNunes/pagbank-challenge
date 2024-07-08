package com.pagbank.challenge.infrastructure.order.presenters;

import com.pagbank.challenge.application.cdborder.retrieve.get.GetOrderByIdOutput;
import com.pagbank.challenge.infrastructure.order.models.OrderResponse;

public interface OrderApiPresenter {
    static OrderResponse present(final GetOrderByIdOutput output) {
        return new OrderResponse(
                output.id().getValue(),
                output.customerId().getValue(),
                output.productId().getValue(),
                output.amount(),
                output.transactionDate(),
                output.transactionType()
        );
    }
}

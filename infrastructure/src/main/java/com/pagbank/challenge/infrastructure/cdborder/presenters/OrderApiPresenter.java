package com.pagbank.challenge.infrastructure.cdborder.presenters;

import com.pagbank.challenge.application.cdborder.retrieve.get.GetOrderByIdOutput;
import com.pagbank.challenge.infrastructure.cdborder.models.CdbOrderResponse;

public interface OrderApiPresenter {
    static CdbOrderResponse present(final GetOrderByIdOutput output) {
        return new CdbOrderResponse(
                output.id().getValue(),
                output.customerId().getValue(),
                output.productId().getValue(),
                output.amount(),
                output.transactionDate(),
                output.transactionType()
        );
    }
}

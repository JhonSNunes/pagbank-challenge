package com.pagbank.challenge.application.cdborder.retrieve.get;

import com.pagbank.challenge.domain.exceptions.NotFoundException;
import com.pagbank.challenge.domain.cdborder.CdbOrder;
import com.pagbank.challenge.domain.cdborder.CdbOrderGateway;
import com.pagbank.challenge.domain.cdborder.CdbOrderID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetOrderByIdUseCase extends GetOrderByIdUseCase {

    private final CdbOrderGateway cdbOrderGateway;

    public DefaultGetOrderByIdUseCase(final CdbOrderGateway cdbOrderGateway) {
        this.cdbOrderGateway = Objects.requireNonNull(cdbOrderGateway);
    }

    @Override
    public GetOrderByIdOutput execute(final String anIn) {
        final var orderId = CdbOrderID.from(anIn);

        return this.cdbOrderGateway.findById(orderId)
                .map(GetOrderByIdOutput::from)
                .orElseThrow(notFound(orderId));
    }

    private Supplier<NotFoundException> notFound(CdbOrderID id) {
        return () -> NotFoundException.with(CdbOrder.class, id);
    }
}

package com.pagbank.challenge.application.cdborder.delete;

import com.pagbank.challenge.domain.cdborder.CdbOrderGateway;
import com.pagbank.challenge.domain.cdborder.CdbOrderID;

import java.util.Objects;

public class DefaultDeleteOrderUseCase extends DeleteOrderUseCase {

    private final CdbOrderGateway cdbOrderGateway;

    public DefaultDeleteOrderUseCase(final CdbOrderGateway cdbOrderGateway) {
        this.cdbOrderGateway = Objects.requireNonNull(cdbOrderGateway);
    }

    @Override
    public void execute(final String anIn) {
        this.cdbOrderGateway.deleteById(CdbOrderID.from(anIn));
    }
}

package com.pagbank.challenge.application.cdborder;

import com.pagbank.challenge.domain.cdborder.CdbOrder;

public record CdbOrderOutput(
        String id
) {
    public static CdbOrderOutput from(final String id) {
        return new CdbOrderOutput(id);
    }

    public static CdbOrderOutput from(final CdbOrder order) {
        return new CdbOrderOutput(order.getId().getValue());
    }
}

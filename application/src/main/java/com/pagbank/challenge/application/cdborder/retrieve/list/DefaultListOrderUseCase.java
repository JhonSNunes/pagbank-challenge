package com.pagbank.challenge.application.cdborder.retrieve.list;

import com.pagbank.challenge.domain.cdborder.CdbOrderGateway;
import com.pagbank.challenge.domain.cdborder.CdbOrderSearchQuery;
import com.pagbank.challenge.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListOrderUseCase extends ListOrderUseCase {

    private final CdbOrderGateway cdbOrderGateway;

    public DefaultListOrderUseCase(final CdbOrderGateway cdbOrderGateway) {
        this.cdbOrderGateway = Objects.requireNonNull(cdbOrderGateway);
    }

    @Override
    public Pagination<ListOrderOutput> execute(final CdbOrderSearchQuery query) {
        return this.cdbOrderGateway.findAll(query)
                .map(ListOrderOutput::from);
    }
}

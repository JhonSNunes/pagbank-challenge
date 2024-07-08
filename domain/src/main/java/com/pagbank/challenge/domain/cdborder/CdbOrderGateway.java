package com.pagbank.challenge.domain.cdborder;

import com.pagbank.challenge.domain.pagination.Pagination;

import java.util.Optional;

public interface CdbOrderGateway {
    CdbOrder create(CdbOrder order);

    void deleteById(CdbOrderID id);

    Optional<CdbOrder> findById(CdbOrderID id);

    Pagination<CdbOrder> findAll(CdbOrderSearchQuery query);
}

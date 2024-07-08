package com.pagbank.challenge.application.cdborder.retrieve.list;

import com.pagbank.challenge.application.UseCase;
import com.pagbank.challenge.domain.cdborder.CdbOrderSearchQuery;
import com.pagbank.challenge.domain.pagination.Pagination;

public abstract class ListOrderUseCase extends UseCase<CdbOrderSearchQuery, Pagination<ListOrderOutput>> {
}

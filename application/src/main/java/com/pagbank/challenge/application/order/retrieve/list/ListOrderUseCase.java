package com.pagbank.challenge.application.order.retrieve.list;

import com.pagbank.challenge.application.UseCase;
import com.pagbank.challenge.domain.order.OrderSearchQuery;
import com.pagbank.challenge.domain.pagination.Pagination;

public abstract class ListOrderUseCase extends UseCase<OrderSearchQuery, Pagination<ListOrderOutput>> {
}

package com.pagbank.challenge.application.customer.retrieve.list;

import com.pagbank.challenge.application.UseCase;
import com.pagbank.challenge.domain.customer.CustomerSearchQuery;
import com.pagbank.challenge.domain.pagination.Pagination;

public abstract class ListCustomerUseCase extends UseCase<CustomerSearchQuery, Pagination<ListCustomerOutput>> {
}

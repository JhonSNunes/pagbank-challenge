package com.pagbank.challenge.application.product.retrieve.list;

import com.pagbank.challenge.application.UseCase;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.ProductSearchQuery;

public abstract class ListProductUseCase extends UseCase<ProductSearchQuery, Pagination<ListProductOutput>> {
}

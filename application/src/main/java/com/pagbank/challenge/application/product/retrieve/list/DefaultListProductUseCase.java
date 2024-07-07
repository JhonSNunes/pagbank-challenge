package com.pagbank.challenge.application.product.retrieve.list;

import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.ProductGateway;
import com.pagbank.challenge.domain.product.ProductSearchQuery;

import java.util.Objects;

public class DefaultListProductUseCase extends ListProductUseCase {

    private final ProductGateway productGateway;

    public DefaultListProductUseCase(final ProductGateway productGateway) {
        this.productGateway = Objects.requireNonNull(productGateway);
    }

    @Override
    public Pagination<ListProductOutput> execute(final ProductSearchQuery query) {

        return this.productGateway.findAll(query)
                .map(ListProductOutput::from);
    }
}

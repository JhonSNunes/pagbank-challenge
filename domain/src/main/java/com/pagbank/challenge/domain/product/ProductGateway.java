package com.pagbank.challenge.domain.product;

import com.pagbank.challenge.domain.pagination.Pagination;

import java.util.Optional;

public interface ProductGateway {
    Product create(Product product);

    Product update(Product product);

    void deleteById(ProductID id);

    Optional<Product> findById(ProductID id);

    Pagination<Product> findAll(ProductSearchQuery query);
}

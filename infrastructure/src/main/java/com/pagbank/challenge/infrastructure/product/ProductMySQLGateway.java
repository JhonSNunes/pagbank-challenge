package com.pagbank.challenge.infrastructure.product;

import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.domain.product.ProductGateway;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.product.ProductSearchQuery;
import com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity;
import com.pagbank.challenge.infrastructure.product.persistence.ProductRepository;
import com.pagbank.challenge.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
public class ProductMySQLGateway implements ProductGateway {
    private final ProductRepository repository;

    public ProductMySQLGateway(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product create(final Product product) {
        return save(product);
    }

    @Override
    public Product update(final Product product) {
        return save(product);
    }

    @Override
    public void deleteById(ProductID id) {
        final String idValue = id.getValue();

        if (this.repository.existsById(idValue)){
            this.repository.deleteById(id.getValue());
        }
    }

    @Override
    public Optional<Product> findById(final ProductID id) {
        return this.repository.findById(id.getValue()).map(ProductJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Product> findAll(ProductSearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var specifications = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(str -> SpecificationUtils.<ProductJpaEntity>like("name", str))
                .orElse(null);

        final var pageResult = this.repository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(ProductJpaEntity::toAggregate).toList()
        );
    }

    private Product save(final Product product) {
        return this.repository.save(ProductJpaEntity.from(product)).toAggregate();
    }
}

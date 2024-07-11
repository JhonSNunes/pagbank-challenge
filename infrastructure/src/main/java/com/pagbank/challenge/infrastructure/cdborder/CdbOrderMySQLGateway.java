package com.pagbank.challenge.infrastructure.cdborder;

import com.pagbank.challenge.domain.cdborder.CdbOrder;
import com.pagbank.challenge.domain.cdborder.CdbOrderGateway;
import com.pagbank.challenge.domain.cdborder.CdbOrderID;
import com.pagbank.challenge.domain.cdborder.CdbOrderSearchQuery;
import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.exceptions.NotFoundException;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.infrastructure.cdborder.persistence.CdbOrderJpaEntity;
import com.pagbank.challenge.infrastructure.cdborder.persistence.CdbOrderRepository;
import com.pagbank.challenge.infrastructure.customer.CustomerMySQLGateway;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity;
import com.pagbank.challenge.infrastructure.product.ProductMySQLGateway;
import com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity;
import com.pagbank.challenge.infrastructure.utils.SqlUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CdbOrderMySQLGateway implements CdbOrderGateway {
    private final CdbOrderRepository repository;

    @Autowired
    private CustomerMySQLGateway customerMySQLGateway;

    @Autowired
    private ProductMySQLGateway productMySQLGateway;

    public CdbOrderMySQLGateway(final CdbOrderRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Transactional
    @Override
    public CdbOrder create(final CdbOrder order) {
        return save(order);
    }

    @Override
    public void deleteById(CdbOrderID id) {
        final String idValue = id.getValue();

        if (this.repository.existsById(idValue)){
            this.repository.deleteById(id.getValue());
        }
    }

    @Override
    public Optional<CdbOrder> findById(final CdbOrderID id) {
        return this.repository.findById(id.getValue()).map(CdbOrderJpaEntity::toAggregate);
    }

    @Override
    public Pagination<CdbOrder> findAll(final CdbOrderSearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), "product.name")
        );

        final var customerId = query.customerId().getValue().isEmpty() ? null : query.customerId().getValue();
        final var productId = query.productId().getValue().isEmpty() ? null : query.productId().getValue();

        final var pageResult = this.repository.findAllOrders(
                SqlUtils.like(SqlUtils.upper(query.terms())),
                customerId,
                productId,
                page
        );

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CdbOrderJpaEntity::toAggregate).toList()
        );
    }

    private CdbOrder save(final CdbOrder order) {
        final var customerId = order.getCustomerId();
        final var productId = order.getProductId();

        final var storageCustomer = customerMySQLGateway
                .findById(customerId)
                .orElseThrow(() -> NotFoundException.with(Customer.class,order.getCustomerId()));

        final var storageProduct = productMySQLGateway
                .findById(productId)
                .orElseThrow(() -> NotFoundException.with(Customer.class, productId));

        final var customerJpaEntity = CustomerJpaEntity.from(storageCustomer);
        final var productJpaEntity = ProductJpaEntity.from(storageProduct);

        return this.repository.save(CdbOrderJpaEntity.from(order, customerJpaEntity, productJpaEntity)).toAggregate();
    }
}

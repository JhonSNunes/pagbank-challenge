package com.pagbank.challenge.infrastructure.cdborder;

import com.pagbank.challenge.domain.cdborder.*;
import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.exceptions.NotFoundException;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.ProductID;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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
                query.perPage()
        );

        final var pageResult = this.repository.findAllOrders(
                SqlUtils.like(SqlUtils.upper(query.terms())),
                SqlUtils.nullIfEmpty(query.customerId()),
                SqlUtils.nullIfEmpty(query.productId()),
                page
        );

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CdbOrderJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public BigDecimal findBalanceByCustomerAndProduct(final CustomerID customerId, final ProductID productId) {
        return this.repository.findBalanceByCustomerAndProduct(
                customerId.getValue(),
                productId.getValue(),
                CdbOrderTransactionType.PURCHASE,
                CdbOrderTransactionType.SELL
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

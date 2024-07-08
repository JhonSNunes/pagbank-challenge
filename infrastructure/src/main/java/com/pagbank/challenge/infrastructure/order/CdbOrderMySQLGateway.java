package com.pagbank.challenge.infrastructure.order;

import com.pagbank.challenge.application.customer.retrieve.get.GetCustomerByIdUseCase;
import com.pagbank.challenge.application.product.retrieve.get.GetProductByIdUseCase;
import com.pagbank.challenge.domain.cdborder.CdbOrder;
import com.pagbank.challenge.domain.cdborder.CdbOrderGateway;
import com.pagbank.challenge.domain.cdborder.CdbOrderID;
import com.pagbank.challenge.domain.cdborder.CdbOrderSearchQuery;
import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity;
import com.pagbank.challenge.infrastructure.order.persistence.OrderJpaEntity;
import com.pagbank.challenge.infrastructure.order.persistence.OrderRepository;
import com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity;
import com.pagbank.challenge.infrastructure.utils.SqlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class CdbOrderMySQLGateway implements CdbOrderGateway {
    private final OrderRepository repository;

    @Autowired
    private GetCustomerByIdUseCase getCustomerByIdUseCase;

    @Autowired
    private GetProductByIdUseCase getProductByIdUseCase;

    public CdbOrderMySQLGateway(OrderRepository repository) {
        this.repository = repository;
    }

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
        return this.repository.findById(id.getValue()).map(OrderJpaEntity::toAggregate);
    }

    @Override
    public Pagination<CdbOrder> findAll(CdbOrderSearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var pageResult = this.repository.findAll(
                SqlUtils.like(SqlUtils.upper(query.terms())),
                query.customerId().getValue(),
                query.productId().getValue(),
                page
        );

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(OrderJpaEntity::toAggregate).toList()
        );
    }

    private CdbOrder save(final CdbOrder order) {
        final var customerOutput = this.getCustomerByIdUseCase.execute(order.getCustomerId().getValue());
        final var productOuput = this.getProductByIdUseCase.execute(order.getProductId().getValue());

        final var customer = Customer.with(
                customerOutput.id(),
                customerOutput.name(),
                customerOutput.age(),
                customerOutput.genre(),
                customerOutput.phone(),
                customerOutput.city(),
                customerOutput.state(),
                customerOutput.country(),
                customerOutput.address(),
                customerOutput.number(),
                customerOutput.zipcode(),
                customerOutput.isActive(),
                customerOutput.createdAt(),
                customerOutput.updatedAt(),
                customerOutput.deletedAt()
        );
        final var customerJpaEntity = CustomerJpaEntity.from(customer);

        final var product = Product.with(
                productOuput.id(),
                productOuput.name(),
                productOuput.rate(),
                productOuput.isActive(),
                productOuput.createdAt(),
                productOuput.updatedAt(),
                productOuput.deletedAt()
        );
        final var productJpaEntity = ProductJpaEntity.from(product);

        return this.repository.save(OrderJpaEntity.from(order, customerJpaEntity, productJpaEntity)).toAggregate();
    }

    private <T> Set<T> nullIfEmpty(final Set<T> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values;
    }
}

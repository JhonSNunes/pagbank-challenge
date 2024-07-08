package com.pagbank.challenge.infrastructure.order;

import com.pagbank.challenge.application.customer.retrieve.get.GetCustomerByIdUseCase;
import com.pagbank.challenge.application.product.retrieve.get.GetProductByIdUseCase;
import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.order.Order;
import com.pagbank.challenge.domain.order.OrderGateway;
import com.pagbank.challenge.domain.order.OrderID;
import com.pagbank.challenge.domain.order.OrderSearchQuery;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity;
import com.pagbank.challenge.infrastructure.order.persistence.OrderJpaEntity;
import com.pagbank.challenge.infrastructure.order.persistence.OrderRepository;
import com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity;
import com.pagbank.challenge.infrastructure.utils.SpecificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderMySQLGateway implements OrderGateway {
    private final OrderRepository repository;

    @Autowired
    private GetCustomerByIdUseCase getCustomerByIdUseCase;

    @Autowired
    private GetProductByIdUseCase getProductByIdUseCase;

    public OrderMySQLGateway(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order create(final Order order) {
        return save(order);
    }

    @Override
    public void deleteById(OrderID id) {
        final String idValue = id.getValue();

        if (this.repository.existsById(idValue)){
            this.repository.deleteById(id.getValue());
        }
    }

    @Override
    public Optional<Order> findById(final OrderID id) {
        return this.repository.findById(id.getValue()).map(OrderJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Order> findAll(OrderSearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var specifications = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(str -> SpecificationUtils.<OrderJpaEntity>like("name", str))
                .orElse(null);

        final var pageResult = this.repository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(OrderJpaEntity::toAggregate).toList()
        );
    }

    private Order save(final Order order) {
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
}

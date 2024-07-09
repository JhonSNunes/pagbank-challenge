package com.pagbank.challenge.infrastructure.cdborder;

import com.pagbank.challenge.application.customer.retrieve.get.GetCustomerByIdUseCase;
import com.pagbank.challenge.application.product.retrieve.get.GetProductByIdUseCase;
import com.pagbank.challenge.domain.cdborder.CdbOrder;
import com.pagbank.challenge.domain.cdborder.CdbOrderGateway;
import com.pagbank.challenge.domain.cdborder.CdbOrderID;
import com.pagbank.challenge.domain.cdborder.CdbOrderSearchQuery;
import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.infrastructure.cdborder.persistence.CdbOrderJpaEntity;
import com.pagbank.challenge.infrastructure.cdborder.persistence.CdbOrderRepository;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity;
import com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity;
import com.pagbank.challenge.infrastructure.utils.SpecificationUtils;
import com.pagbank.challenge.infrastructure.utils.SqlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CdbOrderMySQLGateway implements CdbOrderGateway {
    private final CdbOrderRepository repository;

//    @Autowired
//    private GetCustomerByIdUseCase getCustomerByIdUseCase;
//
//    @Autowired
//    private GetProductByIdUseCase getProductByIdUseCase;

    public CdbOrderMySQLGateway(final CdbOrderRepository repository) {
        this.repository = Objects.requireNonNull(repository);
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
        return this.repository.findById(id.getValue()).map(CdbOrderJpaEntity::toAggregate);
    }

    @Override
    public Pagination<CdbOrder> findAll(final CdbOrderSearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

//        final var pageResult = this.repository.findAllOrders(
//                SqlUtils.like(SqlUtils.upper(query.terms())),
//                query.customerId().getValue(),
//                query.productId().getValue(),
//                page
//        );
//
//        return new Pagination<>(
//                pageResult.getNumber(),
//                pageResult.getSize(),
//                pageResult.getTotalElements(),
//                pageResult.map(CdbOrderJpaEntity::toAggregate).toList()
//        );

        final var specifications = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(str -> SpecificationUtils.<CdbOrderJpaEntity>like("amount", str))
                .orElse(null);

//        final var pageResult = this.repository.findAll(
//                Specification.where(null),
//                SqlUtils.like(SqlUtils.upper(query.terms())),
//                query.customerId().getValue(),
//                query.productId().getValue(),
//                page
//        );

        final var pageResult = this.repository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CdbOrderJpaEntity::toAggregate).toList()
        );
    }

    private CdbOrder save(final CdbOrder order) {
//        final var customerOutput = this.getCustomerByIdUseCase.execute(order.getCustomerId().getValue());
//        final var productOuput = this.getProductByIdUseCase.execute(order.getProductId().getValue());

        final var customer = Customer.with(
//                customerOutput.id(),
//                customerOutput.name(),
//                customerOutput.age(),
//                customerOutput.genre(),
//                customerOutput.phone(),
//                customerOutput.city(),
//                customerOutput.state(),
//                customerOutput.country(),
//                customerOutput.address(),
//                customerOutput.number(),
//                customerOutput.zipcode(),
//                customerOutput.isActive(),
//                customerOutput.createdAt(),
//                customerOutput.updatedAt(),
//                customerOutput.deletedAt()
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        final var customerJpaEntity = CustomerJpaEntity.from(customer);

        final Product product = null;
//        final var product = Product.with(
//                productOuput.id(),
//                productOuput.name(),
//                productOuput.rate(),
//                productOuput.isActive(),
//                productOuput.createdAt(),
//                productOuput.updatedAt(),
//                productOuput.deletedAt()
//        );
        final var productJpaEntity = ProductJpaEntity.from(product);

        return this.repository.save(CdbOrderJpaEntity.from(order, customerJpaEntity, productJpaEntity)).toAggregate();
    }

    private Specification<CdbOrderJpaEntity> customerIdEquals(String customerId) {
        return (root, query, criteriaBuilder) -> {
            if (customerId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("customer").get("id"), customerId);
        };
    }

    private Specification<CdbOrderJpaEntity> productIdEquals(String productId) {
        return (root, query, criteriaBuilder) -> {
            if (productId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("product").get("id"), productId);
        };
    }
}

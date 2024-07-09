package com.pagbank.challenge.infrastructure.customer;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerGateway;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.customer.CustomerSearchQuery;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerRepository;
import com.pagbank.challenge.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerMySQLGateway implements CustomerGateway {
    private final CustomerRepository repository;

    public CustomerMySQLGateway(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer create(final Customer customer) {
        return save(customer);
    }

    @Override
    public Customer update(final Customer customer) {
        return save(customer);
    }

    @Override
    public void deleteById(CustomerID id) {
        final String idValue = id.getValue();

        if (this.repository.existsById(idValue)){
            this.repository.deleteById(id.getValue());
        }
    }

    @Override
    public Optional<Customer> findById(final CustomerID id) {
        return this.repository.findById(id.getValue()).map(CustomerJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Customer> findAll(CustomerSearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var specifications = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(str -> SpecificationUtils.<CustomerJpaEntity>like("name", str))
                .orElse(null);

        final var pageResult = this.repository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CustomerJpaEntity::toAggregate).toList()
        );
    }

    private Customer save(final Customer customer) {
        return this.repository.save(CustomerJpaEntity.from(customer)).toAggregate();
    }
}

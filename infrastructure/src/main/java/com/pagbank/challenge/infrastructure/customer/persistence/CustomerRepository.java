package com.pagbank.challenge.infrastructure.customer.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerJpaEntity, String> {
    Page<CustomerJpaEntity> findAll(Specification<CustomerJpaEntity> whereClause, Pageable page);
}

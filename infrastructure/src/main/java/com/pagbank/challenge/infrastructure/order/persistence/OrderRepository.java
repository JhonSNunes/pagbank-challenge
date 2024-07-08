package com.pagbank.challenge.infrastructure.order.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<OrderJpaEntity, String> {
    Page<OrderJpaEntity> findAll(Specification<OrderJpaEntity> whereClause, Pageable page);
}

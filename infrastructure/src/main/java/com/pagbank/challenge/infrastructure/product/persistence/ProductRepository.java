package com.pagbank.challenge.infrastructure.product.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductJpaEntity, String> {
    Page<ProductJpaEntity> findAll(Specification<ProductJpaEntity> whereClause, Pageable page);
}

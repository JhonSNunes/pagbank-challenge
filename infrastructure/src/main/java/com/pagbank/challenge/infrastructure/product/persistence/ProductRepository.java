package com.pagbank.challenge.infrastructure.product.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<ProductJpaEntity, String> {
    Page<ProductJpaEntity> findAll(Specification<ProductJpaEntity> whereClause, Pageable page);

    @Query("select product.active from Product product where product.id = :productId")
    Boolean findIsActive(@Param("productId") String productId);
}

package com.pagbank.challenge.infrastructure.order.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<OrderJpaEntity, String> {
    @Query("""
        select cdb.*
        from
            cdb_order cdb
        where
                ( :customer_id is null OR cdb.customer_id = :customer_id )
            and
                ( :product_id is null OR cdb.product_id = :product_id )
    """)
    Page<OrderJpaEntity> findAll(
            @Param("terms") String terms,
            @Param("customer_id") String customerId,
            @Param("product_id") String productId,
            Pageable page
    );
}

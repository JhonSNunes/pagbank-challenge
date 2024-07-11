package com.pagbank.challenge.infrastructure.cdborder.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CdbOrderRepository extends JpaRepository<CdbOrderJpaEntity, String> {
    @Query("""
        select
            cdb
        from
            CDBOrder cdb
            inner join Product product on cdb.product.id = product.id 
            inner join Customer customer on cdb.customer.id = customer.id 
        where
            ( :customerId is null OR cdb.customer.id = :customerId )
            and
            ( :productId is null OR cdb.product.id = :productId )
            and
            ( :terms is null OR product.name = :terms )
        order by product.name
    """)
    Page<CdbOrderJpaEntity> findAllOrders(
            @Param("terms") String terms,
            @Param("customerId") String customerId,
            @Param("productId") String productId,
            Pageable page
        );
}

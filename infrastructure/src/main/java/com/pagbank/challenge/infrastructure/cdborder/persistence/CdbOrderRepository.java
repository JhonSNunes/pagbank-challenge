package com.pagbank.challenge.infrastructure.cdborder.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CdbOrderRepository extends JpaRepository<CdbOrderJpaEntity, String> {
    Page<CdbOrderJpaEntity> findByCustomerIdOrProductId(String customerId, String productId, Pageable page);
    Page<CdbOrderJpaEntity> findAll(Specification<CdbOrderJpaEntity> whereClause, Pageable page);

//        @Query("""
//            select
//                cdb.id as id,
//                cdb.customer_id as customerId,
//                cdb.product_id as productId,
//                cdb.amount as amount,
//                cdb.transaction_date as transaction_date,
//                cdb.transaction_type as transaction_type
//            from cdb_order cdb
//            where
//                ( :customer_id is null OR cdb.customer_id = :customerId )
//                and
//                ( :product_id is null OR cdb.product_id = :productId )
//                and
//                ( :terms is null OR cdb.amount = :terms )
//        """)
//        Page<CdbOrderJpaEntity> findAllOrders(
//                @Param("terms") String terms,
//                @Param("customerId") String customerId,
//                @Param("productId") String productId,
//                Pageable page
//        );
}

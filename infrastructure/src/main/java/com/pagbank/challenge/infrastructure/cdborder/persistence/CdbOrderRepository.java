package com.pagbank.challenge.infrastructure.cdborder.persistence;

import com.pagbank.challenge.domain.cdborder.CdbOrderTransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CdbOrderRepository extends JpaRepository<CdbOrderJpaEntity, String> {
    @Query("""
        SELECT
            cdb
        FROM
            CDBOrder cdb
            inner join Product product on cdb.product.id = product.id 
            inner join Customer customer on cdb.customer.id = customer.id 
        WHERE
            ( :customerId is null OR cdb.customer.id = :customerId )
            AND
            ( :productId is null OR cdb.product.id = :productId )
            AND
            ( :terms is null OR product.name = :terms )
        ORDER BY product.name ASC, customer.name ASC
    """)
    Page<CdbOrderJpaEntity> findAllOrders(
            @Param("terms") String terms,
            @Param("customerId") String customerId,
            @Param("productId") String productId,
            Pageable page
        );

    @Query("""
        SELECT
            SUM(CASE WHEN cdb.transactionType = :purchaseType THEN cdb.amount ELSE 0 END)
            -
            SUM(CASE WHEN cdb.transactionType = :sellType THEN cdb.amount ELSE 0 END) AS BALANCE 
        FROM
            CDBOrder cdb
            inner join Product product on cdb.product.id = product.id 
            inner join Customer customer on cdb.customer.id = customer.id 
        WHERE
            cdb.customer.id = :customerId
            AND cdb.product.id = :productId
    """)
    BigDecimal findBalanceByCustomerAndProduct(
            @Param("customerId") String customerId,
            @Param("productId") String productId,
            @Param("purchaseType") CdbOrderTransactionType purchaseType,
            @Param("sellType") CdbOrderTransactionType sellType
            );
}

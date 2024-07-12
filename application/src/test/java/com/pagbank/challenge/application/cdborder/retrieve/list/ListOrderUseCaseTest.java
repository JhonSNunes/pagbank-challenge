package com.pagbank.challenge.application.cdborder.retrieve.list;

import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.cdborder.*;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.ProductID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListOrderUseCaseTest {

    @InjectMocks
    private DefaultListOrderUseCase useCase;

    @Mock
    private CdbOrderGateway cdbOrderGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(cdbOrderGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListOrders_shouldReturnProducts() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedDirection = "asc";

        final var query = new CdbOrderSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                null,
                null
        );

        final var customerId1 = CustomerID.unique();
        final var productId1 = ProductID.unique();

        final var customerId2 = CustomerID.unique();
        final var productId2 = ProductID.unique();

        final var products = List.of(
            CdbOrder.createOrder(customerId1, productId1, new BigDecimal("100.50"), CdbOrderTransactionType.SELL),
            CdbOrder.createOrder(customerId2, productId2, new BigDecimal("569.00"), CdbOrderTransactionType.PURCHASE)
        );

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, products.size(), products);

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(ListOrderOutput::from);

        when(cdbOrderGateway.findAll(eq(query)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Mockito.verify(cdbOrderGateway, times(1)).findAll(eq(query));
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(products.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenHasNoResults_shouldReturnEmptyOrders() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedDirection = "asc";

        final var query = new CdbOrderSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                null,
                null
        );

        final var orders = List.<CdbOrder>of();

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, orders.size(), orders);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(ListOrderOutput::from);

        when(cdbOrderGateway.findAll(eq(query)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Mockito.verify(cdbOrderGateway, times(1)).findAll(eq(query));
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(orders.size(), actualResult.total());
    }
}

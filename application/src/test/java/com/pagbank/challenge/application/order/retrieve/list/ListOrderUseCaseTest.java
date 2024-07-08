package com.pagbank.challenge.application.order.retrieve.list;

import com.pagbank.challenge.application.product.retrieve.list.DefaultListProductUseCase;
import com.pagbank.challenge.application.product.retrieve.list.ListProductOutput;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.order.*;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.product.ProductSearchQuery;
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
    private OrderGateway orderGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(orderGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListOrders_shouldReturnProducts() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var query = new OrderSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                null,
                null
        );

        final var customerId1 = CustomerID.unique();
        final var productId1 = ProductID.unique();

        final var customerId2 = CustomerID.unique();
        final var productId2 = ProductID.unique();

        final var products = List.of(
            Order.createOrder(customerId1, productId1, new BigDecimal("100.50"), OrderTransactionType.SELL),
            Order.createOrder(customerId2, productId2, new BigDecimal("569.00"), OrderTransactionType.PURCHASE)
        );

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, products.size(), products);

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(ListOrderOutput::from);

        when(orderGateway.findAll(eq(query)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Mockito.verify(orderGateway, times(1)).findAll(eq(query));
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
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var query = new OrderSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                null,
                null
        );

        final var orders = List.<Order>of();

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, orders.size(), orders);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(ListOrderOutput::from);

        when(orderGateway.findAll(eq(query)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Mockito.verify(orderGateway, times(1)).findAll(eq(query));
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(orders.size(), actualResult.total());
    }
}

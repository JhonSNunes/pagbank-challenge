package com.pagbank.challenge.application.cdborder.retrieve.list;

import com.pagbank.challenge.domain.cdborder.*;
import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.Product;
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

        final var customer1 = Customer.registerCustomer("José", 25, "masculino", "47 99666952", "Joinville", "Santa Catarina", "Brasil", "Rua Bolinha", "14", "495959595");
        final var customer2 = Customer.registerCustomer("Maria", 30, "feminino", "47 99666555", "Joinville", "Santa Catarina", "Brasil", "Rua Bolinha", "14", "495959595");

        final var product1 = Product.createProduct("CDB 200%", new BigDecimal("200"), true);
        final var product2 = Product.createProduct("CDB 110%", new BigDecimal("110"), true);

        final var order1 = CdbOrder.createOrder(customer1.getId(), product1.getId(), new BigDecimal("100.50"), CdbOrderTransactionType.SELL);
        final var order2 = CdbOrder.createOrder(customer2.getId(), product2.getId(), new BigDecimal("569.00"), CdbOrderTransactionType.PURCHASE);

        final var products = List.of(
                new CdbOrderView(order1, customer1, product1),
                new CdbOrderView(order2, customer2, product2)
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

        final var query = new CdbOrderSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                null,
                null
        );

        final var orders = List.<CdbOrderView>of();

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

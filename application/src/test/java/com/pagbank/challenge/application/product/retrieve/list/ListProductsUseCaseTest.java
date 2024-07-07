package com.pagbank.challenge.application.product.retrieve.list;

import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.domain.product.ProductGateway;
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
public class ListProductsUseCaseTest {

    @InjectMocks
    private DefaultListProductUseCase useCase;

    @Mock
    private ProductGateway productGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(productGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListProducts_shouldReturnProducts() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var query = new ProductSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var products = List.of(
            Product.createProduct("CDB 110%", new BigDecimal("110.00"), true),
            Product.createProduct("CDB Poupar Automático", new BigDecimal("103.00"), true)
        );

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, products.size(), products);

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(ListProductOutput::from);

        when(productGateway.findAll(eq(query)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Mockito.verify(productGateway, times(1)).findAll(eq(query));
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(products.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenHasNoResults_shouldReturnEmptyProducts() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var query = new ProductSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var products = List.<Product>of();

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, products.size(), products);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(ListProductOutput::from);

        when(productGateway.findAll(eq(query)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Mockito.verify(productGateway, times(1)).findAll(eq(query));
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(products.size(), actualResult.total());
    }
}

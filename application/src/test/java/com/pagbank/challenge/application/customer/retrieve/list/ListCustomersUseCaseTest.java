package com.pagbank.challenge.application.customer.retrieve.list;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerGateway;
import com.pagbank.challenge.domain.customer.CustomerSearchQuery;
import com.pagbank.challenge.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCustomersUseCaseTest {

    @InjectMocks
    private DefaultListCustomerUseCase useCase;

    @Mock
    private CustomerGateway customerGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(customerGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCustomers_shouldReturnCustomer() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var query = new CustomerSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var customers = List.of(
            Customer.registerCustomer(
                    "José",
                    33,
                    "masculino",
                    "(47) 99900-3333",
                    "Joinville",
                    "Santa Catarina",
                    "Brasil",
                    "Rua teste da silva",
                    "232333",
                    "895952225"
            ),
            Customer.registerCustomer(
                    "Maria",
                    29,
                    "feminino",
                    "(47) 99900-4444",
                    "Rio de Janeiro",
                    "Rio de Janeiro",
                    "Brasil",
                    "Rua teste da silva sauro",
                    "232456",
                    "7876887909"
            )
        );

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, customers.size(), customers);

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(ListCustomerOutput::from);

        when(customerGateway.findAll(eq(query)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Mockito.verify(customerGateway, times(1)).findAll(eq(query));
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(customers.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenHasNoResults_shouldReturnEmptyCustomers() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var query = new CustomerSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var customers = List.<Customer>of();

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, customers.size(), customers);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(ListCustomerOutput::from);

        when(customerGateway.findAll(eq(query)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        Mockito.verify(customerGateway, times(1)).findAll(eq(query));
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(customers.size(), actualResult.total());
    }
}

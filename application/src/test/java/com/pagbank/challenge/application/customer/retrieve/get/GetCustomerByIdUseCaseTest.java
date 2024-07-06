package com.pagbank.challenge.application.customer.retrieve.get;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerGateway;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCustomerByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCustomerByIdUseCase useCase;

    @Mock
    private CustomerGateway customerGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(customerGateway);
    }

    // 1. Teste do caminho feliz
    @Test
    public void givenAValidId_whenCallsGetCustomer_shouldBeOK() {
        final var expectedName = "José";
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";

        final var persistedCustomer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        final var expectedId = persistedCustomer.getId();

        when(customerGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(persistedCustomer.clone()));

        final var actualCustomer = useCase.execute(expectedId.getValue());

        Mockito.verify(customerGateway, times(1)).findById(eq(expectedId));
        Assertions.assertEquals(expectedId, actualCustomer.id());
        Assertions.assertEquals(expectedName, actualCustomer.name());
        Assertions.assertEquals(expectedAge, actualCustomer.age());
        Assertions.assertEquals(expectedGenre, actualCustomer.genre());
        Assertions.assertEquals(expectedPhone, actualCustomer.phone());
        Assertions.assertEquals(expectedCity, actualCustomer.city());
        Assertions.assertEquals(expectedState, actualCustomer.state());
        Assertions.assertEquals(expectedCountry, actualCustomer.country());
        Assertions.assertEquals(expectedAddress, actualCustomer.address());
        Assertions.assertEquals(expectedNumber, actualCustomer.number());
        Assertions.assertEquals(expectedZipcode, actualCustomer.zipcode());
        Assertions.assertEquals(persistedCustomer.isActive(), actualCustomer.isActive());
        Assertions.assertEquals(persistedCustomer.getCreatedAt(), actualCustomer.createdAt());
        Assertions.assertEquals(persistedCustomer.getUpdatedAt(), actualCustomer.updatedAt());
        Assertions.assertEquals(persistedCustomer.getDeletedAt(), actualCustomer.deletedAt());
    }

    // 2. Teste atualizar cliente com ID inválido
    @Test
    public void givenAInvalidId_whenCallsGetCustomer_shouldReturnNotFound() {
        final var invalidCustomerId = CustomerID.from("AN-INVALID-ID-123");
        final var expectedErrorMessage = "Customer with ID %s was not found".formatted(invalidCustomerId.getValue());

        when(customerGateway.findById(eq(invalidCustomerId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
            DomainException.class,
            () -> useCase.execute(invalidCustomerId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Mockito.verify(customerGateway, times(1)).findById(eq(invalidCustomerId));
    }
}

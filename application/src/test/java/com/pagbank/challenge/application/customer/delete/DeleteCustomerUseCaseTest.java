package com.pagbank.challenge.application.customer.delete;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerGateway;
import com.pagbank.challenge.domain.customer.CustomerID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCustomerUseCaseTest {

    @InjectMocks
    private DefaultDeleteCustomerUseCase useCase;

    @Mock
    private CustomerGateway customerGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(customerGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCustomer_shouldBeOK() {
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

        doNothing().when(customerGateway).deleteById(any());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Mockito.verify(customerGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCustomer_shouldBeOK() {
        final var invalidCustomerId = CustomerID.from("AN-INVALID-ID-123");

        doNothing().when(customerGateway).deleteById(any());

        Assertions.assertDoesNotThrow(() -> useCase.execute(invalidCustomerId.getValue()));
        Mockito.verify(customerGateway, times(1)).deleteById(eq(invalidCustomerId));
    }
}

package com.pagbank.challenge.application.customer.update;

import com.pagbank.challenge.application.customer.create.CreateCustomerCommand;
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

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCustomerUseCaseTest {

    @InjectMocks
    private DefaultUpdateCustomerUseCase useCase;

    @Mock
    private CustomerGateway customerGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(customerGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCustomer_shouldReturnCustomerId() {
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
        final var expectedIsActive = true;

        final var changedPhone = "(47) 99900-4444";

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

        final var command = UpdateCustomerCommand.with(
                expectedId.getValue(),
                persistedCustomer.getName(),
                persistedCustomer.getAge(),
                persistedCustomer.getGenre(),
                changedPhone,
                persistedCustomer.getCity(),
                persistedCustomer.getState(),
                persistedCustomer.getCountry(),
                persistedCustomer.getAddress(),
                persistedCustomer.getNumber(),
                persistedCustomer.getZipcode()
        );

        when(customerGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(persistedCustomer.clone()));

        when(customerGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(customerGateway, times(1)).update(Mockito.argThat(
                customer -> {
                    return Objects.equals(expectedName, customer.getName())
                            && Objects.equals(expectedAge, customer.getAge())
                            && Objects.equals(expectedGenre, customer.getGenre())
                            && !Objects.equals(persistedCustomer.getPhone(), customer.getPhone())
                            && Objects.equals(expectedId, customer.getId())
                            && Objects.equals(persistedCustomer.getCreatedAt(), customer.getCreatedAt())
                            && persistedCustomer.getUpdatedAt().isBefore(customer.getUpdatedAt())
                            && Objects.equals(persistedCustomer.getDeletedAt(), customer.getDeletedAt())
                            && Objects.equals(expectedIsActive, customer.isActive());
                }
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCustomer_shouldThrowsError() {
        final String invalidName = null;
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
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorSize = 1;

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

        final var command = UpdateCustomerCommand.with(
                expectedId.getValue(),
                invalidName,
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

        when(customerGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(persistedCustomer.clone()));

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorSize, notification.getErrors().size());

        Mockito.verify(customerGateway, times(0)).update(any());
    }

    @Test
    public void givenAInvalidID_whenCallsUpdateCustomer_shouldThrowsError() {
        final var invalidCustomerId = CustomerID.from("AN-INVALID-ID-123");
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
        final var expectedErrorMessage = "Customer with ID %s was not found".formatted(invalidCustomerId.getValue());

        final var command = UpdateCustomerCommand.with(
                invalidCustomerId.getValue(),
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

        when(customerGateway.findById(eq(invalidCustomerId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(customerGateway, times(1)).findById(eq(invalidCustomerId));
        Mockito.verify(customerGateway, times(0)).update(any());
    }
}

package com.pagbank.challenge.application.customer.create;

import com.pagbank.challenge.domain.customer.CustomerGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateCustomerUseCaseTest {

    @InjectMocks
    private DefaultCreateCustomerUseCase useCase;

    @Mock
    private CustomerGateway customerGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(customerGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateCustomer_shouldReturnCustomerId() {
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

            final var command = CreateCustomerCommand.with(
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

            when(customerGateway.create(any()))
                    .thenAnswer(returnsFirstArg());

            final var actualOutput = useCase.execute(command).get();
            Assertions.assertNotNull(actualOutput);
            Assertions.assertNotNull(actualOutput.id());

            Mockito.verify(customerGateway, times(1)).create(Mockito.argThat(
                    customer -> {
                        return Objects.equals(expectedName, customer.getName())
                                && Objects.equals(expectedAge, customer.getAge())
                                && Objects.nonNull(customer.getId())
                                && Objects.nonNull(customer.getCreatedAt())
                                && Objects.nonNull(customer.getUpdatedAt())
                                && Objects.isNull(customer.getDeletedAt())
                                && Objects.equals(expectedIsActive, customer.isActive());
                    }
            ));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateCustomer_shouldThrowsError() {
        final String expectedName = null;
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

        final var command = CreateCustomerCommand.with(
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

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorSize, notification.getErrors().size());

        Mockito.verify(customerGateway, times(0)).create(any());
    }
}

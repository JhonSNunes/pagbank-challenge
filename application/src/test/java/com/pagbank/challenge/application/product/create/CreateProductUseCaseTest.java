package com.pagbank.challenge.application.product.create;

import com.pagbank.challenge.domain.product.ProductGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateProductUseCaseTest {

    @InjectMocks
    private DefaultCreateProductUseCase useCase;

    @Mock
    private ProductGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateProduct_shouldReturnProductId() {
            final var expectedName = "CDB 200%";
            final var expectedRate = new BigDecimal("200.00");
            final var expectedIsActive = true;

            final var command = CreateProductCommand.with(
                    expectedName,
                    expectedRate,
                    expectedIsActive
            );

            when(gateway.create(any()))
                    .thenAnswer(returnsFirstArg());

            final var actualOutput = useCase.execute(command).get();
            Assertions.assertNotNull(actualOutput);
            Assertions.assertNotNull(actualOutput.id());

            Mockito.verify(gateway, times(1)).create(Mockito.argThat(
                    entity -> {
                        return Objects.equals(expectedName, entity.getName())
                                && Objects.equals(expectedRate, entity.getRate())
                                && Objects.equals(expectedIsActive, entity.isActive())
                                && Objects.nonNull(entity.getId());
                    }
            ));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateProduct_shouldThrowsError() {
        final String expectedName = null;
        final var expectedRate = new BigDecimal("200.00");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorSize = 1;

        final var command = CreateProductCommand.with(
                expectedName,
                expectedRate,
                expectedIsActive
        );

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorSize, notification.getErrors().size());

        Mockito.verify(gateway, times(0)).create(any());
    }
}

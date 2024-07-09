package com.pagbank.challenge.application.cdborder.create;

import com.pagbank.challenge.domain.cdborder.CdbOrderTransactionType;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.cdborder.CdbOrderGateway;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateCdbOrderUseCaseTest {

    @InjectMocks
    private DefaultCreateCdbOrderUseCase useCase;

    @Mock
    private CdbOrderGateway cdbOrderGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(cdbOrderGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsPurchaseOrder_shouldReturnOrderId() {
            final var expectedAmount = new BigDecimal("101453.00");
            final var expectedCustomerId = CustomerID.unique();
            final var expectedProductId = ProductID.unique();
            final var expectedTransactionType = CdbOrderTransactionType.PURCHASE;

            final var command = CreateCdbOrderCommand.with(
                    expectedCustomerId,
                    expectedProductId,
                    expectedAmount,
                    expectedTransactionType
            );

            when(cdbOrderGateway.create(any()))
                    .thenAnswer(returnsFirstArg());

            final var actualOutput = useCase.execute(command).get();
            Assertions.assertNotNull(actualOutput);
            Assertions.assertNotNull(actualOutput.id());

            Mockito.verify(cdbOrderGateway, times(1)).create(Mockito.argThat(
                    entity -> {
                        return Objects.equals(expectedCustomerId, entity.getCustomerId())
                                && Objects.equals(expectedProductId, entity.getProductId())
                                && Objects.equals(expectedAmount, entity.getAmount())
                                && Objects.nonNull(entity.getId());
                    }
            ));
    }

    @Test
    public void givenAInvalidCommandWithoutCustomerId_whenCallsCreateProduct_shouldThrowsError() {
        final var expectedCustomerId = CustomerID.unique();
        final var expectedProductId = ProductID.unique();
        final BigDecimal expectedAmount = null;
        final var expectedTransactionType = CdbOrderTransactionType.PURCHASE;

        final var expectedErrorMessage = "'amount' should not be null";
        final var expectedErrorSize = 1;

        final var command = CreateCdbOrderCommand.with(
                expectedCustomerId,
                expectedProductId,
                expectedAmount,
                expectedTransactionType
        );

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorSize, notification.getErrors().size());

        Mockito.verify(cdbOrderGateway, times(0)).create(any());
    }
}
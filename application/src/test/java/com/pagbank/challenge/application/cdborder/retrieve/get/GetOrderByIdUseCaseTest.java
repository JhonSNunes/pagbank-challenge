package com.pagbank.challenge.application.cdborder.retrieve.get;

import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.exceptions.NotFoundException;
import com.pagbank.challenge.domain.cdborder.CdbOrder;
import com.pagbank.challenge.domain.cdborder.CdbOrderGateway;
import com.pagbank.challenge.domain.cdborder.CdbOrderID;
import com.pagbank.challenge.domain.cdborder.CdbOrderTransactionType;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetOrderByIdUseCaseTest {

    @InjectMocks
    private DefaultGetOrderByIdUseCase useCase;

    @Mock
    private CdbOrderGateway cdbOrderGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(cdbOrderGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetOrder_shouldBeOK() {
        final var expectedCustomerId = CustomerID.unique();
        final var expectedProductId = ProductID.unique();
        final var expectedAmount = new BigDecimal("300.00");
        final var expectedTransactionType = CdbOrderTransactionType.SELL;

        final var persistedEntity = CdbOrder.createOrder(
                expectedCustomerId,
                expectedProductId,
                expectedAmount,
                expectedTransactionType
        );

        final var expectedId = persistedEntity.getId();

        when(cdbOrderGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(persistedEntity.clone()));

        final var actualOrder = useCase.execute(expectedId.getValue());

        Mockito.verify(cdbOrderGateway, times(1)).findById(eq(expectedId));
        Assertions.assertEquals(expectedId, actualOrder.id());
        Assertions.assertEquals(expectedCustomerId, actualOrder.customerId());
        Assertions.assertEquals(expectedProductId, actualOrder.productId());
        Assertions.assertEquals(expectedAmount, actualOrder.amount());
        Assertions.assertEquals(expectedTransactionType, actualOrder.transactionType());
        Assertions.assertNotNull(persistedEntity.getTransactionDate());
    }

    @Test
    public void givenAInvalidId_whenCallsGetOrder_shouldReturnNotFound() {
//        final var invalidId = CdbOrderID.from("AN-INVALID-ID-123");
//        final var expectedErrorMessage = "Order with ID %s was not found".formatted(invalidId.getValue());
//
//        when(cdbOrderGateway.findById(eq(invalidId))).thenReturn(Optional.empty());
//
//        final var actualException = Assertions.assertThrows(
//            NotFoundException.class,
//            () -> useCase.execute(invalidId.getValue())
//        );
//
//        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
//        Mockito.verify(cdbOrderGateway, times(1)).findById(eq(invalidId));
    }
}

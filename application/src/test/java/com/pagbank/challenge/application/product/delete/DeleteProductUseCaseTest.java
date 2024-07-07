package com.pagbank.challenge.application.product.delete;

import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.domain.product.ProductGateway;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class DeleteProductUseCaseTest {

    @InjectMocks
    private DefaultDeleteProductUseCase useCase;

    @Mock
    private ProductGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteProduct_shouldBeOK() {
        final var expectedName = "CDB 200%";
        final var expectedRate = new BigDecimal("200.00");
        final var expectedIsActive = true;

        final var persistedEntity = Product.createProduct(
                expectedName,
                expectedRate,
                expectedIsActive
        );

        final var expectedId = persistedEntity.getId();

        doNothing().when(gateway).deleteById(any());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Mockito.verify(gateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteProduct_shouldBeOK() {
        final var invalidId = ProductID.from("AN-INVALID-ID-123");

        doNothing().when(gateway).deleteById(any());

        Assertions.assertDoesNotThrow(() -> useCase.execute(invalidId.getValue()));
        Mockito.verify(gateway, times(1)).deleteById(eq(invalidId));
    }
}

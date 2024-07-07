package com.pagbank.challenge.application.product.retrieve.get;

import com.pagbank.challenge.domain.exceptions.NotFoundException;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetProductByIdUseCaseTest {

    @InjectMocks
    private DefaultGetProductByIdUseCase useCase;

    @Mock
    private ProductGateway productGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(productGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetProduct_shouldBeOK() {
        final var expectedName = "CDB 300%";
        final var expectedRate = new BigDecimal("300.00");
        final var expectedIsActive = true;

        final var persistedEntity = Product.createProduct(expectedName, expectedRate, expectedIsActive);

        final var expectedId = persistedEntity.getId();

        when(productGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(persistedEntity.clone()));

        final var actualProduct = useCase.execute(expectedId.getValue());

        Mockito.verify(productGateway, times(1)).findById(eq(expectedId));
        Assertions.assertEquals(expectedId, actualProduct.id());
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedRate, actualProduct.rate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertEquals(persistedEntity.getCreatedAt(), actualProduct.createdAt());
        Assertions.assertEquals(persistedEntity.getUpdatedAt(), actualProduct.updatedAt());
        Assertions.assertEquals(persistedEntity.getDeletedAt(), actualProduct.deletedAt());
    }

    @Test
    public void givenAInvalidId_whenCallsGetProduct_shouldReturnNotFound() {
        final var invalidId = ProductID.from("AN-INVALID-ID-123");
        final var expectedErrorMessage = "Product with ID %s was not found".formatted(invalidId.getValue());

        when(productGateway.findById(eq(invalidId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
            NotFoundException.class,
            () -> useCase.execute(invalidId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Mockito.verify(productGateway, times(1)).findById(eq(invalidId));
    }
}

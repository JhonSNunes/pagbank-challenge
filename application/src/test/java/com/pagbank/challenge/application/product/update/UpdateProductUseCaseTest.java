package com.pagbank.challenge.application.product.update;

import com.pagbank.challenge.domain.exceptions.DomainException;
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
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateProductUseCaseTest {

    @InjectMocks
    private DefaultUpdateProductUseCase useCase;

    @Mock
    private ProductGateway productGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(productGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateProduct_shouldReturnProductId() {
        final var expectedName = "CDB 2000%";
        final var expectedRate = new BigDecimal("200.00");
        final var expectedIsActive = true;

        final var changedName = "CDB 200%";

        final var persistedProduct = Product.createProduct(expectedName, expectedRate, expectedIsActive);

        final var expectedId = persistedProduct.getId();

        final var command = UpdateProductCommand.with(
                expectedId.getValue(),
                changedName,
                persistedProduct.getRate(),
                persistedProduct.isActive()
        );

        when(productGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(persistedProduct.clone()));

        when(productGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(productGateway, times(1)).update(Mockito.argThat(
                product -> {
                    return Objects.equals(changedName, product.getName())
                            && Objects.equals(expectedRate, product.getRate())
                            && Objects.equals(expectedIsActive, product.isActive())
                            && Objects.equals(expectedId, product.getId())
                            && Objects.equals(persistedProduct.getCreatedAt(), product.getCreatedAt())
                            && persistedProduct.getUpdatedAt().isBefore(product.getUpdatedAt())
                            && Objects.equals(persistedProduct.getDeletedAt(), product.getDeletedAt());
                }
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateProduct_shouldThrowsError() {
        final String invalidName = null;
        final var expectedName = "CDBEEEE";
        final var expectedRate = new BigDecimal("200.00");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorSize = 1;

        final var persistedProduct = Product.createProduct(expectedName, expectedRate, expectedIsActive);

        final var expectedId = persistedProduct.getId();

        final var command = UpdateProductCommand.with(
                expectedId.getValue(),
                invalidName,
                expectedRate,
                expectedIsActive
        );

        when(productGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(persistedProduct.clone()));

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorSize, notification.getErrors().size());

        Mockito.verify(productGateway, times(0)).update(any());
    }

    @Test
    public void givenAInvalidID_whenCallsUpdateProduct_shouldThrowsError() {
        final var invalidId = ProductID.from("AN-INVALID-ID-123");
        final var expectedName = "CDB 200%";
        final var expectedRate = new BigDecimal("200.00");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Product with ID %s was not found".formatted(invalidId.getValue());

        final var command = UpdateProductCommand.with(
                invalidId.getValue(),
                expectedName,
                expectedRate,
                expectedIsActive
        );

        when(productGateway.findById(eq(invalidId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(productGateway, times(1)).findById(eq(invalidId));
        Mockito.verify(productGateway, times(0)).update(any());
    }
}

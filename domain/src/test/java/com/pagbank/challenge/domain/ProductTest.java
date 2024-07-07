package com.pagbank.challenge.domain;

import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.domain.validation.handler.Notification;
import com.pagbank.challenge.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ProductTest {
    @Test
    public void givenAValidParams_whenCallNewProduct_thenInstantiateAProduct() {
        final var expectedName = "John O' Connor";
        final var expectedRate = new BigDecimal("110");
        final var expectedIsActive = true;

        final var product = Product.createProduct(
                expectedName,
                expectedRate
        );

        Assertions.assertNotNull(product);
        Assertions.assertEquals(expectedName, product.getName());
        Assertions.assertEquals(expectedRate, product.getRate());
        Assertions.assertEquals(expectedIsActive, product.isActive());
    }

    @Test
    public void givenAInvalidNullName_whenCallNewProductAndValidate_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedRate = new BigDecimal("110");
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var product = Product.createProduct(
                expectedName,
                expectedRate
        );

        Assertions.assertNotNull(product);

        final var notification = Notification.create();

        product.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    @Test
    public void givenAInvalidNullRate_whenCallNewProductAndValidate_thenShouldReceiveError() {
        final var expectedName = "CDB 110%";
        final BigDecimal expectedRate = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rate' should not be null";

        final var product = Product.createProduct(
                expectedName,
                expectedRate
        );

        Assertions.assertNotNull(product);

        final var notification = Notification.create();

        product.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    @Test
    public void givenAValidInvalidNegativeRate_whenCallNewProductAndValidate_thenShouldReceiveError() {
        final var expectedName = "CDB 110%";
        final var expectedRate = new BigDecimal("-1");
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rate' must be greather than zero!";

        final var product = Product.createProduct(
                expectedName,
                expectedRate
        );

        Assertions.assertNotNull(product);

        final var notification = Notification.create();

        product.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    @Test
    public void givenAValidProduct_whenCallActivate_thenShouldActivateProduct() {
        final var expectedName = "CDB 110%";
        final BigDecimal expectedRate = new BigDecimal("110.00");
        final var expectedIsActive = true;

        final var product = Product.createProduct(
                expectedName,
                expectedRate
        );
        final var createdAt = product.getCreatedAt();
        final var updatedAt = product.getUpdatedAt();

        product.activate();

        Assertions.assertEquals(expectedIsActive, product.isActive());
        Assertions.assertEquals(createdAt, product.getCreatedAt());
        Assertions.assertTrue(product.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(product.getDeletedAt());
    }

    @Test
    public void givenAValidProduct_whenCallDeactivate_thenShouldInactivateProduct() {
        final var expectedName = "CDB 110%";
        final BigDecimal expectedRate = new BigDecimal("110.00");

        final var product = Product.createProduct(
                expectedName,
                expectedRate
        );
        final var createdAt = product.getCreatedAt();
        final var updatedAt = product.getUpdatedAt();

        Assertions.assertNull(product.getDeletedAt());
        Assertions.assertTrue(product.isActive());

        product.deactivate();

        Assertions.assertFalse(product.isActive());
        Assertions.assertNotNull(product.getDeletedAt());
        Assertions.assertEquals(createdAt, product.getCreatedAt());
        Assertions.assertTrue(product.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    public void givenAValidActiveCustomer_whenCallUpdate_thenReturnCustomerUpdated() {
        final var expectedName = "CDB 110%";
        final BigDecimal expectedRate = new BigDecimal("110.00");

        final var product = Product.createProduct(
                "Um nome todo errado",
                expectedRate
        );

        Assertions.assertNotNull(product);
        Assertions.assertTrue(product.isActive());
        Assertions.assertDoesNotThrow(() -> product.validate(new ThrowsValidationHandler()));

        final var previousId = product.getId();
        final var previousName = product.getName();
        final var previousRate = product.getRate();
        final var previousUpdatedAt = product.getUpdatedAt();

        final var updatedProduct = product.update(
                expectedName,
                expectedRate
        );

        Assertions.assertNotNull(updatedProduct);
        Assertions.assertTrue(updatedProduct.isActive());
        Assertions.assertDoesNotThrow(() -> updatedProduct.validate(new ThrowsValidationHandler()));
        Assertions.assertTrue(updatedProduct.getUpdatedAt().isAfter(previousUpdatedAt));
        Assertions.assertEquals(updatedProduct.getCreatedAt(), updatedProduct.getCreatedAt());
        Assertions.assertNull(updatedProduct.getDeletedAt());
        Assertions.assertEquals(updatedProduct.getId(), previousId);
        Assertions.assertEquals(updatedProduct.getRate(), previousRate);
        Assertions.assertNotEquals(updatedProduct.getName(), previousName);
    }
}

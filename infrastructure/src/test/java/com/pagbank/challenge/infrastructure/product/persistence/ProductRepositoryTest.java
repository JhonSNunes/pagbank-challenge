package com.pagbank.challenge.infrastructure.product.persistence;

import com.pagbank.challenge.MySQLGatewayTest;
import com.pagbank.challenge.domain.product.Product;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

@MySQLGatewayTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product defaultProduct;

    @BeforeEach
    public void registerDefaultProduct() {
        final var expectedName = "CDB 101%";
        final var expectedRate = new BigDecimal("101.00");
        final var expectedIsActive = true;

        this.defaultProduct = Product.createProduct(expectedName, expectedRate, expectedIsActive);
    }

    @Test
    public void givenAInvalidNullName_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "name";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity.name";

        final var actualProduct = ProductJpaEntity.from(this.defaultProduct);

        actualProduct.setName(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> productRepository.save(actualProduct));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullRate_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "rate";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity.rate";

        final var actualProduct = ProductJpaEntity.from(this.defaultProduct);

        actualProduct.setRate(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> productRepository.save(actualProduct));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullActive_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "active";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity.active";

        final var actualProduct = ProductJpaEntity.from(this.defaultProduct);

        actualProduct.setActive(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> productRepository.save(actualProduct));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullCreatedAt_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "createdAt";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity.createdAt";

        final var actualProduct = ProductJpaEntity.from(this.defaultProduct);

        actualProduct.setCreatedAt(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> productRepository.save(actualProduct));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullUpdatedAt_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "updatedAt";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity.updatedAt";

        final var actualProduct = ProductJpaEntity.from(this.defaultProduct);

        actualProduct.setUpdatedAt(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> productRepository.save(actualProduct));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }
}

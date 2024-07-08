package com.pagbank.challenge.infrastructure.product;

import com.pagbank.challenge.MySQLGatewayTest;
import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.product.ProductSearchQuery;
import com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity;
import com.pagbank.challenge.infrastructure.product.persistence.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

@MySQLGatewayTest
public class ProductMySQLGatewayTest {

    @Autowired
    private ProductMySQLGateway productMySQLGateway;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void givenAValidProduct_whenCallsCreate_shouldReturnANewProduct() {
        final var expectedName = "CDB 300%";
        final var expectedRate = new BigDecimal("300.00");
        final var expectedIsActive = true;

        final var product = Product.createProduct(expectedName, expectedRate, expectedIsActive);

        Assertions.assertEquals(0, productRepository.count());

        final var productId = product.getId();
        final var actualProduct = productMySQLGateway.create(product);

        Assertions.assertEquals(1, productRepository.count());
        Assertions.assertEquals(productId, actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.getName());
        Assertions.assertEquals(expectedRate, actualProduct.getRate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertEquals(product.getCreatedAt(), actualProduct.getCreatedAt());
        Assertions.assertEquals(product.getUpdatedAt(), actualProduct.getUpdatedAt());
        Assertions.assertNull(actualProduct.getDeletedAt());

        final var persistedProduct = productRepository.findById(productId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedProduct.getName());
        Assertions.assertEquals(expectedRate, persistedProduct.getRate());
        Assertions.assertEquals(expectedIsActive, persistedProduct.getActive());
        Assertions.assertEquals(product.getCreatedAt(), persistedProduct.getCreatedAt());
        Assertions.assertEquals(product.getUpdatedAt(), persistedProduct.getUpdatedAt());
        Assertions.assertEquals(product.getDeletedAt(), persistedProduct.getDeletedAt());
        Assertions.assertNull(persistedProduct.getDeletedAt());
    }

    @Test
    public void givenAValidProduct_whenCallsUpdate_shouldReturnAUpdatedProduct() {
        final var expectedName = "CDB 300%";
        final var expectedRate = new BigDecimal("300.00");
        final var expectedIsActive = true;

        final var product = Product.createProduct("CDB 3000%", new BigDecimal("300.00"), true);

        Assertions.assertEquals(0, productRepository.count());

        productRepository.saveAndFlush(ProductJpaEntity.from(product));

        Assertions.assertEquals(1, productRepository.count());

        final var updatedProduct = product.clone().update(
            expectedName,
            expectedRate,
            expectedIsActive
        );

        final var productId = product.getId();
        final var actualProduct = productMySQLGateway.update(updatedProduct);

        Assertions.assertEquals(1, productRepository.count());

        Assertions.assertEquals(productId, actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.getName());
        Assertions.assertEquals(expectedRate, actualProduct.getRate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertEquals(product.getCreatedAt(), actualProduct.getCreatedAt());
        Assertions.assertTrue(product.getUpdatedAt().isBefore(actualProduct.getUpdatedAt()));
        Assertions.assertNull(actualProduct.getDeletedAt());

        final var persistedProduct = productRepository.findById(productId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedProduct.getName());
        Assertions.assertEquals(expectedRate, persistedProduct.getRate());
        Assertions.assertEquals(expectedIsActive, persistedProduct.getActive());
        Assertions.assertEquals(product.getCreatedAt(), persistedProduct.getCreatedAt());
        Assertions.assertEquals(actualProduct.getUpdatedAt(), persistedProduct.getUpdatedAt());
        Assertions.assertTrue(product.getUpdatedAt().isBefore(persistedProduct.getUpdatedAt()));
        Assertions.assertNull(persistedProduct.getDeletedAt());
    }

    @Test
    public void givenAPersistedProduct_whenCallsDeleteById_shouldDeleteProduct() {
        final var expectedName = "CDB 300%";
        final var expectedRate = new BigDecimal("300.00");
        final var expectedIsActive = true;

        final var product = Product.createProduct(expectedName, expectedRate, expectedIsActive);

        Assertions.assertEquals(0, productRepository.count());

        productRepository.saveAndFlush(ProductJpaEntity.from(product));

        Assertions.assertEquals(1, productRepository.count());

        final var productId = product.getId();

        productMySQLGateway.deleteById(productId);

        Assertions.assertEquals(0, productRepository.count());
        Assertions.assertTrue(productRepository.findById(productId.getValue()).isEmpty());
    }

    @Test
    public void givenAInvalidProduct_whenCallsDeleteById_shouldDoNothing() {
        Assertions.assertEquals(0, productRepository.count());

        productMySQLGateway.deleteById(ProductID.from("invalid"));

        Assertions.assertEquals(0, productRepository.count());
    }

    @Test
    public void givenAValidProduct_whenCallsFindById_shouldReturnProduct() {
        final var expectedName = "CDB 300%";
        final var expectedRate = new BigDecimal("300.00");
        final var expectedIsActive = true;

        final var product = Product.createProduct(expectedName, expectedRate, expectedIsActive);

        Assertions.assertEquals(0, productRepository.count());

        productRepository.saveAndFlush(ProductJpaEntity.from(product));

        Assertions.assertEquals(1, productRepository.count());

        final var productId = product.getId();
        final var actualProduct = productMySQLGateway.findById(productId).get();

        Assertions.assertEquals(1, productRepository.count());

        Assertions.assertEquals(productId, actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.getName());
        Assertions.assertEquals(expectedRate, actualProduct.getRate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertEquals(product.getCreatedAt(), actualProduct.getCreatedAt());
        Assertions.assertEquals(product.getUpdatedAt(), actualProduct.getUpdatedAt());
        Assertions.assertEquals(product.getDeletedAt(), actualProduct.getDeletedAt());
        Assertions.assertNull(actualProduct.getDeletedAt());
    }

    @Test
    public void givenAInvalidProduct_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, productRepository.count());

        final var findResult = productMySQLGateway.findById(ProductID.from("invalid"));

        Assertions.assertTrue(findResult.isEmpty());
    }

    @Test
    public void givenPrePersistedProducts_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var product1 = Product.createProduct("CDB 101", new BigDecimal("101.00"), true);
        final var product2 = Product.createProduct("CDB 108", new BigDecimal("108.00"), true);
        final var product3 = Product.createProduct("Poupar Automático", new BigDecimal("105.00"), true);

        Assertions.assertEquals(0, productRepository.count());

        productRepository.saveAll(List.of(
                ProductJpaEntity.from(product1),
                ProductJpaEntity.from(product2),
                ProductJpaEntity.from(product3)
        ));

        Assertions.assertEquals(3, productRepository.count());

        final var query = new ProductSearchQuery(0, 1, "", "name", "asc");
        final var actualResult = productMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(product1.getId(), actualResult.items().getFirst().getId());
    }

    @Test
    public void givenEmptyProductsTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, productRepository.count());

        final var query = new ProductSearchQuery(0, 1, "", "name", "asc");
        final var actualResult = productMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(0, actualResult.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var product1 = Product.createProduct("CDB 101", new BigDecimal("101.00"), true);
        final var product2 = Product.createProduct("CDB 108", new BigDecimal("108.00"), true);
        final var product3 = Product.createProduct("Poupar Automático", new BigDecimal("105.00"), true);

        Assertions.assertEquals(0, productRepository.count());

        productRepository.saveAll(List.of(
                ProductJpaEntity.from(product1),
                ProductJpaEntity.from(product2),
                ProductJpaEntity.from(product3)
        ));

        Assertions.assertEquals(3, productRepository.count());

        var query = new ProductSearchQuery(0, 1, "", "name", "asc");
        var actualResult = productMySQLGateway.findAll(query);

        // Page 0
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(product1.getId(), actualResult.items().getFirst().getId());

        // Page 1
        expectedPage = 1;

        query = new ProductSearchQuery(1, 1, "", "name", "asc");
        actualResult = productMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(product2.getId(), actualResult.items().getFirst().getId());

        // Page 2
        expectedPage = 2;

        query = new ProductSearchQuery(2, 1, "", "name", "asc");
        actualResult = productMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(product3.getId(), actualResult.items().getFirst().getId());
    }

    @Test
    public void givenPrePersistedProductsAndSomeTerm_whenCallsFindAllAndTermsMatchsProductName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var product1 = Product.createProduct("CDB 101", new BigDecimal("101.00"), true);
        final var product2 = Product.createProduct("CDB 108", new BigDecimal("108.00"), true);
        final var product3 = Product.createProduct("Poupar Automático", new BigDecimal("105.00"), true);

        Assertions.assertEquals(0, productRepository.count());

        productRepository.saveAll(List.of(
                ProductJpaEntity.from(product1),
                ProductJpaEntity.from(product2),
                ProductJpaEntity.from(product3)
        ));

        Assertions.assertEquals(3, productRepository.count());

        final var query = new ProductSearchQuery(0, 1, "Poupar", "name", "asc");
        final var actualResult = productMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(product3.getId(), actualResult.items().getFirst().getId());
    }
}

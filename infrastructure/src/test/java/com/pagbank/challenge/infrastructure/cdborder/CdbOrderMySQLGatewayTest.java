package com.pagbank.challenge.infrastructure.cdborder;

import com.pagbank.challenge.MySQLGatewayTest;
import com.pagbank.challenge.domain.cdborder.CdbOrder;
import com.pagbank.challenge.domain.cdborder.CdbOrderID;
import com.pagbank.challenge.domain.cdborder.CdbOrderSearchQuery;
import com.pagbank.challenge.domain.cdborder.CdbOrderTransactionType;
import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.infrastructure.cdborder.persistence.CdbOrderJpaEntity;
import com.pagbank.challenge.infrastructure.cdborder.persistence.CdbOrderRepository;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerRepository;
import com.pagbank.challenge.infrastructure.product.persistence.ProductJpaEntity;
import com.pagbank.challenge.infrastructure.product.persistence.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

@MySQLGatewayTest
public class CdbOrderMySQLGatewayTest {

    @Autowired
    private CdbOrderMySQLGateway cdbOrderMySQLGateway;

    @Autowired
    private CdbOrderRepository cdbOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void givenAValidOrder_whenCallsCreate_shouldReturnANewOrder() {
        final var expectedAmount = new BigDecimal("200.00");
        final var expectectedOrderType = CdbOrderTransactionType.PURCHASE;

        final var customer = Customer.registerCustomer("José", 35, "masculino", "11 969588885", "São Paulo", "São Paulo", "Brasil", "Rua bolinha", "444", "88888");
        final var product = Product.createProduct("CDB 135%", new BigDecimal("125"), true);
        final var order = CdbOrder.createOrder(customer.getId(), product.getId(), expectedAmount, expectectedOrderType);

        customerRepository.saveAndFlush(CustomerJpaEntity.from(customer));
        productRepository.saveAndFlush(ProductJpaEntity.from(product));

        Assertions.assertEquals(0, cdbOrderRepository.count());

        final var orderID = order.getId();
        final var actualOrder = cdbOrderMySQLGateway.create(order);

        Assertions.assertEquals(1, cdbOrderRepository.count());
        Assertions.assertEquals(orderID, actualOrder.getId());
        Assertions.assertEquals(order.getCustomerId(), actualOrder.getCustomerId());
        Assertions.assertEquals(order.getProductId(), actualOrder.getProductId());
        Assertions.assertEquals(expectedAmount, actualOrder.getAmount());
        Assertions.assertEquals(expectectedOrderType, actualOrder.getTransactionType());
        Assertions.assertEquals(order.getTransactionDate(), actualOrder.getTransactionDate());
        Assertions.assertNotNull(actualOrder.getTransactionDate());

        final var persistedProduct = cdbOrderRepository.findById(orderID.getValue()).get();

        Assertions.assertEquals(orderID.getValue(), persistedProduct.getId());
        Assertions.assertEquals(order.getCustomerId().getValue(), persistedProduct.getCustomer().getId());
        Assertions.assertEquals(order.getProductId().getValue(), persistedProduct.getProduct().getId());
        Assertions.assertEquals(expectedAmount, persistedProduct.getAmount());
        Assertions.assertEquals(expectectedOrderType, persistedProduct.getTransactionType());
        Assertions.assertEquals(order.getTransactionDate(), persistedProduct.getTransactionDate());
        Assertions.assertNotNull(actualOrder.getTransactionDate());
    }

    @Test
    public void givenAPersistedOrder_whenCallsDeleteById_shouldDeleteOrder() {
        final var expectedAmount = new BigDecimal("200.00");
        final var expectectedOrderType = CdbOrderTransactionType.PURCHASE;

        final var customer = Customer.registerCustomer("José", 35, "masculino", "11 969588885", "São Paulo", "São Paulo", "Brasil", "Rua bolinha", "444", "88888");
        final var product = Product.createProduct("CDB 135%", new BigDecimal("125"), true);
        final var order = CdbOrder.createOrder(customer.getId(), product.getId(), expectedAmount, expectectedOrderType);

        Assertions.assertEquals(0, cdbOrderRepository.count());

        final var customerEntity = customerRepository.saveAndFlush(CustomerJpaEntity.from(customer));
        final var productEntity = productRepository.saveAndFlush(ProductJpaEntity.from(product));

        cdbOrderRepository.saveAndFlush(CdbOrderJpaEntity.from(order, customerEntity, productEntity));

        Assertions.assertEquals(1, cdbOrderRepository.count());

        final var orderID = order.getId();

        cdbOrderMySQLGateway.deleteById(orderID);

        Assertions.assertEquals(0, cdbOrderRepository.count());
        Assertions.assertTrue(cdbOrderRepository.findById(orderID.getValue()).isEmpty());
    }

    @Test
    public void givenAInvalidOrder_whenCallsDeleteById_shouldDoNothing() {
        Assertions.assertEquals(0, cdbOrderRepository.count());

        cdbOrderMySQLGateway.deleteById(CdbOrderID.from("invalid"));

        Assertions.assertEquals(0, cdbOrderRepository.count());
    }

    @Test
    public void givenAValidOrder_whenCallsFindById_shouldReturnOrder() {
        final var expectedAmount = new BigDecimal("200.00");
        final var expectectedOrderType = CdbOrderTransactionType.PURCHASE;

        final var customer = Customer.registerCustomer("José", 35, "masculino", "11 969588885", "São Paulo", "São Paulo", "Brasil", "Rua bolinha", "444", "88888");
        final var product = Product.createProduct("CDB 135%", new BigDecimal("125"), true);
        final var order = CdbOrder.createOrder(customer.getId(), product.getId(), expectedAmount, expectectedOrderType);

        final var customerEntity = customerRepository.saveAndFlush(CustomerJpaEntity.from(customer));
        final var productEntity = productRepository.saveAndFlush(ProductJpaEntity.from(product));

        Assertions.assertEquals(0, cdbOrderRepository.count());

        cdbOrderRepository.saveAndFlush(CdbOrderJpaEntity.from(order, customerEntity, productEntity));

        Assertions.assertEquals(1, cdbOrderRepository.count());

        final var orderID = order.getId();
        final var actualOrder = cdbOrderMySQLGateway.findById(orderID).get();

        Assertions.assertEquals(1, cdbOrderRepository.count());

        Assertions.assertEquals(orderID, actualOrder.getId());
        Assertions.assertEquals(order.getCustomerId(), actualOrder.getCustomerId());
        Assertions.assertEquals(order.getProductId(), actualOrder.getProductId());
        Assertions.assertEquals(expectedAmount, actualOrder.getAmount());
        Assertions.assertEquals(expectectedOrderType, actualOrder.getTransactionType());
        Assertions.assertEquals(order.getTransactionDate(), actualOrder.getTransactionDate());
        Assertions.assertNotNull(actualOrder.getTransactionDate());
    }

    @Test
    public void givenAInvalidOrder_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, cdbOrderRepository.count());

        final var findResult = cdbOrderMySQLGateway.findById(CdbOrderID.from("invalid"));

        Assertions.assertTrue(findResult.isEmpty());
    }

    @Test
    public void givenPrePersistedOrders_whenCallsFindAllOrders_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var customer1 = Customer.registerCustomer("José", 35, "masculino", "11 969588885", "São Paulo", "São Paulo", "Brasil", "Rua bolinha", "444", "88888");
        final var customer2 = Customer.registerCustomer("Jhon", 28, "masculino", "47 977484999", "Joinville", "Santa Catarina", "Brasil", "Rua bolinha", "566", "8965999");
        final var product1 = Product.createProduct("CDB 135%", new BigDecimal("125"), true);
        final var product2 = Product.createProduct("CDB 200%", new BigDecimal("200"), true);

        final var order1 = CdbOrder.createOrder(customer1.getId(), product2.getId(), new BigDecimal("101.00"), CdbOrderTransactionType.PURCHASE);
        final var order2 = CdbOrder.createOrder(customer2.getId(), product2.getId(), new BigDecimal("255.00"), CdbOrderTransactionType.PURCHASE);
        final var order3 = CdbOrder.createOrder(customer1.getId(), product1.getId(), new BigDecimal("1652.32"), CdbOrderTransactionType.SELL);

        customerRepository.saveAllAndFlush(List.of(
                CustomerJpaEntity.from(customer1),
                CustomerJpaEntity.from(customer2)
        ));
        productRepository.saveAllAndFlush(List.of(
                ProductJpaEntity.from(product1),
                ProductJpaEntity.from(product2)
        ));

        Assertions.assertEquals(0, cdbOrderRepository.count());

        cdbOrderRepository.saveAll(List.of(
                CdbOrderJpaEntity.from(order1, CustomerJpaEntity.from(customer1), ProductJpaEntity.from(product2)),
                CdbOrderJpaEntity.from(order2, CustomerJpaEntity.from(customer2), ProductJpaEntity.from(product2)),
                CdbOrderJpaEntity.from(order3, CustomerJpaEntity.from(customer1), ProductJpaEntity.from(product1))
        ));

        Assertions.assertEquals(3, cdbOrderRepository.count());

        final var query = new CdbOrderSearchQuery(0, 1, "", "", "");
        final var actualResult = cdbOrderMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(order3.getId().getValue(), actualResult.items().getFirst().orderId());
    }

    @Test
    public void givenEmptyCdbOrdersTable_whenCallsFindAllOrders_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, cdbOrderRepository.count());

        final var query = new CdbOrderSearchQuery(0, 1, "", "", "");
        final var actualResult = cdbOrderMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(0, actualResult.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllOrdersWithPage1_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var customer1 = Customer.registerCustomer("José", 35, "masculino", "11 969588885", "São Paulo", "São Paulo", "Brasil", "Rua bolinha", "444", "88888");
        final var customer2 = Customer.registerCustomer("Jhon", 28, "masculino", "47 977484999", "Joinville", "Santa Catarina", "Brasil", "Rua bolinha", "566", "8965999");
        final var product1 = Product.createProduct("CDB 135%", new BigDecimal("125"), true);
        final var product2 = Product.createProduct("CDB 200%", new BigDecimal("200"), true);

        final var order1 = CdbOrder.createOrder(customer1.getId(), product1.getId(), new BigDecimal("101.00"), CdbOrderTransactionType.PURCHASE);
        final var order2 = CdbOrder.createOrder(customer2.getId(), product1.getId(), new BigDecimal("255.00"), CdbOrderTransactionType.PURCHASE);
        final var order3 = CdbOrder.createOrder(customer1.getId(), product2.getId(), new BigDecimal("1652.32"), CdbOrderTransactionType.SELL);

        customerRepository.saveAllAndFlush(List.of(
                CustomerJpaEntity.from(customer1),
                CustomerJpaEntity.from(customer2)
        ));
        productRepository.saveAllAndFlush(List.of(
                ProductJpaEntity.from(product1),
                ProductJpaEntity.from(product2)
        ));

        Assertions.assertEquals(0, cdbOrderRepository.count());

        cdbOrderRepository.saveAll(List.of(
                CdbOrderJpaEntity.from(order1, CustomerJpaEntity.from(customer1), ProductJpaEntity.from(product1)),
                CdbOrderJpaEntity.from(order2, CustomerJpaEntity.from(customer2), ProductJpaEntity.from(product1)),
                CdbOrderJpaEntity.from(order3, CustomerJpaEntity.from(customer1), ProductJpaEntity.from(product2))
        ));

        Assertions.assertEquals(3, cdbOrderRepository.count());

        var query = new CdbOrderSearchQuery(0, 1, "", "", "");
        var actualResult = cdbOrderMySQLGateway.findAll(query);

        // Page 0
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(order2.getId().getValue(), actualResult.items().getFirst().orderId());

        // Page 1
        expectedPage = 1;

        query = new CdbOrderSearchQuery(1, 1, "", "", "");
        actualResult = cdbOrderMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(order1.getId().getValue(), actualResult.items().getFirst().orderId());

        // Page 2
        expectedPage = 2;

        query = new CdbOrderSearchQuery(2, 1, "", "", "");
        actualResult = cdbOrderMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(order3.getId().getValue(), actualResult.items().getFirst().orderId());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllOrdersBySpecificCustomer_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 2;

        final var customer1 = Customer.registerCustomer("José", 35, "masculino", "11 969588885", "São Paulo", "São Paulo", "Brasil", "Rua bolinha", "444", "88888");
        final var customer2 = Customer.registerCustomer("Jhon", 28, "masculino", "47 977484999", "Joinville", "Santa Catarina", "Brasil", "Rua bolinha", "566", "8965999");
        final var product1 = Product.createProduct("CDB 135%", new BigDecimal("125"), true);
        final var product2 = Product.createProduct("CDB 200%", new BigDecimal("200"), true);

        final var order1 = CdbOrder.createOrder(customer1.getId(), product1.getId(), new BigDecimal("101.00"), CdbOrderTransactionType.PURCHASE);
        final var order2 = CdbOrder.createOrder(customer2.getId(), product1.getId(), new BigDecimal("255.00"), CdbOrderTransactionType.PURCHASE);
        final var order3 = CdbOrder.createOrder(customer1.getId(), product2.getId(), new BigDecimal("1652.32"), CdbOrderTransactionType.SELL);

        customerRepository.saveAllAndFlush(List.of(
                CustomerJpaEntity.from(customer1),
                CustomerJpaEntity.from(customer2)
        ));
        productRepository.saveAllAndFlush(List.of(
                ProductJpaEntity.from(product1),
                ProductJpaEntity.from(product2)
        ));

        Assertions.assertEquals(0, cdbOrderRepository.count());

        cdbOrderRepository.saveAll(List.of(
                CdbOrderJpaEntity.from(order1, CustomerJpaEntity.from(customer1), ProductJpaEntity.from(product1)),
                CdbOrderJpaEntity.from(order2, CustomerJpaEntity.from(customer2), ProductJpaEntity.from(product1)),
                CdbOrderJpaEntity.from(order3, CustomerJpaEntity.from(customer1), ProductJpaEntity.from(product2))
        ));

        Assertions.assertEquals(3, cdbOrderRepository.count());

        var query = new CdbOrderSearchQuery(0, 10, "", customer1.getId().getValue(), "");
        var actualResult = cdbOrderMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedTotal, actualResult.items().size());
        Assertions.assertEquals(order1.getId().getValue(), actualResult.items().getFirst().orderId());
        Assertions.assertEquals(order1.getCustomerId().getValue(), actualResult.items().getFirst().customerId());
        Assertions.assertEquals(order3.getId().getValue(), actualResult.items().getLast().orderId());
        Assertions.assertEquals(order3.getCustomerId().getValue(), actualResult.items().getLast().customerId());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllOrdersBySpecificProduct_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 1;

        final var customer1 = Customer.registerCustomer("José", 35, "masculino", "11 969588885", "São Paulo", "São Paulo", "Brasil", "Rua bolinha", "444", "88888");
        final var customer2 = Customer.registerCustomer("Jhon", 28, "masculino", "47 977484999", "Joinville", "Santa Catarina", "Brasil", "Rua bolinha", "566", "8965999");
        final var product1 = Product.createProduct("CDB 135%", new BigDecimal("125"), true);
        final var product2 = Product.createProduct("CDB 200%", new BigDecimal("200"), true);

        final var order1 = CdbOrder.createOrder(customer1.getId(), product1.getId(), new BigDecimal("101.00"), CdbOrderTransactionType.PURCHASE);
        final var order2 = CdbOrder.createOrder(customer2.getId(), product1.getId(), new BigDecimal("255.00"), CdbOrderTransactionType.PURCHASE);
        final var order3 = CdbOrder.createOrder(customer1.getId(), product2.getId(), new BigDecimal("1652.32"), CdbOrderTransactionType.SELL);

        customerRepository.saveAllAndFlush(List.of(
                CustomerJpaEntity.from(customer1),
                CustomerJpaEntity.from(customer2)
        ));
        productRepository.saveAllAndFlush(List.of(
                ProductJpaEntity.from(product1),
                ProductJpaEntity.from(product2)
        ));

        Assertions.assertEquals(0, cdbOrderRepository.count());

        cdbOrderRepository.saveAll(List.of(
                CdbOrderJpaEntity.from(order1, CustomerJpaEntity.from(customer1), ProductJpaEntity.from(product1)),
                CdbOrderJpaEntity.from(order2, CustomerJpaEntity.from(customer2), ProductJpaEntity.from(product1)),
                CdbOrderJpaEntity.from(order3, CustomerJpaEntity.from(customer1), ProductJpaEntity.from(product2))
        ));

        Assertions.assertEquals(3, cdbOrderRepository.count());

        var query = new CdbOrderSearchQuery(0, 10, "", "", product2.getId().getValue());
        var actualResult = cdbOrderMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedTotal, actualResult.items().size());
        Assertions.assertEquals(order3.getId().getValue(), actualResult.items().getFirst().orderId());
        Assertions.assertEquals(order3.getCustomerId().getValue(), actualResult.items().getFirst().customerId());
        Assertions.assertEquals(order3.getProductId().getValue(), actualResult.items().getFirst().productId());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllOrdersBySpecificCustomerAndProduct_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 2;

        final var customer1 = Customer.registerCustomer("José", 35, "masculino", "11 969588885", "São Paulo", "São Paulo", "Brasil", "Rua bolinha", "444", "88888");
        final var customer2 = Customer.registerCustomer("Jhon", 28, "masculino", "47 977484999", "Joinville", "Santa Catarina", "Brasil", "Rua bolinha", "566", "8965999");
        final var product1 = Product.createProduct("CDB 135%", new BigDecimal("125"), true);
        final var product2 = Product.createProduct("CDB 200%", new BigDecimal("200"), true);
        final var product3 = Product.createProduct("CDB 125%", new BigDecimal("125"), true);

        final var order1 = CdbOrder.createOrder(customer1.getId(), product1.getId(), new BigDecimal("101.00"), CdbOrderTransactionType.PURCHASE);
        final var order2 = CdbOrder.createOrder(customer2.getId(), product1.getId(), new BigDecimal("255.00"), CdbOrderTransactionType.PURCHASE);
        final var order3 = CdbOrder.createOrder(customer1.getId(), product2.getId(), new BigDecimal("1652.32"), CdbOrderTransactionType.SELL);
        final var order4 = CdbOrder.createOrder(customer2.getId(), product1.getId(), new BigDecimal("1365.00"), CdbOrderTransactionType.PURCHASE);
        final var order5 = CdbOrder.createOrder(customer2.getId(), product3.getId(), new BigDecimal("350.00"), CdbOrderTransactionType.PURCHASE);

        customerRepository.saveAllAndFlush(List.of(
                CustomerJpaEntity.from(customer1),
                CustomerJpaEntity.from(customer2)
        ));
        productRepository.saveAllAndFlush(List.of(
                ProductJpaEntity.from(product1),
                ProductJpaEntity.from(product2),
                ProductJpaEntity.from(product3)
        ));

        Assertions.assertEquals(0, cdbOrderRepository.count());

        cdbOrderRepository.saveAll(List.of(
                CdbOrderJpaEntity.from(order1, CustomerJpaEntity.from(customer1), ProductJpaEntity.from(product1)),
                CdbOrderJpaEntity.from(order2, CustomerJpaEntity.from(customer2), ProductJpaEntity.from(product1)),
                CdbOrderJpaEntity.from(order3, CustomerJpaEntity.from(customer1), ProductJpaEntity.from(product2)),
                CdbOrderJpaEntity.from(order4, CustomerJpaEntity.from(customer2), ProductJpaEntity.from(product1)),
                CdbOrderJpaEntity.from(order5, CustomerJpaEntity.from(customer2), ProductJpaEntity.from(product3))
        ));

        Assertions.assertEquals(5, cdbOrderRepository.count());

        var query = new CdbOrderSearchQuery(0, 10, "", customer2.getId().getValue(), product1.getId().getValue());
        var actualResult = cdbOrderMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedTotal, actualResult.items().size());
        Assertions.assertEquals(order2.getId().getValue(), actualResult.items().getFirst().orderId());
        Assertions.assertEquals(order2.getCustomerId().getValue(), actualResult.items().getFirst().customerId());
        Assertions.assertEquals(order2.getProductId().getValue(), actualResult.items().getFirst().productId());
        Assertions.assertEquals(order2.getAmount(), actualResult.items().getFirst().amount());
        Assertions.assertEquals(order4.getId().getValue(), actualResult.items().getLast().orderId());
        Assertions.assertEquals(order4.getCustomerId().getValue(), actualResult.items().getLast().customerId());
        Assertions.assertEquals(order4.getProductId().getValue(), actualResult.items().getFirst().productId());
        Assertions.assertEquals(order4.getAmount(), actualResult.items().getLast().amount());
    }
}

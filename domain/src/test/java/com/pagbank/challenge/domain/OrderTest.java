package com.pagbank.challenge.domain;

import com.pagbank.challenge.domain.order.Order;
import com.pagbank.challenge.domain.order.OrderTransactionType;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.validation.handler.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class OrderTest {
    @Test
    public void givenAValidParams_whenCallNewBuyOrder_thenInstantiateAOrder() {
        final var expectedCustomer = CustomerID.unique();
        final var expectedProduct = ProductID.unique();
        final var expectedAmount = new BigDecimal("1045.35");
        final var expectedTransactionType = OrderTransactionType.BUY;

        final var order = Order.createBuyOrder(
                expectedCustomer,
                expectedProduct,
                expectedAmount
        );

        Assertions.assertNotNull(order);
        Assertions.assertEquals(expectedCustomer, order.getCustomerId());
        Assertions.assertEquals(expectedProduct, order.getProductId());
        Assertions.assertEquals(expectedAmount, order.getAmount());
        Assertions.assertNotNull(order.getTransactionDate());
        Assertions.assertEquals(expectedTransactionType, order.getTransactionType());
    }

    @Test
    public void givenAValidParams_whenCallNewSellOrder_thenInstantiateAOrder() {
        final var expectedCustomer = CustomerID.unique();
        final var expectedProduct = ProductID.unique();
        final var amount = new BigDecimal("1045.35");
        final var transactionType = OrderTransactionType.SELL;

        final var order = Order.createSellOrder(
                expectedCustomer,
                expectedProduct,
                amount
        );

        Assertions.assertNotNull(order);
        Assertions.assertEquals(expectedCustomer, order.getCustomerId());
        Assertions.assertEquals(expectedProduct, order.getProductId());
        Assertions.assertEquals(amount, order.getAmount());
        Assertions.assertNotNull(order.getTransactionDate());
        Assertions.assertEquals(transactionType, order.getTransactionType());
    }

    @Test
    public void givenAInvalidNullCustomerId_whenCallCreateBuyOrder_thenShouldReceiveError() {
        final CustomerID expectedCustomer = null;
        final var expectedProduct = ProductID.unique();
        final var amount = new BigDecimal("1045.35");
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'customerId' should not be null";

        final var order = Order.createBuyOrder(
                expectedCustomer,
                expectedProduct,
                amount
        );

        Assertions.assertNotNull(order);

        final var notification = Notification.create();

        order.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    @Test
    public void givenAInvalidNullCustomerId_whenCallCreateSellOrder_thenShouldReceiveError() {
        final CustomerID expectedCustomer = null;
        final var expectedProduct = ProductID.unique();
        final var amount = new BigDecimal("1045.35");
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'customerId' should not be null";

        final var order = Order.createSellOrder(
                expectedCustomer,
                expectedProduct,
                amount
        );

        Assertions.assertNotNull(order);

        final var notification = Notification.create();

        order.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    @Test
    public void givenAInvalidNullProductId_whenCallCreateBuyOrder_thenShouldReceiveError() {
        final var expectedCustomer = CustomerID.unique();
        final ProductID expectedProduct = null;
        final var amount = new BigDecimal("1045.35");
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'productId' should not be null";

        final var order = Order.createBuyOrder(
                expectedCustomer,
                expectedProduct,
                amount
        );

        Assertions.assertNotNull(order);

        final var notification = Notification.create();

        order.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    @Test
    public void givenAInvalidNullProductId_whenCallCreateSellOrder_thenShouldReceiveError() {
        final var expectedCustomer = CustomerID.unique();
        final ProductID expectedProduct = null;
        final var amount = new BigDecimal("1045.35");
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'productId' should not be null";

        final var order = Order.createSellOrder(
                expectedCustomer,
                expectedProduct,
                amount
        );

        Assertions.assertNotNull(order);

        final var notification = Notification.create();

        order.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    @Test
    public void givenAInvalidNullNegativeAmount_whenCallCreateBuyOrder_thenShouldReceiveError() {
        final var expectedCustomer = CustomerID.unique();
        final var expectedProduct = ProductID.unique();
        final var amount = new BigDecimal("-1");
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'amount' must be greather or equal than zero!";

        final var order = Order.createSellOrder(
                expectedCustomer,
                expectedProduct,
                amount
        );

        Assertions.assertNotNull(order);

        final var notification = Notification.create();

        order.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }
}

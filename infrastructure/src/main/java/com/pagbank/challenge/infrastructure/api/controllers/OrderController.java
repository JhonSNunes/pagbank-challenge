package com.pagbank.challenge.infrastructure.api.controllers;

import com.pagbank.challenge.application.order.OrderOutput;
import com.pagbank.challenge.application.order.delete.DeleteOrderUseCase;
import com.pagbank.challenge.application.order.purchase.PurchaseOrderCommand;
import com.pagbank.challenge.application.order.purchase.PurchaseOrderUseCase;
import com.pagbank.challenge.application.order.retrieve.get.GetOrderByIdUseCase;
import com.pagbank.challenge.application.order.retrieve.list.ListOrderUseCase;
import com.pagbank.challenge.application.order.sell.SellOrderCommand;
import com.pagbank.challenge.application.order.sell.SellOrderUseCase;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.order.OrderSearchQuery;
import com.pagbank.challenge.domain.order.OrderTransactionType;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.validation.handler.Notification;
import com.pagbank.challenge.infrastructure.api.OrderAPI;
import com.pagbank.challenge.infrastructure.order.models.OrderRequest;
import com.pagbank.challenge.infrastructure.order.models.OrderResponse;
import com.pagbank.challenge.infrastructure.order.presenters.OrderApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class OrderController implements OrderAPI {

    private PurchaseOrderUseCase purchaseOrderUseCase;
    private SellOrderUseCase sellOrderUseCase;
    private ListOrderUseCase listOrderUseCase;
    private GetOrderByIdUseCase getOrderByIdUseCase;
    private DeleteOrderUseCase deleteOrderUseCase;

    public OrderController(
            final PurchaseOrderUseCase purchaseOrderUseCase,
            final SellOrderUseCase sellOrderUseCase,
            final ListOrderUseCase listOrderUseCase,
            final GetOrderByIdUseCase getOrderByIdUseCase,
            final DeleteOrderUseCase deleteOrderUseCase
    ) {
        this.purchaseOrderUseCase = Objects.requireNonNull(purchaseOrderUseCase);
        this.sellOrderUseCase = Objects.requireNonNull(sellOrderUseCase);
        this.listOrderUseCase = Objects.requireNonNull(listOrderUseCase);
        this.getOrderByIdUseCase = Objects.requireNonNull(getOrderByIdUseCase);
        this.deleteOrderUseCase = Objects.requireNonNull(deleteOrderUseCase);
    }

    @Override
    public ResponseEntity<?> createOrder(final OrderRequest input) {


        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<OrderOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/orders/" + output.id())).body(output);

        if (input.transactionType().equals(OrderTransactionType.PURCHASE)) {
            final var command = PurchaseOrderCommand.with(
                    CustomerID.from(input.customerId()),
                    ProductID.from(input.productId()),
                    input.amount()
            );
            return purchaseOrderUseCase.execute(command).fold(onError, onSuccess);
        }
        final var command = SellOrderCommand.with(
                CustomerID.from(input.customerId()),
                ProductID.from(input.productId()),
                input.amount()
        );

        return sellOrderUseCase.execute(command).fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listOrders(
            String customerId,
            String productId,
            String search,
            int page,
            int perPage,
            String sort,
            String direction
    ) {
        CustomerID actualCustomerId = null;
        ProductID actualProductId = null;

        if (customerId != null) {
            actualCustomerId = CustomerID.from(customerId);
        }

        if (productId != null) {
            actualProductId = ProductID.from(productId);
        }

        return this.listOrderUseCase.execute(new OrderSearchQuery(
                page,
                perPage,
                search,
                sort,
                direction,
                actualCustomerId,
                actualProductId
        ));
    }

    @Override
    public OrderResponse getById(final String id) {
        return OrderApiPresenter.present(this.getOrderByIdUseCase.execute(id));
    }

    @Override
    public void deleteById(String id) {
        this.deleteOrderUseCase.execute(id);
    }
}

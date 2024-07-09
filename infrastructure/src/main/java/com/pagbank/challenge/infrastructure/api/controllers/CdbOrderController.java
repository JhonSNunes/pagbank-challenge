package com.pagbank.challenge.infrastructure.api.controllers;

import com.pagbank.challenge.application.cdborder.CdbOrderOutput;
import com.pagbank.challenge.application.cdborder.create.CreateCdbOrderCommand;
import com.pagbank.challenge.application.cdborder.create.CreateCdbOrderUseCase;
import com.pagbank.challenge.application.cdborder.delete.DeleteOrderUseCase;
import com.pagbank.challenge.application.cdborder.retrieve.get.GetOrderByIdUseCase;
import com.pagbank.challenge.application.cdborder.retrieve.list.ListOrderUseCase;
import com.pagbank.challenge.domain.cdborder.CdbOrderSearchQuery;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.validation.handler.Notification;
import com.pagbank.challenge.infrastructure.api.CdbOrderAPI;
import com.pagbank.challenge.infrastructure.cdborder.models.CdbOrderRequest;
import com.pagbank.challenge.infrastructure.cdborder.models.CdbOrderResponse;
import com.pagbank.challenge.infrastructure.cdborder.presenters.OrderApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CdbOrderController implements CdbOrderAPI {

    private CreateCdbOrderUseCase createCdbOrderUseCase;
    private ListOrderUseCase listOrderUseCase;
    private GetOrderByIdUseCase getOrderByIdUseCase;
    private DeleteOrderUseCase deleteOrderUseCase;

    public CdbOrderController(
            final CreateCdbOrderUseCase createCdbOrderUseCase,
            final ListOrderUseCase listOrderUseCase,
            final GetOrderByIdUseCase getOrderByIdUseCase,
            final DeleteOrderUseCase deleteOrderUseCase
    ) {
        this.createCdbOrderUseCase = Objects.requireNonNull(createCdbOrderUseCase);
        this.listOrderUseCase = Objects.requireNonNull(listOrderUseCase);
        this.getOrderByIdUseCase = Objects.requireNonNull(getOrderByIdUseCase);
        this.deleteOrderUseCase = Objects.requireNonNull(deleteOrderUseCase);
    }

    @Override
    public ResponseEntity<?> createOrder(final CdbOrderRequest input) {
        final var command = CreateCdbOrderCommand.with(
            CustomerID.from(input.customerId()),
            ProductID.from(input.productId()),
            input.amount(),
            input.transactionType()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CdbOrderOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/orders/" + output.id())).body(output);

        return createCdbOrderUseCase.execute(command).fold(onError, onSuccess);
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

        return this.listOrderUseCase.execute(new CdbOrderSearchQuery(
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
    public CdbOrderResponse getById(final String id) {
        return OrderApiPresenter.present(this.getOrderByIdUseCase.execute(id));
    }

    @Override
    public void deleteById(String id) {
        this.deleteOrderUseCase.execute(id);
    }
}

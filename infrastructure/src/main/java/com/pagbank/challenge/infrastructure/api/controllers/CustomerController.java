package com.pagbank.challenge.infrastructure.api.controllers;

import com.pagbank.challenge.application.customer.create.CreateCustomerCommand;
import com.pagbank.challenge.application.customer.create.CreateCustomerOutput;
import com.pagbank.challenge.application.customer.create.CreateCustomerUseCase;
import com.pagbank.challenge.application.customer.delete.DeleteCustomerUseCase;
import com.pagbank.challenge.application.customer.retrieve.get.GetCustomerByIdUseCase;
import com.pagbank.challenge.application.customer.retrieve.list.ListCustomerUseCase;
import com.pagbank.challenge.application.customer.update.UpdateCustomerCommand;
import com.pagbank.challenge.application.customer.update.UpdateCustomerOutput;
import com.pagbank.challenge.application.customer.update.UpdateCustomerUseCase;
import com.pagbank.challenge.domain.customer.CustomerSearchQuery;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.validation.handler.Notification;
import com.pagbank.challenge.infrastructure.api.CustomerAPI;
import com.pagbank.challenge.infrastructure.customer.models.CustomerRequest;
import com.pagbank.challenge.infrastructure.customer.models.CustomerResponse;
import com.pagbank.challenge.infrastructure.customer.presenters.CustomerApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CustomerController implements CustomerAPI {

    private CreateCustomerUseCase createCustomerUseCase;
    private GetCustomerByIdUseCase getCustomerByIdUseCase;
    private ListCustomerUseCase listCustomerUseCase;
    private UpdateCustomerUseCase updateCustomerUseCase;
    private DeleteCustomerUseCase deleteCustomerUseCase;

    public CustomerController(
            final CreateCustomerUseCase createCustomerUseCase,
            final GetCustomerByIdUseCase getCustomerByIdUseCase,
            final ListCustomerUseCase listCustomerUseCase,
            final UpdateCustomerUseCase updateCustomerUseCase,
            final DeleteCustomerUseCase deleteCustomerUseCase
    ) {
        this.createCustomerUseCase = Objects.requireNonNull(createCustomerUseCase);
        this.getCustomerByIdUseCase = Objects.requireNonNull(getCustomerByIdUseCase);
        this.listCustomerUseCase = Objects.requireNonNull(listCustomerUseCase);
        this.updateCustomerUseCase = Objects.requireNonNull(updateCustomerUseCase);
        this.deleteCustomerUseCase = Objects.requireNonNull(deleteCustomerUseCase);
    }

    @Override
    public ResponseEntity<?> registerCustomer(final CustomerRequest input) {
        final var command = CreateCustomerCommand.with(
                input.name(),
                input.age(),
                input.genre(),
                input.phone(),
                input.city(),
                input.state(),
                input.country(),
                input.address(),
                input.number(),
                input.zipcode()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCustomerOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/customers/" + output.id())).body(output);

        return createCustomerUseCase.execute(command).fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCustomers(
            String search,
            int page,
            int perPage,
            String sort,
            String direction
    ) {
        return this.listCustomerUseCase.execute(new CustomerSearchQuery(
                page,
                perPage,
                search,
                sort,
                direction
        ));
    }

    @Override
    public CustomerResponse getById(final String id) {
        return CustomerApiPresenter.present(this.getCustomerByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(String id, CustomerRequest input) {
        final var command = UpdateCustomerCommand.with(
                id,
                input.name(),
                input.age(),
                input.genre(),
                input.phone(),
                input.city(),
                input.state(),
                input.country(),
                input.address(),
                input.number(),
                input.zipcode()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCustomerOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return updateCustomerUseCase.execute(command).fold(onError, onSuccess);
    }

    @Override
    public void deleteById(String id) {
        this.deleteCustomerUseCase.execute(id);
    }
}

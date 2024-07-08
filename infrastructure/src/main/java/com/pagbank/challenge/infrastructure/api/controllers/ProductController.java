package com.pagbank.challenge.infrastructure.api.controllers;

import com.pagbank.challenge.application.product.create.CreateProductCommand;
import com.pagbank.challenge.application.product.create.CreateProductOutput;
import com.pagbank.challenge.application.product.create.CreateProductUseCase;
import com.pagbank.challenge.application.product.delete.DeleteProductUseCase;
import com.pagbank.challenge.application.product.retrieve.get.GetProductByIdUseCase;
import com.pagbank.challenge.application.product.retrieve.list.ListProductUseCase;
import com.pagbank.challenge.application.product.update.UpdateProductCommand;
import com.pagbank.challenge.application.product.update.UpdateProductOutput;
import com.pagbank.challenge.application.product.update.UpdateProductUseCase;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.ProductSearchQuery;
import com.pagbank.challenge.domain.validation.handler.Notification;
import com.pagbank.challenge.infrastructure.api.ProductAPI;
import com.pagbank.challenge.infrastructure.product.models.ProductRequest;
import com.pagbank.challenge.infrastructure.product.models.ProductResponse;
import com.pagbank.challenge.infrastructure.product.presenters.ProductApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class ProductController implements ProductAPI {

    private CreateProductUseCase createProductUseCase;
    private GetProductByIdUseCase getProductByIdUseCase;
    private ListProductUseCase listProductUseCase;
    private UpdateProductUseCase updateProductUseCase;
    private DeleteProductUseCase deleteProductUseCase;

    public ProductController(
            final CreateProductUseCase createProductUseCase,
            final GetProductByIdUseCase getProductByIdUseCase,
            final ListProductUseCase listProductUseCase,
            final UpdateProductUseCase updateProductUseCase,
            final DeleteProductUseCase deleteProductUseCase
    ) {
        this.createProductUseCase = Objects.requireNonNull(createProductUseCase);
        this.getProductByIdUseCase = Objects.requireNonNull(getProductByIdUseCase);
        this.listProductUseCase = Objects.requireNonNull(listProductUseCase);
        this.updateProductUseCase = Objects.requireNonNull(updateProductUseCase);
        this.deleteProductUseCase = Objects.requireNonNull(deleteProductUseCase);
    }

    @Override
    public ResponseEntity<?> createProduct(final ProductRequest input) {
        final var command = CreateProductCommand.with(
                input.name(),
                input.rate(),
                input.isActive()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateProductOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/products/" + output.id())).body(output);

        return createProductUseCase.execute(command).fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listProducts(
            String search,
            int page,
            int perPage,
            String sort,
            String direction
    ) {
        return this.listProductUseCase.execute(new ProductSearchQuery(
                page,
                perPage,
                search,
                sort,
                direction
        ));
    }

    @Override
    public ProductResponse getById(final String id) {
        return ProductApiPresenter.present(this.getProductByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(String id, ProductRequest input) {
        final var command = UpdateProductCommand.with(
                id,
                input.name(),
                input.rate(),
                input.isActive()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateProductOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return updateProductUseCase.execute(command).fold(onError, onSuccess);
    }

    @Override
    public void deleteById(String id) {
        this.deleteProductUseCase.execute(id);
    }
}

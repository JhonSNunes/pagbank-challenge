package com.pagbank.challenge.infrastructure.product.presenters;

import com.pagbank.challenge.application.product.retrieve.get.GetProductByIdOutput;
import com.pagbank.challenge.infrastructure.product.models.ProductResponse;

public interface ProductApiPresenter {
    static ProductResponse present(final GetProductByIdOutput output) {
        return new ProductResponse(
                output.id().getValue(),
                output.name(),
                output.rate(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }
}

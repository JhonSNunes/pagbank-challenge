package com.pagbank.challenge.application.product.create;

import com.pagbank.challenge.application.UseCase;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateProductUseCase extends UseCase<CreateProductCommand, Either<Notification, CreateProductOutput>> {
}

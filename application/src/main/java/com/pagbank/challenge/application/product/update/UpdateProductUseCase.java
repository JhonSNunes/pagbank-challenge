package com.pagbank.challenge.application.product.update;

import com.pagbank.challenge.application.UseCase;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateProductUseCase extends UseCase<UpdateProductCommand, Either<Notification, UpdateProductOutput>> {
}

package com.pagbank.challenge.application.customer.create;

import com.pagbank.challenge.application.UseCase;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCustomerUseCase extends UseCase<CreateCustomerCommand, Either<Notification, CreateCustomerOutput>> {
}

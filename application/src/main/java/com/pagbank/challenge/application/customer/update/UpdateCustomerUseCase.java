package com.pagbank.challenge.application.customer.update;

import com.pagbank.challenge.application.UseCase;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCustomerUseCase extends UseCase<UpdateCustomerCommand, Either<Notification, UpdateCustomerOutput>> {
}

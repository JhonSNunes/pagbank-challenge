package com.pagbank.challenge.application.cdborder.create;

import com.pagbank.challenge.application.UseCase;
import com.pagbank.challenge.application.cdborder.CdbOrderOutput;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCdbOrderUseCase extends UseCase<CreateCdbOrderCommand, Either<Notification, CdbOrderOutput>> {
}

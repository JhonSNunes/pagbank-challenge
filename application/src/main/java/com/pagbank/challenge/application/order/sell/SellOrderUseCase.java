package com.pagbank.challenge.application.order.sell;

import com.pagbank.challenge.application.UseCase;
import com.pagbank.challenge.application.order.OrderOutput;
import com.pagbank.challenge.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class SellOrderUseCase extends UseCase<SellOrderCommand, Either<Notification, OrderOutput>> {
}

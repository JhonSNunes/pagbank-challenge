package com.pagbank.challenge.domain.exceptions;

import com.pagbank.challenge.domain.AggregateRoot;
import com.pagbank.challenge.domain.Identifier;
import com.pagbank.challenge.domain.validation.Error;

import java.util.List;

public class NotFoundException extends DomainException {
    private NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> aggregate,
            final Identifier id
            ) {
        final var errorMessage = "%s with ID %s was not found".formatted(
                aggregate.getSimpleName(),
                id.getValue()
        );
        return new NotFoundException(errorMessage, List.of());
    }
}

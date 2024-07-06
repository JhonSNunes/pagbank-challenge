package com.pagbank.challenge.application;

import com.pagbank.challenge.domain.customer.Customer;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN in);
}
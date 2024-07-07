package com.pagbank.challenge.domain.order;

import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.domain.validation.Error;
import com.pagbank.challenge.domain.validation.ValidationHandler;
import com.pagbank.challenge.domain.validation.Validator;

public class OrderValidator extends Validator {
    private final Order order;

    protected OrderValidator(Order order, ValidationHandler handler) {
        super(handler);

        this.order = order;
    }

    @Override
    public void validate() {
        if (this.order.getCustomerId() == null) {
            this.validationHandler().append(new Error("'customerId' should not be null"));
            return;
        }

        if (this.order.getProductId() == null) {
            this.validationHandler().append(new Error("'productId' should not be null"));
            return;
        }

        if (this.order.getAmount().signum() < 0) {
            this.validationHandler().append(new Error("'amount' must be greather or equal than zero!"));
            return;
        }
    }
}

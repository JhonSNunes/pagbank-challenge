package com.pagbank.challenge.domain.product;

import com.pagbank.challenge.domain.validation.Error;
import com.pagbank.challenge.domain.validation.ValidationHandler;
import com.pagbank.challenge.domain.validation.Validator;

public class ProductValidator extends Validator {
    private final Product product;

    protected ProductValidator(Product product, ValidationHandler handler) {
        super(handler);

        this.product = product;
    }

    @Override
    public void validate() {
        if (this.product.getName() == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (this.product.getRate() == null) {
            this.validationHandler().append(new Error("'rate' should not be null"));
            return;
        }

        if (this.product.getRate().signum() < 0) {
            this.validationHandler().append(new Error("'rate' must be greather or equal than zero!"));
            return;
        }
    }
}

package com.pagbank.challenge.domain.customer;

import com.pagbank.challenge.domain.validation.Error;
import com.pagbank.challenge.domain.validation.ValidationHandler;
import com.pagbank.challenge.domain.validation.Validator;

public class CustomerValidator extends Validator {
    private final Customer customer;

    protected CustomerValidator(Customer customer, ValidationHandler handler) {
        super(handler);

        this.customer = customer;
    }

    @Override
    public void validate() {
        if (this.customer.getName() == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (this.customer.getAge() == null) {
            this.validationHandler().append(new Error("'age' should not be null"));
            return;
        }

        if (this.customer.getGenre() == null) {
            this.validationHandler().append(new Error("'genre' should not be null"));
            return;
        }

        if (this.customer.getCity() == null) {
            this.validationHandler().append(new Error("'city' should not be null"));
            return;
        }

        if (this.customer.getState() == null) {
            this.validationHandler().append(new Error("'state' should not be null"));
            return;
        }

        if (this.customer.getCountry() == null) {
            this.validationHandler().append(new Error("'country' should not be null"));
            return;
        }

        if (this.customer.getAddress() == null) {
            this.validationHandler().append(new Error("'address' should not be null"));
            return;
        }

        if (this.customer.getNumber() == null) {
            this.validationHandler().append(new Error("'number' should not be null"));
            return;
        }

        if (this.customer.getZipcode() == null) {
            this.validationHandler().append(new Error("'zipcode' should not be null"));
            return;
        }
    }
}

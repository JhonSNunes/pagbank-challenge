package com.pagbank.challenge.domain;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.validation.handler.Notification;
import com.pagbank.challenge.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomerTest {
    @Test
    public void givenAValidParams_whenCallNewCustomer_thenInstantiateACustomer() {
        final var expectedName = "John O' Connor";
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";
        final var expectedIsActive = true;

        Customer customer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        Assertions.assertNotNull(customer);
        Assertions.assertEquals(expectedName, customer.getName());
        Assertions.assertEquals(expectedAge, customer.getAge());
        Assertions.assertEquals(expectedGenre, customer.getGenre());
        Assertions.assertEquals(expectedPhone, customer.getPhone());
        Assertions.assertEquals(expectedCity, customer.getCity());
        Assertions.assertEquals(expectedState, customer.getState());
        Assertions.assertEquals(expectedCountry, customer.getCountry());
        Assertions.assertEquals(expectedAddress, customer.getAddress());
        Assertions.assertEquals(expectedNumber, customer.getNumber());
        Assertions.assertEquals(expectedZipcode, customer.getZipcode());
        Assertions.assertEquals(expectedIsActive, customer.isActive());
    }

    @Test
    public void givenAValidInvalidNullName_whenCallNewCustomerAndValidate_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        Customer customer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        final var notification = Notification.create();

        customer.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    @Test
    public void givenAValidInvalidNullAge_whenCallNewCustomerAndValidate_thenShouldReceiveError() {
        final var expectedName = "José";
        final Integer expectedAge = null;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'age' should not be null";

        Customer customer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        final var notification = Notification.create();

        customer.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    public void givenAValidInvalidNullGenre_whenCallNewCustomerAndValidate_thenShouldReceiveError() {
        final var expectedName = "José";
        final var expectedAge = 33;
        final String expectedGenre = null;
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'genre' should not be null";

        Customer customer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        final var notification = Notification.create();

        customer.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    public void givenAValidInvalidNullPhone_whenCallNewCustomerAndValidate_thenShouldReceiveError() {
        final var expectedName = "José";
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final String expectedPhone = null;
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'phone' should not be null";

        Customer customer = Customer.registerCustomer(
                null,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        final var notification = Notification.create();

        customer.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    public void givenAValidInvalidCity_whenCallNewCustomerAndValidate_thenShouldReceiveError() {
        final var expectedName = "José";
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final String expectedCity = null;
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'city' should not be null";

        Customer customer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        final var notification = Notification.create();

        customer.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    public void givenAValidInvalidState_whenCallNewCustomerAndValidate_thenShouldReceiveError() {
        final var expectedName = "José";
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final String expectedState = null;
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'state' should not be null";

        Customer customer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        final var notification = Notification.create();

        customer.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    public void givenAValidInvalidCountry_whenCallNewCustomerAndValidate_thenShouldReceiveError() {
        final var expectedName = "José";
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final String expectedCountry = null;
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'country' should not be null";

        Customer customer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        final var notification = Notification.create();

        customer.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    public void givenAValidInvalidAddress_whenCallNewCustomerAndValidate_thenShouldReceiveError() {
        final var expectedName = "José";
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final String expectedAddress = null;
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'address' should not be null";

        Customer customer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        final var notification = Notification.create();

        customer.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    public void givenAValidInvalidNumber_whenCallNewCustomerAndValidate_thenShouldReceiveError() {
        final var expectedName = "José";
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final String expectedNumber = null;
        final var expectedZipcode = "895952225";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'number' should not be null";

        Customer customer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        final var notification = Notification.create();

        customer.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    public void givenAValidInvalidZipcode_whenCallNewCustomerAndValidate_thenShouldReceiveError() {
        final var expectedName = "José";
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final String expectedZipcode = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'zipcode' should not be null";

        Customer customer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
            expectedNumber,
                expectedZipcode
        );

        final var notification = Notification.create();

        customer.validate(notification);

        Assertions.assertEquals(expectedErrorMessage, notification.getFirst().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    @Test
    public void givenAValidActiveCustomer_whenCallDeactivate_thenReturnCustomerInactivated() {
        final var expectedName = "José";
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";

        Customer customer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        Assertions.assertNotNull(customer);
        Assertions.assertTrue(customer.isActive());

        final var updatedAt = customer.getUpdatedAt();
        final var createdAt = customer.getCreatedAt();

        customer.deactivate();

        Assertions.assertFalse(customer.isActive());
        Assertions.assertTrue(customer.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertEquals(createdAt, customer.getCreatedAt());
        Assertions.assertNotNull(customer.getDeletedAt());
    }

    @Test
    public void givenAValidActiveCustomer_whenCallActivate_thenReturnCustomerActivated() {
        final var expectedName = "José";
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";

        Customer customer = Customer.registerCustomer(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        Assertions.assertNotNull(customer);
        Assertions.assertTrue(customer.isActive());

        customer.deactivate();

        final var createdAt = customer.getCreatedAt();
        final var updatedAt = customer.getUpdatedAt();

        customer.activate();

        Assertions.assertTrue(customer.isActive());
        Assertions.assertTrue(customer.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertEquals(createdAt, customer.getCreatedAt());
        Assertions.assertNull(customer.getDeletedAt());
    }

    @Test
    public void givenAValidActiveCustomer_whenCallUpdate_thenReturnCustomerUpdated() {
        final var expectedName = "José";
        final var expectedAge = 33;
        final var expectedGenre = "masculino";
        final var expectedPhone = "(47) 99900-3333";
        final var expectedCity = "Joinville";
        final var expectedState = "Santa Catarina";
        final var expectedCountry = "Brasil";
        final var expectedAddress = "Rua teste da silva";
        final var expectedNumber = "232333";
        final var expectedZipcode = "895952225";

        final Customer customer = Customer.registerCustomer(
                "Nome errado",
                23,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                "Rua errada",
                expectedNumber,
                expectedZipcode
        );

        Assertions.assertNotNull(customer);
        Assertions.assertTrue(customer.isActive());
        Assertions.assertDoesNotThrow(() -> customer.validate(new ThrowsValidationHandler()));

        final var previousId = customer.getId();
        final var previousName = customer.getName();
        final var previousAge = customer.getAge();
        final var previousAddress = customer.getAddress();
        final var previousUpdatedAt = customer.getUpdatedAt();

        final Customer updatedCustomer = customer.update(
                expectedName,
                expectedAge,
                expectedGenre,
                expectedPhone,
                expectedCity,
                expectedState,
                expectedCountry,
                expectedAddress,
                expectedNumber,
                expectedZipcode
        );

        Assertions.assertNotNull(updatedCustomer);
        Assertions.assertTrue(updatedCustomer.isActive());
        Assertions.assertDoesNotThrow(() -> updatedCustomer.validate(new ThrowsValidationHandler()));
        Assertions.assertTrue(updatedCustomer.getUpdatedAt().isAfter(previousUpdatedAt));
        Assertions.assertEquals(updatedCustomer.getCreatedAt(), customer.getCreatedAt());
        Assertions.assertNull(customer.getDeletedAt());
        Assertions.assertNull(updatedCustomer.getDeletedAt());
        Assertions.assertEquals(updatedCustomer.getId(), previousId);
        Assertions.assertNotEquals(updatedCustomer.getName(), previousName);
        Assertions.assertNotEquals(updatedCustomer.getAge(), previousAge);
        Assertions.assertNotEquals(updatedCustomer.getAddress(), previousAddress);
    }
}

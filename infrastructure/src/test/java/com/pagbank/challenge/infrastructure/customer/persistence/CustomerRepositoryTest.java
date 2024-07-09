package com.pagbank.challenge.infrastructure.customer.persistence;

import com.pagbank.challenge.application.customer.retrieve.get.GetCustomerByIdUseCase;
import com.pagbank.challenge.application.product.retrieve.get.GetProductByIdUseCase;
import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.MySQLGatewayTest;
import com.pagbank.challenge.infrastructure.cdborder.CdbOrderMySQLGateway;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CustomerRepositoryTest {
//    @Mock
    private GetCustomerByIdUseCase getCustomerByIdUseCase;
//
//    @Mock
//    private GetProductByIdUseCase getProductByIdUseCase;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer defaultCustomer;

    @BeforeEach
    public void registerDefaultCustomer() {
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

        this.defaultCustomer = Customer.registerCustomer(
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
    }

    @Test
    public void givenAInvalidNullName_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "name";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity.name";

        final var actualCustomer = CustomerJpaEntity.from(this.defaultCustomer);

        actualCustomer.setName(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(actualCustomer));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullAge_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "age";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity.age";

        final var actualCustomer = CustomerJpaEntity.from(this.defaultCustomer);

        actualCustomer.setAge(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(actualCustomer));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullGenre_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "genre";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity.genre";

        final var actualCustomer = CustomerJpaEntity.from(this.defaultCustomer);

        actualCustomer.setGenre(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(actualCustomer));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullPhone_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "phone";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity.phone";

        final var actualCustomer = CustomerJpaEntity.from(this.defaultCustomer);

        actualCustomer.setPhone(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(actualCustomer));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullCity_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "city";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity.city";

        final var actualCustomer = CustomerJpaEntity.from(this.defaultCustomer);

        actualCustomer.setCity(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(actualCustomer));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullState_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "state";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity.state";

        final var actualCustomer = CustomerJpaEntity.from(this.defaultCustomer);

        actualCustomer.setState(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(actualCustomer));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullCountry_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "country";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity.country";

        final var actualCustomer = CustomerJpaEntity.from(this.defaultCustomer);

        actualCustomer.setCountry(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(actualCustomer));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullAddress_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "address";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity.address";

        final var actualCustomer = CustomerJpaEntity.from(this.defaultCustomer);

        actualCustomer.setAddress(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(actualCustomer));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullNumber_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "number";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity.number";

        final var actualCustomer = CustomerJpaEntity.from(this.defaultCustomer);

        actualCustomer.setNumber(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(actualCustomer));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullZipcode_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "zipcode";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity.zipcode";

        final var actualCustomer = CustomerJpaEntity.from(this.defaultCustomer);

        actualCustomer.setZipcode(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(actualCustomer));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullCreatedAt_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "createdAt";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity.createdAt";

        final var actualCustomer = CustomerJpaEntity.from(this.defaultCustomer);

        actualCustomer.setCreatedAt(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(actualCustomer));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }

    @Test
    public void givenAInvalidNullUpdatedAt_whenCallsCreate_shouldThrowError() {
        final var propertyExpection = "updatedAt";
        final var expectionMessage = "not-null property references a null or transient value : com.pagbank.challenge.infrastructure.customer.persistence.CustomerJpaEntity.updatedAt";

        final var actualCustomer = CustomerJpaEntity.from(this.defaultCustomer);

        actualCustomer.setUpdatedAt(null);

        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(actualCustomer));

        final var exceptionCause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(propertyExpection, exceptionCause.getPropertyName());
        Assertions.assertEquals(expectionMessage, exceptionCause.getMessage());
    }
}

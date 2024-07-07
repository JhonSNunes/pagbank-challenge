package com.pagbank.challenge.domain.customer;

import com.pagbank.challenge.domain.AggregateRoot;
import com.pagbank.challenge.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Customer extends AggregateRoot<CustomerID> implements Cloneable {
    private String name;
    private Integer age;
    private String genre;
    private String phone;
    private String city;
    private String state;
    private String country;
    private String address;
    private String number;
    private String zipcode;
    private Boolean active = false;
    private Instant createdAt = null;
    private Instant updatedAt = null;
    private Instant deletedAt = null;

    private Customer(
            final CustomerID id,
            final String name,
            final Integer age,
            final String genre,
            final String phone,
            final String city,
            final String state,
            final String country,
            final String address,
            final String number,
            final String zipcode,
            final Boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        super(id);
        this.name = name;
        this.age = age;
        this.genre = genre;
        this.phone = phone;
        this.city = city;
        this.state = state;
        this.country = country;
        this.address = address;
        this.number = number;
        this.zipcode = zipcode;
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
        this.deletedAt = deletedAt;
    }

    public static Customer registerCustomer(
            final String name,
            final Integer age,
            final String genre,
            final String phone,
            final String city,
            final String state,
            final String country,
            final String address,
            final String number,
            final String zipcode
    ) {
        Instant now = Instant.now();
        CustomerID id = CustomerID.unique();

        return new Customer(
                id,
                name,
                age,
                genre,
                phone,
                city,
                state,
                country,
                address,
                number,
                zipcode,
                true,
                now,
                now,
                null
        );
    }

    public static Customer with(
            final CustomerID id,
            final String name,
            final Integer age,
            final String genre,
            final String phone,
            final String city,
            final String state,
            final String country,
            final String address,
            final String number,
            final String zipcode,
            final Boolean active,
            final Instant createdAt,
            final Instant updatedAtAt,
            final Instant deletedAtAt
    ) {
        return new Customer(
                id,
                name,
                age,
                genre,
                phone,
                city,
                state,
                country,
                address,
                number,
                zipcode,
                active,
                createdAt,
                updatedAtAt,
                deletedAtAt
        );
    }

    public Customer update(
            final String name,
            final Integer age,
            final String genre,
            final String phone,
            final String city,
            final String state,
            final String country,
            final String address,
            final String number,
            final String zipcode
    ) {
        Instant now = Instant.now();

        this.name = name;
        this.age = age;
        this.genre = genre;
        this.phone = phone;
        this.city = city;
        this.state = state;
        this.country = country;
        this.address = address;
        this.number = number;
        this.zipcode = zipcode;

        this.updatedAt = now;

        return this;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CustomerValidator(this, handler).validate();
    }

    public Customer activate() {
        Instant now = Instant.now();

        this.active = true;
        this.updatedAt = now;
        this.deletedAt = null;

        return this;
    }

    public Customer deactivate() {
        Instant now = Instant.now();

        this.active = false;
        this.updatedAt = now;

        if (getDeletedAt() == null) {
            this.deletedAt = now;
        }

        return this;
    }

    public CustomerID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getGenre() {
        return genre;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }

    public String getZipcode() {
        return zipcode;
    }

    public Boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Customer clone() {
        try {
            Customer clone = (Customer) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
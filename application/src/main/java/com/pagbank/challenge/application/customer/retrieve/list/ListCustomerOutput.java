package com.pagbank.challenge.application.customer.retrieve.list;

import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerID;

import java.time.Instant;

public record ListCustomerOutput(
        CustomerID id,
        String name,
        Integer age,
        String genre,
        String phone,
        String city,
        String state,
        String country,
        String address,
        String number,
        String zipcode,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static ListCustomerOutput from(final Customer customer) {
        return new ListCustomerOutput(
                customer.getId(),
                customer.getName(),
                customer.getAge(),
                customer.getGenre(),
                customer.getPhone(),
                customer.getCity(),
                customer.getState(),
                customer.getCountry(),
                customer.getAddress(),
                customer.getNumber(),
                customer.getZipcode(),
                customer.isActive(),
                customer.getCreatedAt(),
                customer.getUpdatedAt(),
                customer.getDeletedAt()
        );
    }
}

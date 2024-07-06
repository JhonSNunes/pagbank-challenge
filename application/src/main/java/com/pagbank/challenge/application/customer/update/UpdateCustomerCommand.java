package com.pagbank.challenge.application.customer.update;

public record UpdateCustomerCommand(
        String id,
        String name,
        Integer age,
        String genre,
        String phone,
        String city,
        String state,
        String country,
        String address,
        String number,
        String zipcode
) {
    public static UpdateCustomerCommand with(
            final String id,
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
        return new UpdateCustomerCommand(
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
                zipcode
        );
    }
}

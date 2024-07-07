package com.pagbank.challenge.infrastructure.api.controllers;

import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.infrastructure.api.CustomerAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController implements CustomerAPI {
    @Override
    public ResponseEntity<?> registerCustomer() {
        return null;
    }

    @Override
    public Pagination<?> listCustomers(String search, int page, int perPage, String sort, String direction) {
        return null;
    }
}

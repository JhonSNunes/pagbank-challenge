package com.pagbank.challenge.infrastructure.api;

import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.infrastructure.customer.models.CustomerRequest;
import com.pagbank.challenge.infrastructure.customer.models.CustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "customers")
@Tag(name = "Customers")
public interface CustomerAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Register a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was throw")
    })
    ResponseEntity<?> registerCustomer(@RequestBody CustomerRequest input);

    @GetMapping
    @Operation(summary = "Lista all customers paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server error was throw")
    })
    Pagination<?> listCustomers(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a customer by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get customer successfully"),
            @ApiResponse(responseCode = "404", description = "Customer was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was throw")
    })
    CustomerResponse getById(@PathVariable(name = "id") String id);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a customer by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was throw")
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") String id, @RequestBody CustomerRequest input);

    @DeleteMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a customer by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "500", description = "An internal server error was throw")
    })
    void deleteById(@PathVariable(name = "id") String id);

}

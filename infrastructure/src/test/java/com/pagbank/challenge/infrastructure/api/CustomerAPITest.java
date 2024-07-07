package com.pagbank.challenge.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagbank.challenge.ControllerTest;
import com.pagbank.challenge.application.customer.create.CreateCustomerOutput;
import com.pagbank.challenge.application.customer.create.CreateCustomerUseCase;
import com.pagbank.challenge.application.customer.create.GetCustomerByIdOutput;
import com.pagbank.challenge.application.customer.delete.DeleteCustomerUseCase;
import com.pagbank.challenge.application.customer.retrieve.get.GetCustomerByIdUseCase;
import com.pagbank.challenge.application.customer.retrieve.list.ListCustomerOutput;
import com.pagbank.challenge.application.customer.retrieve.list.ListCustomerUseCase;
import com.pagbank.challenge.application.customer.update.UpdateCustomerOutput;
import com.pagbank.challenge.application.customer.update.UpdateCustomerUseCase;
import com.pagbank.challenge.domain.customer.Customer;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.exceptions.DomainException;
import com.pagbank.challenge.domain.exceptions.NotFoundException;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.validation.Error;
import com.pagbank.challenge.domain.validation.handler.Notification;
import com.pagbank.challenge.infrastructure.customer.models.CustomerRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CustomerAPI.class)
public class CustomerAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCustomerUseCase createCustomerUseCase;

    @MockBean
    private GetCustomerByIdUseCase getCustomerByIdUseCase;

    @MockBean
    private ListCustomerUseCase listCustomerUseCase;

    @MockBean
    private UpdateCustomerUseCase updateCustomerUseCase;

    @MockBean
    private DeleteCustomerUseCase deleteCustomerUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCustomer_shouldReturnCustomerID() throws Exception {
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
        final var apiInput = new CustomerRequest(
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

        final var customerId = "123456";

        when(createCustomerUseCase.execute(any()))
                .thenReturn(Right(CreateCustomerOutput.from(customerId)));

        final var request = MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/customers/123456"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123456")));

        Mockito.verify(createCustomerUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedAge, cmd.age())
                && Objects.equals(expectedGenre, cmd.genre())
                && Objects.equals(expectedPhone, cmd.phone())
                && Objects.equals(expectedCity, cmd.city())
                && Objects.equals(expectedState, cmd.state())
                && Objects.equals(expectedCountry, cmd.country())
                && Objects.equals(expectedAddress, cmd.address())
                && Objects.equals(expectedNumber, cmd.number())
                && Objects.equals(expectedZipcode, cmd.zipcode())
        ));
    }

    @Test
    public void givenAInvalidCommandName_whenCallsCreateCustomer_shouldReturnError() throws Exception {
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
        final var expectedMessage = "'name' should not be null";

        final var apiInput = new CustomerRequest(
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

        when(createCustomerUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var request = MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        Mockito.verify(createCustomerUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedAge, cmd.age())
                        && Objects.equals(expectedGenre, cmd.genre())
                        && Objects.equals(expectedPhone, cmd.phone())
                        && Objects.equals(expectedCity, cmd.city())
                        && Objects.equals(expectedState, cmd.state())
                        && Objects.equals(expectedCountry, cmd.country())
                        && Objects.equals(expectedAddress, cmd.address())
                        && Objects.equals(expectedNumber, cmd.number())
                        && Objects.equals(expectedZipcode, cmd.zipcode())
        ));
    }

    @Test
    public void givenAInvalidCommandName_whenCallsCreateCustomer_shouldThrowADomainExpcetion() throws Exception {
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
        final var expectedMessage = "'name' should not be null";

        final var apiInput = new CustomerRequest(
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

        when(createCustomerUseCase.execute(any()))

                .thenThrow(DomainException.with(new Error(expectedMessage)));

        final var request = MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        Mockito.verify(createCustomerUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedAge, cmd.age())
                        && Objects.equals(expectedGenre, cmd.genre())
                        && Objects.equals(expectedPhone, cmd.phone())
                        && Objects.equals(expectedCity, cmd.city())
                        && Objects.equals(expectedState, cmd.state())
                        && Objects.equals(expectedCountry, cmd.country())
                        && Objects.equals(expectedAddress, cmd.address())
                        && Objects.equals(expectedNumber, cmd.number())
                        && Objects.equals(expectedZipcode, cmd.zipcode())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetCustomer_shouldBeOK() throws Exception {
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

        final var persistedCustomer = Customer.registerCustomer(
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

        final var expectedId = persistedCustomer.getId().getValue();

        Mockito.when(getCustomerByIdUseCase.execute(any()))
                .thenReturn(GetCustomerByIdOutput.from(persistedCustomer));

        final var request = MockMvcRequestBuilders
                .get("/customers/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.age", equalTo(expectedAge)))
                .andExpect(jsonPath("$.genre", equalTo(expectedGenre)))
                .andExpect(jsonPath("$.phone", equalTo(expectedPhone)))
                .andExpect(jsonPath("$.city", equalTo(expectedCity)))
                .andExpect(jsonPath("$.state", equalTo(expectedState)))
                .andExpect(jsonPath("$.country", equalTo(expectedCountry)))
                .andExpect(jsonPath("$.address", equalTo(expectedAddress)))
                .andExpect(jsonPath("$.number", equalTo(expectedNumber)))
                .andExpect(jsonPath("$.zipcode", equalTo(expectedZipcode)))
                .andExpect(jsonPath("$.is_active", equalTo(persistedCustomer.isActive())))
                .andExpect(jsonPath("$.created_at", equalTo(persistedCustomer.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(persistedCustomer.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(persistedCustomer.getDeletedAt())));
    }

    @Test
    public void givenAInvalidId_whenCallsGetCustomer_shouldReturnNotFound() throws Exception {
        final var invalidCustomerId = CustomerID.from("AN-INVALID-ID-123");
        final var expectedErrorMessage = "Customer with ID %s was not found".formatted(invalidCustomerId.getValue());

        Mockito.when(getCustomerByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Customer.class, invalidCustomerId));

        final var request = MockMvcRequestBuilders
                .get("/customers/{id}", invalidCustomerId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCustomer_shouldReturnCustomerID() throws Exception {
        final var customerId = "123456";
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
        final var apiInput = new CustomerRequest(
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

        when(updateCustomerUseCase.execute(any()))
                .thenReturn(Right(UpdateCustomerOutput.from(customerId)));

        final var request = MockMvcRequestBuilders.put("/customers/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123456")));

        Mockito.verify(updateCustomerUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedAge, cmd.age())
                        && Objects.equals(expectedGenre, cmd.genre())
                        && Objects.equals(expectedPhone, cmd.phone())
                        && Objects.equals(expectedCity, cmd.city())
                        && Objects.equals(expectedState, cmd.state())
                        && Objects.equals(expectedCountry, cmd.country())
                        && Objects.equals(expectedAddress, cmd.address())
                        && Objects.equals(expectedNumber, cmd.number())
                        && Objects.equals(expectedZipcode, cmd.zipcode())
        ));
    }

    @Test
    public void givenACommandWithInvalidId_whenCallsUpdateCustomer_shouldReturnError() throws Exception {
        final var customerId = CustomerID.from("123456");
        final var expectedErrorMessage = "Customer with ID 123456 was not found";
        final var apiInput = new CustomerRequest("José", 33, "masculino", "(47) 99900-3333", "Joinville", "Santa Catarina", "Brasil", "Rua teste da silva", "232333", "895952225");

        Mockito.when(updateCustomerUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Customer.class, customerId));

        final var request = MockMvcRequestBuilders.put("/customers/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenACommandWithInvalidName_whenCallsUpdateCustomer_shouldReturnError() throws Exception {
        final var customerId = CustomerID.from("123456");
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var apiInput = new CustomerRequest(null, 33, "masculino", "(47) 99900-3333", "Joinville", "Santa Catarina", "Brasil", "Rua teste da silva", "232333", "895952225");

        Mockito.when(updateCustomerUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.put("/customers/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCustomer_shouldReturnNoContent() throws Exception {
        final var customerId = "123456";

        Mockito.doNothing()
                        .when(deleteCustomerUseCase).execute(any());

        final var request = MockMvcRequestBuilders
                .delete("/customers/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent());

        Mockito.verify(deleteCustomerUseCase, times(1)).execute(eq(customerId));
    }

    @Test
    public void givenAValidQuery_whenCallsListCustomers_shouldReturnCustomer() throws Exception {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Jos";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var customer = Customer.registerCustomer("José", 33, "masculino", "(47) 99900-3333", "Joinville", "Santa Catarina", "Brasil", "Rua teste da silva", "232333", "895952225");

        final var expectedItems = List.of(ListCustomerOutput.from(customer));

        Mockito.when(listCustomerUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders
                .get("/customers")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(expectedPage)))
                .andExpect(jsonPath("$.perPage", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id.value", equalTo(customer.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(customer.getName())));

    }
}

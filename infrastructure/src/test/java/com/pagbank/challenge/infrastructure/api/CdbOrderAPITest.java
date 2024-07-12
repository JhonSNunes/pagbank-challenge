package com.pagbank.challenge.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagbank.challenge.ControllerTest;
import com.pagbank.challenge.application.cdborder.CdbOrderOutput;
import com.pagbank.challenge.application.cdborder.create.CreateCdbOrderUseCase;
import com.pagbank.challenge.application.cdborder.delete.DeleteOrderUseCase;
import com.pagbank.challenge.application.cdborder.retrieve.get.GetOrderByIdOutput;
import com.pagbank.challenge.application.cdborder.retrieve.get.GetOrderByIdUseCase;
import com.pagbank.challenge.application.cdborder.retrieve.list.ListOrderOutput;
import com.pagbank.challenge.application.cdborder.retrieve.list.ListOrderUseCase;
import com.pagbank.challenge.domain.cdborder.CdbOrder;
import com.pagbank.challenge.domain.cdborder.CdbOrderID;
import com.pagbank.challenge.domain.cdborder.CdbOrderTransactionType;
import com.pagbank.challenge.domain.customer.CustomerID;
import com.pagbank.challenge.domain.exceptions.NotFoundException;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.validation.Error;
import com.pagbank.challenge.domain.validation.handler.Notification;
import com.pagbank.challenge.infrastructure.cdborder.models.CdbOrderRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.Instant;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = CdbOrderAPI.class)
public class CdbOrderAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCdbOrderUseCase createCdbOrderUseCase;

    @MockBean
    private GetOrderByIdUseCase getOrderByIdUseCase;

    @MockBean
    private ListOrderUseCase listOrderUseCase;

    @MockBean
    private DeleteOrderUseCase deleteOrderUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCdbOrder_shouldReturnCdbOrderID() throws Exception {
        final var expectedCustomerId = "valid-customer-id";
        final var expectedProductId = "valid-product-id";
        final var expectedAmount = new BigDecimal("200.00");
        final var expectectedOrderType = CdbOrderTransactionType.PURCHASE;

        final var apiInput = new CdbOrderRequest(expectedCustomerId, expectedProductId, expectedAmount, expectectedOrderType);

        final var orderId = "123456";

        when(createCdbOrderUseCase.execute(any()))
                .thenReturn(Right(CdbOrderOutput.from(orderId)));

        final var request = MockMvcRequestBuilders.post("/cdborders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/cdborders/123456"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123456")));

        Mockito.verify(createCdbOrderUseCase, times(1)).execute(argThat(cmd ->
            Objects.equals(expectedCustomerId, cmd.customerId())
                && Objects.equals(expectedProductId, cmd.productId())
                && Objects.equals(expectedAmount, cmd.amount())
                && Objects.equals(expectectedOrderType, cmd.transactionType())
        ));
    }

    @Test
    public void givenAInvalidCommandCustomerId_whenCallsCreateCdbOrder_shouldThrowADomainExpcetion() throws Exception {
        final String expectedCustomerId = null;
        final var expectedProductId = "valid-product-id";
        final var expectedAmount = new BigDecimal("200.00");
        final var expectectedOrderType = CdbOrderTransactionType.PURCHASE;
        final var expectedMessage = "'customerId' should not be null";

        final var apiInput = new CdbOrderRequest(expectedCustomerId, expectedProductId, expectedAmount, expectectedOrderType);

        when(createCdbOrderUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var request = MockMvcRequestBuilders.post("/cdborders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        Mockito.verify(createCdbOrderUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedCustomerId, cmd.customerId())
                        && Objects.equals(expectedProductId, cmd.productId())
                        && Objects.equals(expectedAmount, cmd.amount())
                        && Objects.equals(expectectedOrderType, cmd.transactionType())
        ));
    }

    @Test
    public void givenAInvalidCommandProductId_whenCallsCreateCdbOrder_shouldThrowADomainExpcetion() throws Exception {
        final var expectedCustomerId = "valid-product-id";
        final String expectedProductId = null;
        final var expectedAmount = new BigDecimal("200.00");
        final var expectectedOrderType = CdbOrderTransactionType.PURCHASE;
        final var expectedMessage = "'productId' should not be null";

        final var apiInput = new CdbOrderRequest(expectedCustomerId, expectedProductId, expectedAmount, expectectedOrderType);

        when(createCdbOrderUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var request = MockMvcRequestBuilders.post("/cdborders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        Mockito.verify(createCdbOrderUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedCustomerId, cmd.customerId())
                        && Objects.equals(expectedProductId, cmd.productId())
                        && Objects.equals(expectedAmount, cmd.amount())
                        && Objects.equals(expectectedOrderType, cmd.transactionType())
        ));
    }

    @Test
    public void givenAInvalidCommandAmount_whenCallsCreateCdbOrder_shouldThrowADomainExpcetion() throws Exception {
        final var expectedCustomerId = "valid-customer-id";
        final var expectedProductId = "valid-product-id";
        final var expectedAmount = new BigDecimal("-1");
        final var expectectedOrderType = CdbOrderTransactionType.PURCHASE;
        final var expectedMessage = "'amount' must be greather or equal than zero!";

        final var apiInput = new CdbOrderRequest(expectedCustomerId, expectedProductId, expectedAmount, expectectedOrderType);

        when(createCdbOrderUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var request = MockMvcRequestBuilders.post("/cdborders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        Mockito.verify(createCdbOrderUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedCustomerId, cmd.customerId())
                        && Objects.equals(expectedProductId, cmd.productId())
                        && Objects.equals(expectedAmount, cmd.amount())
                        && Objects.equals(expectectedOrderType, cmd.transactionType())
        ));
    }


    @Test
    public void givenAValidId_whenCallsGetCdbOrder_shouldBeOK() throws Exception {
        final var expectedCustomerId = CustomerID.from( "valid-customer-id");
        final var expectedProductId = ProductID.from("valid-product-id");
        final var expectedAmount = new BigDecimal("200.00");
        final var expectectedOrderType = CdbOrderTransactionType.PURCHASE;
        final var someTime = Instant.now().toString();

        final var persistedOrder = CdbOrder.createOrder(expectedCustomerId, expectedProductId, expectedAmount, expectectedOrderType);

        final var expectedId = persistedOrder.getId().getValue();

        Mockito.when(getOrderByIdUseCase.execute(any()))
                .thenReturn(GetOrderByIdOutput.from(persistedOrder));

        final var request = MockMvcRequestBuilders
                .get("/cdborders/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);


        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.order_id", equalTo(expectedId)))
                .andExpect(jsonPath("$.customer_id", equalTo(expectedCustomerId.getValue())))
                .andExpect(jsonPath("$.product_id", equalTo(expectedProductId.getValue())))
                .andExpect(jsonPath("$.transaction_date", Matchers.notNullValue()))
                .andExpect(jsonPath("$.transaction_date", Matchers.greaterThan(someTime)))
                .andExpect(jsonPath("$.transaction_type", equalTo(expectectedOrderType.toString())));
    }

    @Test
    public void givenAInvalidId_whenCallsGetOrder_shouldReturnNotFound() throws Exception {
        final var invalidId = CdbOrderID.from("AN-INVALID-ID-123");
        final var expectedErrorMessage = "CdbOrder with ID %s was not found".formatted(invalidId.getValue());

        Mockito.when(getOrderByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(CdbOrder.class, invalidId));

        final var request = MockMvcRequestBuilders
                .get("/cdborders/{id}", invalidId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidId_whenCallsDeleteOrder_shouldReturnNoContent() throws Exception {
        final var orderId = "123456";

        Mockito.doNothing()
                        .when(deleteOrderUseCase).execute(any());

        final var request = MockMvcRequestBuilders
                .delete("/cdborders/{id}", orderId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent());

        Mockito.verify(deleteOrderUseCase, times(1)).execute(eq(orderId));
    }

    @Test
    public void givenAValidQuery_whenCallsListOrders_shouldReturnAllOrders() throws Exception {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final String expectedCustomerId = null;
        final String expectedProductId = null;
        final var expectedDirection = "asc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var order = CdbOrder.createOrder(CustomerID.unique(), ProductID.unique(), new BigDecimal("201.5"), CdbOrderTransactionType.PURCHASE);

        final var expectedItems = List.of(ListOrderOutput.from(order));

        Mockito.when(listOrderUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders
                .get("/cdborders")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("customer_id", expectedCustomerId)
                .queryParam("product_id", expectedProductId)
                .queryParam("dir", expectedDirection)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(expectedPage)))
                .andExpect(jsonPath("$.perPage", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id.value", equalTo(order.getId().getValue())))
                .andExpect(jsonPath("$.items[0].customerId.value", equalTo(order.getCustomerId().getValue())))
                .andExpect(jsonPath("$.items[0].productId.value", equalTo(order.getProductId().getValue())))
                .andExpect(jsonPath("$.items[0].amount", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items[0].transactionDate", equalTo(order.getTransactionDate().toString())))
                .andExpect(jsonPath("$.items[0].transactionType", equalTo(order.getTransactionType().toString())));

    }
}

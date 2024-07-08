package com.pagbank.challenge.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagbank.challenge.ControllerTest;
import com.pagbank.challenge.application.product.create.CreateProductOutput;
import com.pagbank.challenge.application.product.create.CreateProductUseCase;
import com.pagbank.challenge.application.product.delete.DeleteProductUseCase;
import com.pagbank.challenge.application.product.retrieve.get.GetProductByIdOutput;
import com.pagbank.challenge.application.product.retrieve.get.GetProductByIdUseCase;
import com.pagbank.challenge.application.product.retrieve.list.ListProductOutput;
import com.pagbank.challenge.application.product.retrieve.list.ListProductUseCase;
import com.pagbank.challenge.application.product.update.UpdateProductOutput;
import com.pagbank.challenge.application.product.update.UpdateProductUseCase;
import com.pagbank.challenge.domain.exceptions.DomainException;
import com.pagbank.challenge.domain.exceptions.NotFoundException;
import com.pagbank.challenge.domain.pagination.Pagination;
import com.pagbank.challenge.domain.product.Product;
import com.pagbank.challenge.domain.product.ProductID;
import com.pagbank.challenge.domain.validation.Error;
import com.pagbank.challenge.domain.validation.handler.Notification;
import com.pagbank.challenge.infrastructure.product.models.ProductRequest;
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

@ControllerTest(controllers = ProductAPI.class)
public class ProductAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateProductUseCase createProductUseCase;

    @MockBean
    private GetProductByIdUseCase getProductByIdUseCase;

    @MockBean
    private ListProductUseCase listProductUseCase;

    @MockBean
    private UpdateProductUseCase updateProductUseCase;

    @MockBean
    private DeleteProductUseCase deleteProductUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateProduct_shouldReturnProductID() throws Exception {
        final var expectedName = "CDB 200%";
        final var expectedRate = new BigDecimal("200.00");
        final var expectedIsActive = true;

        final var apiInput = new ProductRequest(expectedName, expectedRate, expectedIsActive);

        final var productId = "123456";

        when(createProductUseCase.execute(any()))
                .thenReturn(Right(CreateProductOutput.from(productId)));

        final var request = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/products/123456"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123456")));

        Mockito.verify(createProductUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedRate, cmd.rate())
                && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidCommandName_whenCallsCreateProduct_shouldReturnError() throws Exception {
        final String expectedName = null;
        final var expectedRate = new BigDecimal("200.00");
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var apiInput = new ProductRequest(expectedName, expectedRate, expectedIsActive);

        when(createProductUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var request = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        Mockito.verify(createProductUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedRate, cmd.rate())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidCommandName_whenCallsCreateProduct_shouldThrowADomainExpcetion() throws Exception {
        final String expectedName = null;
        final var expectedRate = new BigDecimal("200.00");
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var apiInput = new ProductRequest(expectedName, expectedRate, expectedIsActive);

        when(createProductUseCase.execute(any()))

                .thenThrow(DomainException.with(new Error(expectedMessage)));

        final var request = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        Mockito.verify(createProductUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedRate, cmd.rate())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetProduct_shouldBeOK() throws Exception {
        final var expectedName = "CDB 200%";
        final var expectedRate = new BigDecimal("200.00");
        final var expectedIsActive = true;

        final var persistedProduct = Product.createProduct(expectedName, expectedRate, expectedIsActive);

        final var expectedId = persistedProduct.getId().getValue();

        Mockito.when(getProductByIdUseCase.execute(any()))
                .thenReturn(GetProductByIdOutput.from(persistedProduct));

        final var request = MockMvcRequestBuilders
                .get("/products/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.is_active", equalTo(persistedProduct.isActive())))
                .andExpect(jsonPath("$.created_at", equalTo(persistedProduct.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(persistedProduct.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(persistedProduct.getDeletedAt())));
    }

    @Test
    public void givenAInvalidId_whenCallsGetProduct_shouldReturnNotFound() throws Exception {
        final var invalidId = ProductID.from("AN-INVALID-ID-123");
        final var expectedErrorMessage = "Product with ID %s was not found".formatted(invalidId.getValue());

        Mockito.when(getProductByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Product.class, invalidId));

        final var request = MockMvcRequestBuilders
                .get("/products/{id}", invalidId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateProduct_shouldReturnProductID() throws Exception {
        final var productId = "123456";
        final var expectedName = "CDB 200%";
        final var expectedRate = new BigDecimal("200.00");
        final var expectedIsActive = true;

        final var apiInput = new ProductRequest(expectedName, expectedRate, expectedIsActive);

        when(updateProductUseCase.execute(any()))
                .thenReturn(Right(UpdateProductOutput.from(productId)));

        final var request = MockMvcRequestBuilders.put("/products/{id}", productId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(apiInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123456")));

        Mockito.verify(updateProductUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedRate, cmd.rate())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenACommandWithInvalidId_whenCallsUpdateProduct_shouldReturnError() throws Exception {
        final var productId = ProductID.from("123456");
        final var expectedErrorMessage = "Product with ID 123456 was not found";
        final var apiInput = new ProductRequest("CDB 200%", new BigDecimal("200.00"), true);

        Mockito.when(updateProductUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Product.class, productId));

        final var request = MockMvcRequestBuilders.put("/products/{id}", productId)
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
    public void givenACommandWithInvalidName_whenCallsUpdateProduct_shouldReturnError() throws Exception {
        final var productId = ProductID.from("123456");
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var apiInput = new ProductRequest(null, new BigDecimal("200.00"), true);

        Mockito.when(updateProductUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.put("/products/{id}", productId)
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
    public void givenAValidId_whenCallsDeleteProduct_shouldReturnNoContent() throws Exception {
        final var productId = "123456";

        Mockito.doNothing()
                        .when(deleteProductUseCase).execute(any());

        final var request = MockMvcRequestBuilders
                .delete("/products/{id}", productId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent());

        Mockito.verify(deleteProductUseCase, times(1)).execute(eq(productId));
    }

    @Test
    public void givenAValidQuery_whenCallsListProducts_shouldReturnProduct() throws Exception {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "CDB";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var product = Product.createProduct("CDB 200%", new BigDecimal("200.00"), true);

        final var expectedItems = List.of(ListProductOutput.from(product));

        Mockito.when(listProductUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders
                .get("/products")
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
                .andExpect(jsonPath("$.items[0].id.value", equalTo(product.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(product.getName())));

    }
}

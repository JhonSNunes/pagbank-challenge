package com.pagbank.challenge.infrastructure.configuration.usecases;

import com.pagbank.challenge.application.product.create.CreateProductUseCase;
import com.pagbank.challenge.application.product.create.DefaultCreateProductUseCase;
import com.pagbank.challenge.application.product.delete.DefaultDeleteProductUseCase;
import com.pagbank.challenge.application.product.delete.DeleteProductUseCase;
import com.pagbank.challenge.application.product.retrieve.get.DefaultGetProductByIdUseCase;
import com.pagbank.challenge.application.product.retrieve.get.GetProductByIdUseCase;
import com.pagbank.challenge.application.product.retrieve.list.DefaultListProductUseCase;
import com.pagbank.challenge.application.product.retrieve.list.ListProductUseCase;
import com.pagbank.challenge.application.product.update.DefaultUpdateProductUseCase;
import com.pagbank.challenge.application.product.update.UpdateProductUseCase;
import com.pagbank.challenge.domain.product.ProductGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductUseCaseConfig {
    private final ProductGateway productGateway;

    public ProductUseCaseConfig(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Bean
    public CreateProductUseCase createProductUseCase() {
        return new DefaultCreateProductUseCase(this.productGateway);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase() {
        return new DefaultUpdateProductUseCase(this.productGateway);
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase() {
        return new DefaultDeleteProductUseCase(this.productGateway);
    }

    @Bean
    public GetProductByIdUseCase getProductByIdUseCase() {
        return new DefaultGetProductByIdUseCase(this.productGateway);
    }

    @Bean
    public ListProductUseCase listProductUseCase() {
        return new DefaultListProductUseCase(this.productGateway);
    }
}

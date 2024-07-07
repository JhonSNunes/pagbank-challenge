package com.pagbank.challenge.infrastructure.configuration.usecases;

import com.pagbank.challenge.application.customer.create.CreateCustomerUseCase;
import com.pagbank.challenge.application.customer.create.DefaultCreateCustomerUseCase;
import com.pagbank.challenge.application.customer.delete.DefaultDeleteCustomerUseCase;
import com.pagbank.challenge.application.customer.delete.DeleteCustomerUseCase;
import com.pagbank.challenge.application.customer.retrieve.get.DefaultGetCustomerByIdUseCase;
import com.pagbank.challenge.application.customer.retrieve.get.GetCustomerByIdUseCase;
import com.pagbank.challenge.application.customer.retrieve.list.CustomerListUseCase;
import com.pagbank.challenge.application.customer.retrieve.list.DefaultCustomerListUseCase;
import com.pagbank.challenge.application.customer.update.DefaultUpdateCustomerUseCase;
import com.pagbank.challenge.application.customer.update.UpdateCustomerUseCase;
import com.pagbank.challenge.domain.customer.CustomerGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerUseCaseConfig {
    private final CustomerGateway customerGateway;

    public CustomerUseCaseConfig(CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    @Bean
    public CreateCustomerUseCase createCustomerUseCase() {
        return new DefaultCreateCustomerUseCase(this.customerGateway);
    }

    @Bean
    public UpdateCustomerUseCase updateCustomerUseCase() {
        return new DefaultUpdateCustomerUseCase(this.customerGateway);
    }

    @Bean
    public DeleteCustomerUseCase deleteCustomerUseCase() {
        return new DefaultDeleteCustomerUseCase(this.customerGateway);
    }

    @Bean
    public GetCustomerByIdUseCase getCustomerByIdUseCase() {
        return new DefaultGetCustomerByIdUseCase(this.customerGateway);
    }

    @Bean
    public CustomerListUseCase customerListUseCase() {
        return new DefaultCustomerListUseCase(this.customerGateway);
    }
}

package com.pagbank.challenge.infrastructure.configuration.usecases;

import com.pagbank.challenge.application.cdborder.create.CreateCdbOrderUseCase;
import com.pagbank.challenge.application.cdborder.create.DefaultCreateCdbOrderUseCase;
import com.pagbank.challenge.application.cdborder.delete.DefaultDeleteOrderUseCase;
import com.pagbank.challenge.application.cdborder.delete.DeleteOrderUseCase;
import com.pagbank.challenge.application.cdborder.retrieve.get.DefaultGetOrderByIdUseCase;
import com.pagbank.challenge.application.cdborder.retrieve.get.GetOrderByIdUseCase;
import com.pagbank.challenge.application.cdborder.retrieve.list.DefaultListOrderUseCase;
import com.pagbank.challenge.application.cdborder.retrieve.list.ListOrderUseCase;
import com.pagbank.challenge.domain.cdborder.CdbOrderGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CdbOrderUseCaseConfig {
    private final CdbOrderGateway cdbOrderGateway;

    public CdbOrderUseCaseConfig(final CdbOrderGateway cdbOrderGateway) {
        this.cdbOrderGateway = cdbOrderGateway;
    }

    @Bean
    public CreateCdbOrderUseCase createCdbOrderUseCase() {
        return new DefaultCreateCdbOrderUseCase(this.cdbOrderGateway);
    }

    @Bean
    public DeleteOrderUseCase deleteOrderUseCase() {
        return new DefaultDeleteOrderUseCase(this.cdbOrderGateway);
    }

    @Bean
    public GetOrderByIdUseCase getOrderByIdUseCase() {
        return new DefaultGetOrderByIdUseCase(this.cdbOrderGateway);
    }

    @Bean
    public ListOrderUseCase listOrderUseCase() {
        return new DefaultListOrderUseCase(this.cdbOrderGateway);
    }
}

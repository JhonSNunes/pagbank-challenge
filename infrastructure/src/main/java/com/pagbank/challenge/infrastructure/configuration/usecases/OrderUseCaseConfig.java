package com.pagbank.challenge.infrastructure.configuration.usecases;

import com.pagbank.challenge.application.order.delete.DefaultDeleteOrderUseCase;
import com.pagbank.challenge.application.order.delete.DeleteOrderUseCase;
import com.pagbank.challenge.application.order.purchase.DefaultPurchaseOrderUseCase;
import com.pagbank.challenge.application.order.purchase.PurchaseOrderUseCase;
import com.pagbank.challenge.application.order.retrieve.get.DefaultGetOrderByIdUseCase;
import com.pagbank.challenge.application.order.retrieve.get.GetOrderByIdUseCase;
import com.pagbank.challenge.application.order.retrieve.list.DefaultListOrderUseCase;
import com.pagbank.challenge.application.order.retrieve.list.ListOrderUseCase;
import com.pagbank.challenge.application.order.sell.DefaultSellOrderUseCase;
import com.pagbank.challenge.application.order.sell.SellOrderUseCase;
import com.pagbank.challenge.domain.order.OrderGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseConfig {
    private final OrderGateway orderGateway;

    public OrderUseCaseConfig(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Bean
    public PurchaseOrderUseCase purchaseOrderUseCase() {
        return new DefaultPurchaseOrderUseCase(this.orderGateway);
    }

    @Bean
    public SellOrderUseCase sellOrderUseCase() {
        return new DefaultSellOrderUseCase(this.orderGateway);
    }

    @Bean
    public DeleteOrderUseCase deleteOrderUseCase() {
        return new DefaultDeleteOrderUseCase(this.orderGateway);
    }

    @Bean
    public GetOrderByIdUseCase getOrderByIdUseCase() {
        return new DefaultGetOrderByIdUseCase(this.orderGateway);
    }

    @Bean
    public ListOrderUseCase listOrderUseCase() {
        return new DefaultListOrderUseCase(this.orderGateway);
    }
}

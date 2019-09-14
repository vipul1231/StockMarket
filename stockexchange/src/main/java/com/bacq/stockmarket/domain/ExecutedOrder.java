package com.bacq.stockmarket.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExecutedOrder {
    private Order order;
    private OrderStatus orderStatus;

    public ExecutedOrder(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }
}

package com.bacq.stockmarket.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ExecutedOrder {
    private List<Order> order = new ArrayList<>();
    private OrderStatus orderStatus;

    public ExecutedOrder(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    public void addExecutedOrder(Order order){
        this.order.add(order);
    }
}

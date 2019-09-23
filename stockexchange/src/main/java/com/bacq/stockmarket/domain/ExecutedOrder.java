package com.bacq.stockmarket.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ExecutedOrder {
    private List<ProcessedOrder> order = new ArrayList<>();
    private OrderStatus orderStatus;

    public ExecutedOrder(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    public void addExecutedOrder(ProcessedOrder order){
        this.order.add(order);
    }
}

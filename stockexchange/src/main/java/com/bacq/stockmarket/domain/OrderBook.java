package com.bacq.stockmarket.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

@Data
public class OrderBook {

    private Map<String, PriorityQueue<Order>> buyOrders;

    private Map<String, PriorityQueue<Order>> sellOrders;

    public OrderBook() {
        buyOrders = new HashMap<>();
        sellOrders = new HashMap<>();
    }
}

package com.bacq.stockmarket.service;

import com.bacq.stockmarket.domain.ExecutedOrder;
import com.bacq.stockmarket.domain.Order;

public interface OrderManagement {

    void addStockOrder(Order order);

    ExecutedOrder verifyAndOrderMatchingBuyRequest(String stock, Double price, int qty);

    ExecutedOrder verifyAndOrderMatchingSellRequest(String stock, Double price, int qty);
}

package com.bacq.stockmarket.service;

import com.bacq.stockmarket.domain.ExecutedOrder;
import com.bacq.stockmarket.domain.Order;

public interface OrderManagement {

    /**
     * This method just add order in order book.
     *
     * @param order
     */
    void addStockOrder(Order order);

    /**
     * This method takes the BUY request and verify if any order matching with SELL request persist or not.
     *
     * @param buyOrder    Buy Order
     * @return            Executed order
     */
    ExecutedOrder verifyAndOrderMatchingBuyRequest(Order buyOrder);

    /**
     * This method takes the SELL request and verify if any order matching with BUY request persist or not.
     *
     * @param sellOrder Sell order
     * @return          Executed Order
     */
    ExecutedOrder verifyAndOrderMatchingSellRequest(Order sellOrder);
}

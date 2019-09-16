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
     * @param stock Stock name
     * @param price Stock price
     * @param qty   Stock quantity
     * @return      Executed order
     */
    ExecutedOrder verifyAndOrderMatchingBuyRequest(String stock, Double price, int qty);

    /**
     * This method takes the SELL request and verify if any order matching with BUY request persist or not.
     *
     * @param stock
     * @param price
     * @param qty
     * @return
     */
    ExecutedOrder verifyAndOrderMatchingSellRequest(String stock, Double price, int qty);
}

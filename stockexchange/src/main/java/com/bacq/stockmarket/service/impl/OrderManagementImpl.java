package com.bacq.stockmarket.service.impl;

import com.bacq.stockmarket.domain.*;
import com.bacq.stockmarket.service.OrderManagement;

import java.util.Comparator;
import java.util.PriorityQueue;

public class OrderManagementImpl implements OrderManagement {

    private OrderBook orderBook;

    public OrderManagementImpl(){
        orderBook = new OrderBook();
    }

    @Override
    public void addStockOrder(Order order) {

        if(order.getOrderType() == OrderType.SELL){

        }
        else {
            PriorityQueue<Order> stockQueue = orderBook.getBuyOrders().get(order.getStock());
            if(stockQueue == null){
                PriorityQueue<Order> orderQueue = new PriorityQueue<>(returnComparator());
                orderQueue.add(order);
                orderBook.getBuyOrders().put(order.getStock(),orderQueue);
            }
        }
    }

    @Override
    public ExecutedOrder verifyAndOrderMatchingBuyRequest(String stock, Double price, int quantity) {

        PriorityQueue<Order> orderQueue = orderBook.getSellOrders().get(stock);
        ExecutedOrder executedOrder = new ExecutedOrder(OrderStatus.NOT_PROCESSED);

        if(orderQueue == null || orderQueue.size() == 0){
            return executedOrder;
        }

        for (Order order : orderQueue) {
            if (price > order.getPrice()) {
                int remaining = quantity - order.getQty();
                if (remaining == 0) {
                    executedOrder.setOrderStatus(OrderStatus.PROCESSED);
                    executedOrder.setOrder(order);
                    break;
                } else if (remaining < 0) {
                    try {
                        Order clonedOrder = (Order) order.clone();
                        executedOrder.setOrder(clonedOrder);
                        executedOrder.setOrderStatus(OrderStatus.PARTIAL);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    order.setQty(Math.abs(remaining));
                    break;
                }
            }
        }

        return executedOrder;
    }

    @Override
    public ExecutedOrder verifyAndOrderMatchingSellRequest(String stock, Double price, int quantity) {
        return null;
    }

    /**
     * Return comparator.
     *
     * @return
     */
    private Comparator<Order> returnComparator() {
        return (o1, o2) -> {
            if(o1.getPrice() > o2.getPrice()){
                    return 1;
            }
            else if(o1.getPrice() < o2.getPrice()){
                return -1;
            }
            else {
                return o1.getTime().compareTo(o2.getTime());
            }
        };
    }
}

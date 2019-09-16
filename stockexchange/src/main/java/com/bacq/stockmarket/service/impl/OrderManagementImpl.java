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

            PriorityQueue<Order> sellStockQueue = orderBook.getSellOrders().get(order.getStock());

            if(sellStockQueue == null){
            PriorityQueue<Order> sellStockNewQueue = new PriorityQueue<>(returnComparator());
            sellStockNewQueue.add(order);
            orderBook.getSellOrders().put(order.getStock(), sellStockNewQueue);
            return;
            }
            sellStockQueue.add(order);
        }
        else {
            PriorityQueue<Order> buyStockQueue = orderBook.getBuyOrders().get(order.getStock());
            if(buyStockQueue == null){
                PriorityQueue<Order> orderQueue = new PriorityQueue<>(returnComparator());
                orderQueue.add(order);
                orderBook.getBuyOrders().put(order.getStock(),orderQueue);
                return;
            }
            buyStockQueue.add(order);
        }
    }

    @Override
    public ExecutedOrder verifyAndOrderMatchingBuyRequest(String stock, Double price, int quantity) {
        PriorityQueue<Order> ordersQueue = orderBook.getSellOrders().get(stock);
        return processOrder(OrderType.BUY, ordersQueue, price, quantity);

    }

    @Override
    public ExecutedOrder verifyAndOrderMatchingSellRequest(String stock, Double price, int quantity) {
        PriorityQueue<Order> ordersQueue = orderBook.getBuyOrders().get(stock);
        return processOrder(OrderType.SELL, ordersQueue, price, quantity);
    }

    private ExecutedOrder processOrder(OrderType orderType, PriorityQueue<Order> orderQueue, Double price, int quantity){
        ExecutedOrder executedOrder = new ExecutedOrder(OrderStatus.NOT_PROCESSED);

        if(orderQueue == null || orderQueue.size() == 0){
            return executedOrder;
        }

        try {
            while(true){
                Order order = orderQueue.peek();
                if(order!=null && ((orderType == OrderType.BUY && price >= order.getPrice()) ||
                        (orderType == OrderType.SELL && order.getPrice() >= price))){
                    int remaining = quantity - order.getQty();
                    if(remaining == 0){
                        executedOrder.setOrderStatus(OrderStatus.PROCESSED);
                        executedOrder.addExecutedOrder(orderQueue.poll());
                        break;
                    }
                    else if(remaining < 0){
                        Order clonedOrder = order.clone();
                        clonedOrder.setQty(quantity);
                        executedOrder.addExecutedOrder(clonedOrder);
                        executedOrder.setOrderStatus(OrderStatus.PROCESSED);
                        order.setQty(Math.abs(remaining));
                        break;
                    }
                    else {
                        quantity = remaining;
                        executedOrder.addExecutedOrder(order.clone());
                        executedOrder.setOrderStatus(OrderStatus.PARTIAL);
                        orderQueue.poll();
                    }
                }
                else {
                    break;
                }
            }
        }
        catch (Exception e){
            System.out.println("Some exception occurred.");
        }

        return executedOrder;
    }

    /**
     * This comparator arrange the queue in order of ascending price then time.
     *
     * @return Comparator
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

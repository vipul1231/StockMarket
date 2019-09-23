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
            PriorityQueue<Order> sellStockNewQueue = new PriorityQueue<>(returnComparator(0));
            sellStockNewQueue.add(order);
            orderBook.getSellOrders().put(order.getStock(), sellStockNewQueue);
            return;
            }
            sellStockQueue.add(order);
        }
        else {
            PriorityQueue<Order> buyStockQueue = orderBook.getBuyOrders().get(order.getStock());
            if(buyStockQueue == null){
                PriorityQueue<Order> orderQueue = new PriorityQueue<>(returnComparator(1));
                orderQueue.add(order);
                orderBook.getBuyOrders().put(order.getStock(),orderQueue);
                return;
            }
            buyStockQueue.add(order);
        }
    }

    @Override
    public ExecutedOrder verifyAndOrderMatchingBuyRequest(Order buyOrder) {
        PriorityQueue<Order> ordersQueue = orderBook.getSellOrders().get(buyOrder.getStock());
        return processOrder(OrderType.BUY, ordersQueue, buyOrder);

    }

    @Override
    public ExecutedOrder verifyAndOrderMatchingSellRequest(Order sellOrder) {
        PriorityQueue<Order> ordersQueue = orderBook.getBuyOrders().get(sellOrder.getStock());
        return processOrder(OrderType.SELL, ordersQueue, sellOrder);
    }

    /**
     * This method process order w.r.t BUY or SELL
     *
     * @param orderType         order type
     * @param  buyOrSellOrder  buy or a sell order which will be processed.
     * @return                  executed order
     */
    private ExecutedOrder processOrder(OrderType orderType, PriorityQueue<Order> buyOrSellOrderQueue, Order buyOrSellOrder){
        ExecutedOrder executedOrder = new ExecutedOrder(OrderStatus.NOT_PROCESSED);
        Double price = buyOrSellOrder.getPrice();
        Integer quantity = buyOrSellOrder.getQty();
        if(buyOrSellOrderQueue == null || buyOrSellOrderQueue.size() == 0){
            return executedOrder;
        }

        try {
            while(true){
                Order order = buyOrSellOrderQueue.peek();
                if(order!=null && ((orderType == OrderType.BUY && price >= order.getPrice()) ||
                        (orderType == OrderType.SELL && order.getPrice() >= price))){
                    int remaining = quantity - order.getQty();
                    if(remaining == 0){
                        executedOrder.setOrderStatus(OrderStatus.PROCESSED);
                        if(orderType == OrderType.SELL){
                            createProcessedOrder(executedOrder,order.getOrderId(),buyOrSellOrder.getOrderId(),buyOrSellOrder.getQty(), buyOrSellOrder.getPrice());
                        }
                        else {
                            createProcessedOrder(executedOrder,order.getOrderId(),buyOrSellOrder.getOrderId(),buyOrSellOrder.getQty(), order.getPrice());
                        }
                        buyOrSellOrderQueue.poll();
                        break;
                    }
                    else if(remaining < 0){
                        Order clonedOrder = order.clone();
                        clonedOrder.setQty(quantity);
                        if(orderType == OrderType.SELL){
                            createProcessedOrder(executedOrder, buyOrSellOrder.getOrderId(),clonedOrder.getOrderId(),quantity,buyOrSellOrder.getPrice());
                        }
                        else {
                            createProcessedOrder(executedOrder, clonedOrder.getOrderId(), buyOrSellOrder.getOrderId(), clonedOrder.getQty(), clonedOrder.getPrice());
                        }
                        executedOrder.setOrderStatus(OrderStatus.PROCESSED);
                        order.setQty(Math.abs(remaining));
                        break;
                    }
                    else {
                        quantity = remaining;
                        if(orderType == OrderType.SELL){
                            Order clonedOrder = buyOrSellOrder.clone();
                            createProcessedOrder(executedOrder, clonedOrder.getOrderId(),order.getOrderId(), order.getQty(),clonedOrder.getPrice());
                        }
                        else {
                            Order clonedOrder = order.clone();
                            createProcessedOrder(executedOrder, order.getOrderId(), buyOrSellOrder.getOrderId(), clonedOrder.getQty(), clonedOrder.getPrice());
                        }
                        executedOrder.setOrderStatus(OrderStatus.PARTIAL);
                        buyOrSellOrderQueue.poll();
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

    private void createProcessedOrder(ExecutedOrder executedOrder, Integer sellOrderId, Integer buyOrderId, Integer amount, Double price){
        ProcessedOrder processedOrder = new ProcessedOrder(sellOrderId, buyOrderId, amount, price);
        executedOrder.addExecutedOrder(processedOrder);
    }

    /**
     * This comparator arrange the queue in order of ascending price then time.
     *
     * @return Comparator
     */
    private Comparator<Order> returnComparator(int order) {

        if(order == 0){
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
        else {
            return (o1, o2) -> {
                if(o1.getPrice() > o2.getPrice()){
                    return -1;
                }
                else if(o1.getPrice() < o2.getPrice()){
                    return 1;
                }
                else {
                    return o1.getTime().compareTo(o2.getTime());
                }
            };
        }
    }
}

package com.bacq.stockmarket.start;

import com.bacq.stockmarket.domain.ExecutedOrder;
import com.bacq.stockmarket.domain.Order;
import com.bacq.stockmarket.domain.OrderStatus;
import com.bacq.stockmarket.domain.OrderType;
import com.bacq.stockmarket.service.OrderManagement;
import com.bacq.stockmarket.service.impl.OrderManagementImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class StartApp {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    public static void main(String[] args) throws ParseException {
        StartApp startApp = new StartApp();
        Scanner scanner = new Scanner(System.in);
        OrderManagement orderManagement = new OrderManagementImpl();

        while (scanner.hasNext()){
            String[] input = scanner.nextLine().split(" ");

            Order order = startApp.createOrder(input);

            if(order.getOrderType() == OrderType.SELL){
                ExecutedOrder executedOrder = orderManagement.verifyAndOrderMatchingSellRequest(order.getStock(),order.getPrice(), order.getQty());
                startApp.printOrderExecution(executedOrder, order);

                if(executedOrder.getOrderStatus() == OrderStatus.NOT_PROCESSED){
                    orderManagement.addStockOrder(order);
                }

                if(executedOrder.getOrderStatus() == OrderStatus.PARTIAL){
                    int totalExecuted = 0;
                    for(Order order1 : executedOrder.getOrder()){
                        totalExecuted += order1.getQty();
                    }
                    order.setQty(order.getQty() - totalExecuted);
                    orderManagement.addStockOrder(order);
                }
            }
            else {
                ExecutedOrder executedOrder = orderManagement.verifyAndOrderMatchingBuyRequest(order.getStock(), order.getPrice(), order.getQty());
                startApp.printOrderExecution(executedOrder, order);
                if(executedOrder.getOrderStatus() == OrderStatus.NOT_PROCESSED){
                    orderManagement.addStockOrder(order);
                }

                if(executedOrder.getOrderStatus() == OrderStatus.PARTIAL){
                    int totalExecuted = 0;
                    for(Order order1 : executedOrder.getOrder()){
                        totalExecuted += order1.getQty();
                    }
                    order.setQty(order.getQty() - totalExecuted);
                    orderManagement.addStockOrder(order);
                }
            }
        }
    }

    private void printOrderExecution(ExecutedOrder executedOrder, Order order){
        List<Order> ordersList = executedOrder.getOrder();
        for(Order order1 : ordersList){
            System.out.println("#"+order1.getOrderId()+" "+order1.getQty()+" "+order1.getPrice()+" #"+order.getOrderId());
        }
    }

    private Order createOrder(String[] inputs) throws ParseException {
        Order order = new Order();
        order.setOrderId(Integer.parseInt(inputs[0].substring(1)));
        order.setTime(simpleDateFormat.parse(inputs[1]));
        order.setStock(inputs[2]);
        order.setOrderType(inputs[3].equals("sell") ? OrderType.SELL : OrderType.BUY);
        order.setQty(Integer.parseInt(inputs[4]));
        order.setPrice(Double.parseDouble(inputs[5]));
        return order;
    }
}

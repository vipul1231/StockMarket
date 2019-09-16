package com.bacq.stockmarket.helper;

import com.bacq.stockmarket.domain.ExecutedOrder;
import com.bacq.stockmarket.domain.Order;
import com.bacq.stockmarket.domain.OrderStatus;
import com.bacq.stockmarket.domain.OrderType;
import com.bacq.stockmarket.service.OrderManagement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MarketHelper {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    StringBuilder stringBuilder = new StringBuilder();

    /**
     * This method takes the input array and execute the order. It also adds the order if partially executed.
     *
     * @param input             array input
     * @param orderManagement   service object
     * @throws Exception        ohho exception :(
     */
    public void takeAndAddStockToOrderBook(String[] input, OrderManagement orderManagement) throws Exception{
        Order order = createOrder(input);

        if(order.getOrderType() == OrderType.SELL){
            ExecutedOrder executedOrder = orderManagement.verifyAndOrderMatchingSellRequest(order.getStock(),order.getPrice(), order.getQty());
            printOrderExecution(executedOrder, order);

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
            printOrderExecution(executedOrder, order);
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

    /**
     * Print order on console.
     *
     * @param executedOrder executed order
     * @param order         current order
     */
    private void printOrderExecution(ExecutedOrder executedOrder, Order order){
        List<Order> ordersList = executedOrder.getOrder();
        for(Order order1 : ordersList){
            stringBuilder.append("#").append(order1.getOrderId()).append(" ").append(order1.getQty()).append(" ").append(order1.getPrice()).append(" #").append(order.getOrderId()).append("\n");
        }
    }

    public String displayOutput(){
        String output = stringBuilder.toString();
        System.out.println(output);
        return output;
    }

    /**
     * Create order from string array.
     *
     * @param inputs        string array
     * @return              order object
     * @throws ParseException  oops exception :(
     */
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

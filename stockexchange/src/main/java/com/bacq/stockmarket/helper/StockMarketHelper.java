package com.bacq.stockmarket.helper;

import com.bacq.stockmarket.domain.*;
import com.bacq.stockmarket.service.OrderManagement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class StockMarketHelper {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    private StringBuilder stringBuilder = new StringBuilder();

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
           ExecutedOrder executedOrder = orderManagement.verifyAndOrderMatchingSellRequest(order);
           processOutComeOfOrder(executedOrder, order, orderManagement);
        }
        else {
            ExecutedOrder executedOrder = orderManagement.verifyAndOrderMatchingBuyRequest(order);
            processOutComeOfOrder(executedOrder, order, orderManagement);

        }
    }


    /**
     * Print order on console.
     *
     * @param executedOrder executed order
     */
    private void printOrderExecution(ExecutedOrder executedOrder){
        List<ProcessedOrder> ordersList = executedOrder.getOrder();
        for(ProcessedOrder processedOrder : ordersList){
            stringBuilder.append("#").append(processedOrder.getSellOrderId()).append(" ").append(processedOrder.getAmount()).append(" ").append(processedOrder.displayPrice()).append(" #").append(processedOrder.getBuyOrderId()).append("\n");
        }
    }

    public String displayOutput(){
        String output = stringBuilder.toString();
        System.out.println(output);
        return output;
    }

    public void flushOutput(){
        stringBuilder.delete(0, stringBuilder.length());
    }

    private void processOutComeOfOrder(ExecutedOrder executedOrder, Order order, OrderManagement orderManagement){
        printOrderExecution(executedOrder);
        if(executedOrder.getOrderStatus() == OrderStatus.NOT_PROCESSED){
            orderManagement.addStockOrder(order);
        }
        else if(executedOrder.getOrderStatus() == OrderStatus.PARTIAL){
            int totalExecuted = 0;
            for(ProcessedOrder processedOrder : executedOrder.getOrder()){
                totalExecuted += processedOrder.getAmount();
            }
            order.setQty(order.getQty() - totalExecuted);
            orderManagement.addStockOrder(order);
        }
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

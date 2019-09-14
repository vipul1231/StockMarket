package com.bacq.stockmarket.start;

import com.bacq.stockmarket.domain.Order;
import com.bacq.stockmarket.domain.OrderType;
import com.bacq.stockmarket.service.OrderManagement;
import com.bacq.stockmarket.service.impl.OrderManagementImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class StartApp {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    public static void main(String[] args) throws ParseException {
        StartApp startApp = new StartApp();
        Scanner scanner = new Scanner(System.in);

        while (scanner.next() != "\\n"){
            String[] input = scanner.nextLine().split(" ");
            OrderManagement orderManagement = new OrderManagementImpl();
            orderManagement.addStockOrder(startApp.createOrder(input));
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

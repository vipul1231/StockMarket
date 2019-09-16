package com.bacq.stockmarket.start;

import com.bacq.stockmarket.helper.MarketHelper;
import com.bacq.stockmarket.service.OrderManagement;
import com.bacq.stockmarket.service.impl.OrderManagementImpl;

import java.util.Scanner;

public class StartApp {

    public static void main(String[] args) throws Exception {
        MarketHelper marketHelper = new MarketHelper();
        Scanner scanner = new Scanner(System.in);
        OrderManagement orderManagement = new OrderManagementImpl();

        while (true){
            String[] input = scanner.nextLine().split(" ");
            if(input.length == 1){
                break;
            }
            marketHelper.takeAndAddStockToOrderBook(input, orderManagement);
        }
        marketHelper.displayOutput();
    }
}

package com.bacq.stockmarket.domain;

import lombok.Data;

import java.text.DecimalFormat;

@Data
public class ProcessedOrder
{
    private Integer sellOrderId;
    private Integer amount;
    private Double price;
    private Integer buyOrderId;
    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    public String displayPrice(){
        return decimalFormat.format(price);
    }
    public ProcessedOrder(Integer sellOrderId, Integer buyOrderId, Integer amount, Double price){
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.amount = amount;
        this.price = price;
    }
}

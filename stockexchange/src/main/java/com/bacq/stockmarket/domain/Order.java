package com.bacq.stockmarket.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Order implements Cloneable{

    private Integer orderId;
    private Date time;
    private String stock;
    private OrderType orderType;
    private int qty;
    private Double price;

    public Order clone() throws CloneNotSupportedException {
        return (Order) super.clone();
    }
}

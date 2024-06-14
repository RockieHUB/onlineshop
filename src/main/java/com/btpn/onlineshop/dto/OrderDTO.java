package com.btpn.onlineshop.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int orderId;
    private String orderCode;
    private Date orderDate;
    private BigDecimal totalPrice;

    private ItemDTO items;

    private CustomerDTO customers;
}

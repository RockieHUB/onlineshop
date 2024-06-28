package com.onlineshop.dto;

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
public class OrderDTOReport {
    private Integer orderId;
    private String orderCode;
    private Date orderDate;
    private BigDecimal totalPrice;
    private Integer quantity;

    private Integer itemsId;
    private String customerName;
}

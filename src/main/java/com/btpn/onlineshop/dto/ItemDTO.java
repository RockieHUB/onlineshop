package com.btpn.onlineshop.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private int itemsId;
    private String itemsName;
    private String itemsCode;
    private int stock;
    private BigDecimal price;
    private Boolean isAvailable;
}

package com.onlineshop.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private int customerId;

    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private Boolean isActive;
    private Date lastOrderDate;
    private String pic;
}

package com.onlineshop.entities;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @NotBlank(message = "OrderCode tidak boleh kosong")
    private String orderCode;

    @Column(name = "order_date")
    @Temporal(TemporalType.DATE)
    @LastModifiedDate
    private Date orderDate;
    
    @NotNull(message = "total harus diisi")
    private BigDecimal totalPrice;

    @NotNull(message = "QTY harus diisi")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "items_id")
    @NotNull(message = "Item harus diisi")
    private Items items;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @NotNull(message = "Customer harus diisi")
    private Customers customers;
}

package com.btpn.onlineshop.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Customers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    @NotBlank(message = "Nama tidak boleh kosong")
    private String customerName;
    @NotBlank(message = "Alamat tidak boleh kosong")
    private String customerAddress;
    @NotBlank(message = "Nomor Telepon tidak boleh kosong")
    private String customerPhone;
    @NotNull(message = "Status tidak boleh kosong")
    private Boolean isActive;

    @Column(name = "last_order_date")
    @Temporal(TemporalType.DATE)
    @LastModifiedDate
    private Date lastOrderDate;
    
    private String pic;

    @OneToMany(mappedBy = "customers")
    private List<Orders> orderses;
}

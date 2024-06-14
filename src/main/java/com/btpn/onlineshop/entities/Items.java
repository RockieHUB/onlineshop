package com.btpn.onlineshop.entities;

import java.math.BigDecimal;
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
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemsId;

    @NotBlank(message = "Nama tidak boleh kosong")
    private String itemsName;
    @NotBlank(message = "Kode tidak boleh kosong")
    private String itemsCode;
    @NotNull(message = "Stock harus diisi")
    private int stock;
    @NotNull(message = "Harga tidak boleh kosong")
    private BigDecimal price;
    @NotNull(message = "Status ketersediaan tidak boleh kosong")
    private Boolean isAvailable;

    @Column(name = "last_re_stock")
    @Temporal(TemporalType.DATE)
    @LastModifiedDate
    private Date lastReStock;

    @OneToMany(mappedBy = "items")
    private List<Orders> orderses;
}

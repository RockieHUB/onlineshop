package com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineshop.entities.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer>{

}

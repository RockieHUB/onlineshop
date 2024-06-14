package com.btpn.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.btpn.onlineshop.entities.Items;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Integer>{

}

package com.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlineshop.entities.Items;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Integer>{

}

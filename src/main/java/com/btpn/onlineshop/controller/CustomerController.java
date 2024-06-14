package com.btpn.onlineshop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btpn.onlineshop.entities.Customers;
import com.btpn.onlineshop.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/createcustomer")
    public ResponseEntity<Map<String, Object>> createCustomer(@RequestBody Customers customer) {
        log.info("Mengakses Create Customer");
        ResponseEntity<Map<String, Object>> data = customerService.createCustomer(customer);

        return data;
    }

    @GetMapping("/getlist")
    public ResponseEntity<Map<String, Object>> getListCustomers() {
        log.info("Mengakses Get Customers");
        ResponseEntity<Map<String, Object>> data = customerService.getCustomers();

        return data;
    }

    @PutMapping("/updatecustomer/{id}")
    public ResponseEntity<Map<String, Object>> updateCustomer(@PathVariable("id") Integer customerId, @RequestBody Customers customer) {
        log.info("Mengakses Update Customer");
        ResponseEntity<Map<String, Object>> data = customerService.updateCustomer(customerId, customer);

        return data;
    }

    @DeleteMapping("/deletecustomer/{id}")
    public ResponseEntity<Map<String, Object>> deleteItem(@PathVariable("id") Integer customerId) {
        log.info("Mengakses Delete Item");
        ResponseEntity<Map<String, Object>> data = customerService.deleteCustomerById(customerId);

        return data;
    }
}

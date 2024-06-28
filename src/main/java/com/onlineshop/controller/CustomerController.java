package com.onlineshop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onlineshop.entities.Customers;
import com.onlineshop.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping(path = { "/createcustomer" }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createCustomer(@RequestPart("file") MultipartFile gambar,
            @RequestPart("customer") Customers customer) {
        log.info("Mengakses Create Customer");
        log.info(gambar.getOriginalFilename());
        ResponseEntity<Map<String, Object>> data = customerService.createCustomer(gambar, customer);

        return data;
    }

    @GetMapping("/getlist")
    public ResponseEntity<Map<String, Object>> getListCustomers(
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Mengakses Get Customers");
        ResponseEntity<Map<String, Object>> data = customerService.getCustomers(pageable);

        return data;
    }

    @GetMapping("/getlist/{id}")
    public ResponseEntity<Map<String, Object>> getCustomerById(@PathVariable("id") Integer id) {
        log.info("Mengakses Get Customer ID:" + id);
        ResponseEntity<Map<String, Object>> data = customerService.getCustomerById(id);

        return data;
    }

    @PutMapping("/updatecustomer/{id}")
    public ResponseEntity<Map<String, Object>> updateCustomer(@PathVariable("id") Integer customerId,
            @RequestBody Customers customer) {
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

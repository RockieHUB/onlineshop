package com.btpn.onlineshop.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

import lombok.extern.slf4j.Slf4j;

import com.btpn.onlineshop.entities.Items;
import com.btpn.onlineshop.service.ItemService;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping("/createitem")
    public ResponseEntity<Map<String, Object>> createItem(@RequestBody Items items) {
        log.info("Mengakses Create Item");
        ResponseEntity<Map<String, Object>> data = itemService.createItem(items);

        return data;
    }

    @GetMapping("/getlist")
    public ResponseEntity<Map<String, Object>> getListItems(
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Mengakses Get Items");
        ResponseEntity<Map<String, Object>> data = itemService.getItems(pageable);

        return data;
    }

    @GetMapping("/getlist/{id}")
    public ResponseEntity<Map<String, Object>> getItemById(@PathVariable("id") Integer id) {
        log.info("Mengakses Get Item ID:" + id);
        ResponseEntity<Map<String, Object>> data = itemService.getItemById(id);

        return data;
    }

    @PutMapping("/updateitem/{id}")
    public ResponseEntity<Map<String, Object>> updateItem(@PathVariable("id") Integer itemId,
            @RequestBody Items items) {
        log.info("Mengakses Update Item");
        ResponseEntity<Map<String, Object>> data = itemService.updateItem(itemId, items);

        return data;
    }

    @DeleteMapping("/deleteitem/{id}")
    public ResponseEntity<Map<String, Object>> deleteItem(@PathVariable("id") Integer itemId) {
        log.info("Mengakses Delete Item");
        ResponseEntity<Map<String, Object>> data = itemService.deleteItemById(itemId);

        return data;
    }
}

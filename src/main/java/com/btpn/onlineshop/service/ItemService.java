package com.btpn.onlineshop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.btpn.onlineshop.dto.ItemDTO;
import com.btpn.onlineshop.entities.Items;
import com.btpn.onlineshop.repository.ItemsRepository;

import jakarta.transaction.Transactional;
// import lombok.extern.slf4j.Slf4j;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
// @Slf4j
public class ItemService {
    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validator validator;

    @Transactional
    public ResponseEntity<Map<String, Object>> createItem(Items item) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Items temp = Items.builder()
                .itemsName(item.getItemsName())
                .itemsCode(item.getItemsCode())
                .isAvailable(item.getIsAvailable())
                .stock(item.getStock())
                .price(item.getPrice())
            .build();
            
            Set<ConstraintViolation<Items>> violations = validator.validate(temp);
            if (!violations.isEmpty()) {
                // If there are validation errors, collect them and return a BAD_REQUEST response
                result.put("message ", "Item gagal ditambahkan");
                for (ConstraintViolation<Items> violation : violations) {
                    result.put(violation.getPropertyPath().toString(), violation.getMessage());
                }
                result.put("statusCode", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            itemsRepository.save(temp);

            result.put("message ", "Item berhasil ditambahkan");
            result.put("statusCode", HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.put("message ", "Item gagal ditambahkan");
            result.put("exception ", e.getCause());
            result.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<String, Object>> getItems() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<Items> data = itemsRepository.findAll();
            if (!data.isEmpty()) {
                List<ItemDTO> allItems = data.stream()
                        .map(item -> modelMapper.map(item, ItemDTO.class))
                        .collect(Collectors.toList());

                result.put("data", allItems);
                result.put("total_data", allItems.size());
                result.put("message ", "Items Berhasil dibaca");
                result.put("statusCode", HttpStatus.OK.value());
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.put("data", data);
                result.put("message ", "Items Gagal dibaca");
                result.put("statusCode", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result.put("message ", "Items Gagal dibaca");
            result.put("exception ", e.getCause());
            result.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> updateItem(Integer id, Items item) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Optional<Items> temp_data = itemsRepository.findById(id);
            if (temp_data.isPresent()) {
                Items toup = temp_data.get();
                toup.setItemsName(item.getItemsName());
                toup.setItemsCode(item.getItemsCode());
                toup.setIsAvailable(item.getIsAvailable());
                toup.setStock(item.getStock());
                toup.setPrice(item.getPrice());

                Set<ConstraintViolation<Items>> violations = validator.validate(toup);
                if (!violations.isEmpty()) {
                    // If there are validation errors, collect them and return a BAD_REQUEST response
                    result.put("message ", "Item gagal diperbarui");
                    for (ConstraintViolation<Items> violation : violations) {
                        result.put(violation.getPropertyPath().toString(), violation.getMessage());
                    }
                    result.put("statusCode", HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                }
                itemsRepository.save(toup);

                result.put("message ", "Item berhasil diperbarui");
                result.put("statusCode", HttpStatus.OK.value());
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.put("message ", "Item gagal diperbarui");
                result.put("statusCode", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result.put("message ", "Item gagal ditambahkan");
            result.put("exception ", e.getCause());
            result.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> deleteItemById(Integer id) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Optional<Items> temp_data = itemsRepository.findById(id);
            if (temp_data.isPresent()) {
                itemsRepository.delete(temp_data.get());

                result.put("message ", "Item berhasil dihapus");
                result.put("statusCode", HttpStatus.OK.value());
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.put("message ", "Item gagal dihapus");
                result.put("statusCode", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result.put("message ", "Item gagal dihapus");
            result.put("exception ", e.getCause());
            result.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

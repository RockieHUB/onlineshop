package com.btpn.onlineshop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.btpn.onlineshop.config.MinioHelper;
import com.btpn.onlineshop.dto.CustomerDTO;
import com.btpn.onlineshop.entities.Customers;
import com.btpn.onlineshop.repository.CustomersRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
// @Slf4j
public class CustomerService {
    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validator validator;

    @Autowired
    private MinioHelper minioHelper;

    private static final HashMap<Integer, String> objectMap = new HashMap<>() {
        {
            put(1, "Anjing.jpg");
            put(2, "Berang-berang.jpg");
            put(3, "Kelinci.jpg");
            put(4, "Kucing.jpg");
            put(5, "Monyet.jpg");
            put(6, "Panda.jpg");
        }
    };

    public String getRandomObject() {
        List<String> values = new ArrayList<>(objectMap.values());
        Random rand = new Random();
        return values.get(rand.nextInt(values.size()));
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> createCustomer(Customers customer) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String obj = getRandomObject();
            Customers temp = Customers.builder()
                    .customerName(customer.getCustomerName())
                    .customerAddress(customer.getCustomerAddress())
                    .customerPhone(customer.getCustomerPhone())
                    .isActive(customer.getIsActive())
                    .pic(obj)
                    .build();

            Set<ConstraintViolation<Customers>> violations = validator.validate(temp);
            if (!violations.isEmpty()) {
                // If there are validation errors, collect them and return a BAD_REQUEST
                // response
                result.put("message ", "Customer gagal ditambahkan");
                for (ConstraintViolation<Customers> violation : violations) {
                    result.put(violation.getPropertyPath().toString(), violation.getMessage());
                }
                result.put("statusCode", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            customersRepository.save(temp);

            result.put("message ", "Customer berhasil ditambahkan");
            result.put("statusCode", HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.put("message ", "Customer gagal ditambahkan");
            result.put("exception ", e.getCause());
            result.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<String, Object>> getCustomers() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<Customers> data = customersRepository.findAll();
            if (!data.isEmpty()) {
                List<CustomerDTO> allCustomers = data.stream()
                        .map(customer -> {
                            CustomerDTO dto = modelMapper.map(customer, CustomerDTO.class);

                            String url = minioHelper.getPresignedUrl(customer.getPic(), 3);
                            dto.setPic(url);

                            return dto;
                        })
                        .collect(Collectors.toList());

                result.put("data", allCustomers);
                result.put("total_data", allCustomers.size());
                result.put("message ", "Customers Berhasil dibaca");
                result.put("statusCode", HttpStatus.OK.value());
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.put("data", data);
                result.put("message ", "Customers Gagal dibaca");
                result.put("statusCode", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result.put("message ", "Customers Gagal dibaca");
            result.put("exception ", e.getCause());
            result.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> updateCustomer(Integer id, Customers customer) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Optional<Customers> temp_data = customersRepository.findById(id);
            if (temp_data.isPresent()) {
                Customers toup = temp_data.get();
                toup.setCustomerName(customer.getCustomerName());
                toup.setCustomerAddress(customer.getCustomerAddress());
                toup.setCustomerPhone(customer.getCustomerPhone());
                toup.setIsActive(customer.getIsActive());
                if (customer.getPic() != null){
                    toup.setPic(customer.getPic());
                }

                Set<ConstraintViolation<Customers>> violations = validator.validate(toup);
                if (!violations.isEmpty()) {
                    // If there are validation errors, collect them and return a BAD_REQUEST
                    // response
                    result.put("message ", "Customer gagal diperbarui");
                    for (ConstraintViolation<Customers> violation : violations) {
                        result.put(violation.getPropertyPath().toString(), violation.getMessage());
                    }
                    result.put("statusCode", HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                }
                customersRepository.save(toup);

                result.put("message ", "Customer berhasil diperbarui");
                result.put("statusCode", HttpStatus.OK.value());
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.put("message ", "Customer gagal diperbarui");
                result.put("statusCode", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result.put("message ", "Customer gagal ditambahkan");
            result.put("exception ", e.getCause());
            result.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> deleteCustomerById(Integer id) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Optional<Customers> temp_data = customersRepository.findById(id);
            if (temp_data.isPresent()) {
                customersRepository.delete(temp_data.get());

                result.put("message ", "customer berhasil dihapus");
                result.put("statusCode", HttpStatus.OK.value());
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.put("message ", "customer gagal dihapus");
                result.put("statusCode", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result.put("message ", "customer gagal dihapus");
            result.put("exception ", e.getCause());
            result.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

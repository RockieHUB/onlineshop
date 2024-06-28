package com.btpn.onlineshop.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RestController;

import com.btpn.onlineshop.entities.Orders;
import com.btpn.onlineshop.service.OrderService;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/createorder")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Orders order) {
        log.info("Mengakses Create Order");
        ResponseEntity<Map<String, Object>> data = orderService.createOrder(order);

        return data;
    }

    @GetMapping("/getlist")
    public ResponseEntity<Map<String, Object>> getListOrder(
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable pageable) {

        ResponseEntity<Map<String, Object>> data = orderService.getOrders(pageable);

        return data;
    }

    // @GetMapping("order-report/{format}")
    // public ResponseEntity<Resource> getOrderReport(@PathVariable String format)
    // throws JRException, IOException {
    // log.info("Mengakses Get Orders Report");

    // byte[] reportContent = orderService.getOrderReport(format);

    // ByteArrayResource resource = new ByteArrayResource(reportContent);
    // return ResponseEntity.ok()
    // .contentType(MediaType.APPLICATION_OCTET_STREAM)
    // .contentLength(resource.contentLength())
    // .header(HttpHeaders.CONTENT_DISPOSITION,
    // ContentDisposition.attachment()
    // .filename("order-report." + format)
    // .build().toString())
    // .body(resource);
    // }

    @GetMapping("/order-report/{format}")
    public ResponseEntity<Resource> getOrderReport(@PathVariable String format) throws JRException, IOException {
        log.info("Accessing Get Orders Report");

        byte[] reportContent = orderService.getOrderReport(format);

        ByteArrayResource resource = new ByteArrayResource(reportContent);

        MediaType contentType;
        String fileExtension;

        switch (format.toLowerCase()) {
            case "pdf":
                contentType = MediaType.APPLICATION_PDF;
                fileExtension = "pdf";
                break;
            case "xlsx":
                contentType = MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                fileExtension = "xlsx";
                break;
            default:
                throw new IllegalArgumentException("Invalid report format: " + format);
        }

        return ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("order-report." + fileExtension)
                                .build().toString())
                .body(resource);
    }

    @PutMapping("/updateorder/{id}")
    public ResponseEntity<Map<String, Object>> updateOrder(@PathVariable("id") Integer orderId,
            @RequestBody Orders order) {
        log.info("Mengakses Update Order");
        ResponseEntity<Map<String, Object>> data = orderService.updateOrder(orderId, order);

        return data;
    }

    @DeleteMapping("/deleteorder/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable("id") Integer orderId) {
        log.info("Mengakses Delete Order");
        ResponseEntity<Map<String, Object>> data = orderService.deleteOrderById(orderId);

        return data;
    }
}

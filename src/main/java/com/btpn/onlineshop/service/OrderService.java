package com.btpn.onlineshop.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.btpn.onlineshop.dto.OrderDTO;
import com.btpn.onlineshop.dto.OrderDTOReport;
import com.btpn.onlineshop.entities.Orders;
import com.btpn.onlineshop.repository.OrdersRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class OrderService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validator validator;

    @Transactional
    public ResponseEntity<Map<String, Object>> createOrder(Orders order) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Orders temp = Orders.builder()
                    .orderCode(order.getOrderCode())
                    .totalPrice(order.getTotalPrice())
                    .quantity(order.getQuantity())
                    .items(order.getItems())
                    .customers(order.getCustomers())
                    .build();

            Set<ConstraintViolation<Orders>> violations = validator.validate(temp);
            if (!violations.isEmpty()) {
                // If there are validation errors, collect them and return a BAD_REQUEST
                // response
                result.put("message ", "Order gagal ditambahkan");
                for (ConstraintViolation<Orders> violation : violations) {
                    result.put(violation.getPropertyPath().toString(), violation.getMessage());
                }
                result.put("statusCode", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            ordersRepository.save(temp);

            result.put("message ", "Order berhasil ditambahkan");
            result.put("statusCode", HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.put("message ", "Order gagal ditambahkan");
            result.put("exception ", e.getCause());
            result.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<String, Object>> getOrders() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<Orders> data = ordersRepository.findAll();
            if (!data.isEmpty()) {
                List<OrderDTO> allOrders = data.stream()
                        .map(order -> modelMapper.map(order, OrderDTO.class))
                        .collect(Collectors.toList());

                result.put("data", allOrders);
                result.put("total_data", allOrders.size());
                result.put("message ", "Orders Berhasil dibaca");
                result.put("statusCode", HttpStatus.OK.value());
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.put("data", data);
                result.put("message ", "Orders Gagal dibaca");
                result.put("statusCode", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result.put("message ", "Orders Gagal dibaca");
            result.put("exception ", e.getCause());
            result.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // public byte[] getOrderReport(String format) {
    // JasperReport jasperReport;

    // List<Orders> data = ordersRepository.findAll();
    // List<OrderDTOReport> allOrders = data.stream()
    // .map(order -> modelMapper.map(order, OrderDTOReport.class))
    // .collect(Collectors.toList());

    // try {
    // jasperReport = (JasperReport)
    // JRLoader.loadObject(ResourceUtils.getFile("order_report.jasper"));
    // } catch (FileNotFoundException | JRException e) {
    // try {
    // File file = ResourceUtils.getFile("classpath:order_report.jrxml");
    // jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
    // JRSaver.saveObject(jasperReport, "order_report.jasper");
    // } catch (FileNotFoundException | JRException ex) {
    // throw new RuntimeException(e);
    // }
    // }

    // JRBeanCollectionDataSource dataSource = new
    // JRBeanCollectionDataSource(allOrders);
    // Map<String, Object> parameters = new HashMap<>();
    // // parameters.put("title", "Item Report");
    // JasperPrint jasperPrint = null;
    // byte[] reportContent;

    // try {
    // jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
    // dataSource);
    // switch (format) {
    // case "pdf" -> reportContent =
    // JasperExportManager.exportReportToPdf(jasperPrint);
    // case "xml" -> reportContent =
    // JasperExportManager.exportReportToXml(jasperPrint).getBytes();
    // default -> throw new RuntimeException("Unknown report format");
    // }
    // } catch (JRException e) {
    // throw new RuntimeException(e);
    // }
    // return reportContent;
    // }
    public byte[] getOrderReport(String format) throws JRException, IOException {

        // 1. Load the Jasper Report Efficiently
        JasperReport jasperReport;
        try (InputStream jasperStream = getClass().getResourceAsStream("/order_report.jasper")) {
            if (jasperStream == null) {
                // If not found, compile the .jrxml
                try (InputStream jrxmlStream = getClass().getResourceAsStream("/order_report.jrxml")) {
                    jasperReport = JasperCompileManager.compileReport(jrxmlStream);
                    // Optionally, save the compiled report to avoid recompilation next time
                    JRSaver.saveObject(jasperReport, "order_report.jasper");
                }
            } else {
                jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
            }
        }

        // 2. Fetch and Map Orders (No Changes Needed)
        List<Orders> data = ordersRepository.findAll();
        List<OrderDTOReport> allOrders = data.stream()
                .map(order -> modelMapper.map(order, OrderDTOReport.class))
                .collect(Collectors.toList());

        // 3. Fill and Export the Report
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(allOrders);
        Map<String, Object> parameters = new HashMap<>();
        // parameters.put("title", "Item Report");

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) { // Auto-closeable
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            switch (format.toLowerCase()) {
                case "pdf":
                    JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                    break;
                case "xlsx":
                    JRXlsxExporter exporter = new JRXlsxExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                    exporter.exportReport(); // No need for getBytes() here
                    break;
                default:
                    throw new IllegalArgumentException("Unknown report format");
            }
            return outputStream.toByteArray();
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> updateOrder(Integer id, Orders order) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Optional<Orders> temp_data = ordersRepository.findById(id);
            if (temp_data.isPresent()) {
                Orders toup = temp_data.get();
                toup.setOrderCode(order.getOrderCode());
                toup.setTotalPrice(order.getTotalPrice());
                toup.setQuantity(order.getQuantity());
                if (order.getCustomers() != null) {
                    toup.setCustomers(order.getCustomers());
                }
                if (order.getItems() != null) {
                    toup.setItems(order.getItems());
                }

                Set<ConstraintViolation<Orders>> violations = validator.validate(toup);
                if (!violations.isEmpty()) {
                    // If there are validation errors, collect them and return a BAD_REQUEST
                    // response
                    result.put("message ", "Order gagal diperbarui");
                    for (ConstraintViolation<Orders> violation : violations) {
                        result.put(violation.getPropertyPath().toString(), violation.getMessage());
                    }
                    result.put("statusCode", HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                }
                ordersRepository.save(toup);

                result.put("message ", "Order berhasil diperbarui");
                result.put("statusCode", HttpStatus.OK.value());
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.put("message ", "Order gagal diperbarui");
                result.put("statusCode", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result.put("message ", "Order gagal ditambahkan");
            result.put("exception ", e.getCause());
            result.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> deleteOrderById(Integer id) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Optional<Orders> temp_data = ordersRepository.findById(id);
            if (temp_data.isPresent()) {
                ordersRepository.delete(temp_data.get());

                result.put("message ", "Order berhasil dihapus");
                result.put("statusCode", HttpStatus.OK.value());
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.put("message ", "Order gagal dihapus");
                result.put("statusCode", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result.put("message ", "Order gagal dihapus");
            result.put("exception ", e.getCause());
            result.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

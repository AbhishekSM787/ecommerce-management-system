package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderRequest;
import com.example.ecommerce.model.CustomerOrder;
import com.example.ecommerce.repository.CustomerOrderRepository;
import com.example.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    private final CustomerOrderRepository orders;
    public OrderController(OrderService service, CustomerOrderRepository orders) {
        this.service = service; this.orders = orders;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody OrderRequest request) {
        try {
            CustomerOrder order = service.place(request);
            return ResponseEntity.ok(Map.of("orderId", order.getId(), "total", order.getTotal(),
                    "customerName", order.getCustomerName(), "createdAt", order.getCreatedAt()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return orders.findById(id).<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

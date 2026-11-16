package com.example.ecommerce.controller;

import com.example.ecommerce.dto.*;
import com.example.ecommerce.model.CustomerOrder;
import com.example.ecommerce.repository.CustomerOrderRepository;
import com.example.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    private final CustomerOrderRepository orders;
    public OrderController(OrderService service, CustomerOrderRepository orders) { this.service = service; this.orders = orders; }

    @Transactional
    @PostMapping public ResponseEntity<?> create(@Valid @RequestBody OrderRequest request, Authentication auth) {
        try { return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(service.place(request, auth.getName()))); }
        catch (IllegalArgumentException ex) { return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage())); }
    }
    @Transactional(readOnly = true)
    @GetMapping("/my") public List<OrderResponse> mine(Authentication auth) {
        return orders.findByUserEmailOrderByCreatedAtDesc(auth.getName()).stream().map(OrderResponse::from).toList();
    }
}

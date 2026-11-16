package com.example.ecommerce.controller;

import com.example.ecommerce.dto.*;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final ProductRepository products;
    private final CustomerOrderRepository orders;
    private final UserRepository users;
    public AdminController(ProductRepository products, CustomerOrderRepository orders, UserRepository users) {
        this.products = products; this.orders = orders; this.users = users;
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        var allOrders = orders.findAll();
        var revenue = allOrders.stream().map(CustomerOrder::getTotal).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        return Map.of("products", products.count(), "users", users.count(), "orders", orders.count(), "revenue", revenue);
    }
    @GetMapping("/products") public List<Product> allProducts() { return products.findAll(); }
    @PostMapping("/products") public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest request) {
        Product p = new Product(request.name(), request.description(), request.price().doubleValue(),
                request.category(), request.stock(), request.imageUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(products.save(p));
    }
    @PutMapping("/products/{id}") public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        var existing = products.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        Product p = existing.get();
        p.update(request.name(), request.description(), request.price(), request.category(), request.stock(), request.imageUrl());
        return ResponseEntity.ok(products.save(p));
    }
    @DeleteMapping("/products/{id}") public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        if (!products.existsById(id)) return ResponseEntity.notFound().build();
        products.deleteById(id); return ResponseEntity.noContent().build();
    }
    @Transactional(readOnly = true)
    @GetMapping("/orders") public List<OrderResponse> allOrders() {
        return orders.findAllByOrderByCreatedAtDesc().stream().map(OrderResponse::from).toList();
    }
    @Transactional
    @PatchMapping("/orders/{id}/status") public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String,String> body) {
        String status = body.getOrDefault("status", "");
        if (!Set.of("PLACED","PROCESSING","SHIPPED","DELIVERED","CANCELLED").contains(status))
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid order status"));
        return orders.findById(id).map(order -> {
            order.updateStatus(status); orders.save(order);
            return ResponseEntity.ok(OrderResponse.from(order));
        }).orElse(ResponseEntity.notFound().build());
    }
}

package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository repository;
    public ProductController(ProductRepository repository) { this.repository = repository; }

    @GetMapping
    public List<Product> all(@RequestParam(required = false) String search) {
        return (search == null || search.isBlank())
                ? repository.findAll() : repository.findByNameContainingIgnoreCase(search.trim());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> one(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

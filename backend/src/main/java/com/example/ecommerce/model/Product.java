package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150) private String name;
    @Column(nullable = false, length = 1000) private String description;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal price;
    @Column(nullable = false, length = 80) private String category;
    @Column(nullable = false) private Integer stock;
    @Column(length = 1000) private String imageUrl;

    protected Product() {}
    public Product(String name, String description, double price, String category, int stock, String imageUrl) {
        this.name = name; this.description = description; this.price = BigDecimal.valueOf(price);
        this.category = category; this.stock = stock; this.imageUrl = imageUrl;
    }
    public void update(String name, String description, BigDecimal price, String category, Integer stock, String imageUrl) {
        this.name = name; this.description = description; this.price = price;
        this.category = category; this.stock = stock; this.imageUrl = imageUrl;
    }
    public void reduceStock(int quantity) { this.stock -= quantity; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getCategory() { return category; }
    public Integer getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }
}

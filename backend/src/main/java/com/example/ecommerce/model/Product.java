package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 1000) private String description;
    private BigDecimal price;
    private String category;
    private Integer stock;
    @Column(length = 1000) private String imageUrl;

    protected Product() {}
    public Product(String name, String description, double price, String category, int stock, String imageUrl) {
        this.name = name; this.description = description; this.price = BigDecimal.valueOf(price);
        this.category = category; this.stock = stock; this.imageUrl = imageUrl;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getCategory() { return category; }
    public Integer getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }
}

package com.example.ecommerce;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    CommandLineRunner seedProducts(ProductRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Product("Wireless Headphones", "Bluetooth headphones with cushioned ear cups.", 2499.00, "Electronics", 25,
                    "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800"));
                repository.save(new Product("Smart Watch", "Everyday smartwatch with activity tracking.", 3999.00, "Electronics", 18,
                    "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800"));
                repository.save(new Product("Backpack", "Lightweight backpack for work and travel.", 1499.00, "Accessories", 30,
                    "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=800"));
                repository.save(new Product("Running Shoes", "Comfortable shoes for daily training.", 2999.00, "Fashion", 15,
                    "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800"));
                repository.save(new Product("Desk Lamp", "Minimal LED desk lamp for your workspace.", 1199.00, "Home", 22,
                    "https://images.unsplash.com/photo-1507457379470-08b800bebc67?w=800"));
                repository.save(new Product("Coffee Mug", "Ceramic mug for your favourite coffee.", 399.00, "Home", 50,
                    "https://images.unsplash.com/photo-1514228742587-6b1558f\xa0?auto=format&w=800".replace("\u00a0", "")));
            }
        };
    }
}

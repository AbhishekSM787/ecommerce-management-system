package com.example.ecommerce;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    CommandLineRunner seedData(ProductRepository products, UserRepository users,
                               PasswordEncoder encoder,
                               @Value("${app.admin.email}") String adminEmail,
                               @Value("${app.admin.password}") String adminPassword) {
        return args -> {
            if (products.count() == 0) {
                products.save(new Product("Wireless Headphones", "Bluetooth headphones with cushioned ear cups.", 2499.00, "Electronics", 25,
                        "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800"));
                products.save(new Product("Smart Watch", "Everyday smartwatch with activity tracking.", 3999.00, "Electronics", 18,
                        "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800"));
                products.save(new Product("Backpack", "Lightweight backpack for work and travel.", 1499.00, "Accessories", 30,
                        "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=800"));
                products.save(new Product("Running Shoes", "Comfortable shoes for daily training.", 2999.00, "Fashion", 15,
                        "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800"));
                products.save(new Product("Desk Lamp", "Minimal LED desk lamp for your workspace.", 1199.00, "Home", 22,
                        "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=800"));
            }
            if (!users.existsByEmailIgnoreCase(adminEmail)) {
                users.save(new User("Administrator", adminEmail, encoder.encode(adminPassword), "ADMIN"));
            }
        };
    }
}

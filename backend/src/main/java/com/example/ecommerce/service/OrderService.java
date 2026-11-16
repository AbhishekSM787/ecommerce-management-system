package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderRequest;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class OrderService {
    private final ProductRepository products;
    private final CustomerOrderRepository orders;
    private final UserRepository users;
    public OrderService(ProductRepository products, CustomerOrderRepository orders, UserRepository users) {
        this.products = products; this.orders = orders; this.users = users;
    }
    @Transactional
    public CustomerOrder place(OrderRequest request, String email) {
        User user = users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BigDecimal total = BigDecimal.ZERO;
        java.util.List<OrderItem> requestedItems = new java.util.ArrayList<>();

        for (OrderRequest.ItemRequest requested : request.items()) {
            Product product = products.findById(requested.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + requested.productId()));
            if (product.getStock() < requested.quantity()) {
                throw new IllegalArgumentException("Insufficient stock for " + product.getName());
            }
            product.reduceStock(requested.quantity());
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(requested.quantity())));
            requestedItems.add(new OrderItem(product, requested.quantity()));
        }

        CustomerOrder order = new CustomerOrder(user, total);
        requestedItems.forEach(order::addItem);
        return orders.save(order);
    }
}

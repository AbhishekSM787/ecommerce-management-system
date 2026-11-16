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

    public OrderService(ProductRepository products, CustomerOrderRepository orders) {
        this.products = products; this.orders = orders;
    }

    @Transactional
    public CustomerOrder place(OrderRequest request) {
        BigDecimal total = BigDecimal.ZERO;
        CustomerOrder order = new CustomerOrder(request.customerName(), request.customerEmail(), total);

        for (OrderRequest.ItemRequest requested : request.items()) {
            Product product = products.findById(requested.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + requested.productId()));
            if (product.getStock() < requested.quantity()) {
                throw new IllegalArgumentException("Insufficient stock for " + product.getName());
            }
            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(requested.quantity()));
            total = total.add(lineTotal);
            order.addItem(new OrderItem(product.getId(), product.getName(), requested.quantity(), product.getPrice()));
        }
        // Persist the calculated total on the order.
        CustomerOrder completed = new CustomerOrder(request.customerName(), request.customerEmail(), total);
        for (OrderRequest.ItemRequest requested : request.items()) {
            Product product = products.findById(requested.productId()).orElseThrow();
            completed.addItem(new OrderItem(product.getId(), product.getName(), requested.quantity(), product.getPrice()));
        }
        return orders.save(completed);
    }
}

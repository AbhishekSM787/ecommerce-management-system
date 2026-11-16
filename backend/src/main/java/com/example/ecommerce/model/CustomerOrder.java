package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_orders")
public class CustomerOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String customerEmail;
    private BigDecimal total;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected CustomerOrder() {}
    public CustomerOrder(String customerName, String customerEmail, BigDecimal total) {
        this.customerName = customerName; this.customerEmail = customerEmail;
        this.total = total; this.createdAt = LocalDateTime.now();
    }
    public void addItem(OrderItem item) { items.add(item); item.setOrder(this); }
    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public BigDecimal getTotal() { return total; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<OrderItem> getItems() { return items; }
}

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
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal total;
    @Column(nullable = false, length = 30) private String status;
    @Column(nullable = false) private LocalDateTime createdAt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected CustomerOrder() {}
    public CustomerOrder(User user, BigDecimal total) {
        this.user = user; this.total = total; this.status = "PLACED"; this.createdAt = LocalDateTime.now();
    }
    public void addItem(OrderItem item) { items.add(item); item.setOrder(this); }
    public void updateStatus(String status) { this.status = status; }
    public Long getId() { return id; }
    public User getUser() { return user; }
    public BigDecimal getTotal() { return total; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<OrderItem> getItems() { return items; }
}

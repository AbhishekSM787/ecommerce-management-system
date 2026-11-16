package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "order_id")
    private CustomerOrder order;

    protected OrderItem() {}
    public OrderItem(Long productId, String productName, Integer quantity, BigDecimal unitPrice) {
        this.productId = productId; this.productName = productName;
        this.quantity = quantity; this.unitPrice = unitPrice;
    }
    public void setOrder(CustomerOrder order) { this.order = order; }
    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
}

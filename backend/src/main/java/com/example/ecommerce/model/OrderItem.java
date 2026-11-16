package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "order_id", nullable = false)
    private CustomerOrder order;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id")
    private Product product;
    @Column(nullable = false, length = 150) private String productName;
    @Column(nullable = false) private Integer quantity;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal unitPrice;

    protected OrderItem() {}
    public OrderItem(Product product, int quantity) {
        this.product = product; this.productName = product.getName();
        this.quantity = quantity; this.unitPrice = product.getPrice();
    }
    public void setOrder(CustomerOrder order) { this.order = order; }
    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
}

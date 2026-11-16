package com.example.ecommerce.dto;
import com.example.ecommerce.model.CustomerOrder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
public record OrderResponse(Long orderId, String customerName, String customerEmail,
                            BigDecimal total, String status, LocalDateTime createdAt,
                            List<Item> items) {
    public record Item(String productName, Integer quantity, BigDecimal unitPrice) {}
    public static OrderResponse from(CustomerOrder order) {
        return new OrderResponse(order.getId(), order.getUser().getName(), order.getUser().getEmail(),
            order.getTotal(), order.getStatus(), order.getCreatedAt(),
            order.getItems().stream().map(i -> new Item(i.getProductName(), i.getQuantity(), i.getUnitPrice())).toList());
    }
}

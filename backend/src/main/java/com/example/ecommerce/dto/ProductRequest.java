package com.example.ecommerce.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record ProductRequest(@NotBlank @Size(max=150) String name,
                             @NotBlank @Size(max=1000) String description,
                             @NotNull @DecimalMin("0.01") BigDecimal price,
                             @NotBlank @Size(max=80) String category,
                             @NotNull @Min(0) Integer stock,
                             @Size(max=1000) String imageUrl) {}

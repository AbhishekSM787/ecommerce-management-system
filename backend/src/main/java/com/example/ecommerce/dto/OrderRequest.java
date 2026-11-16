package com.example.ecommerce.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
public record OrderRequest(@NotEmpty List<@Valid ItemRequest> items) {
    public record ItemRequest(@NotNull Long productId, @NotNull @Min(1) @Max(50) Integer quantity) {}
}

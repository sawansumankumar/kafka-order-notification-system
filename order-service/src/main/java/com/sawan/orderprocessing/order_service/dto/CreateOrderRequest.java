package com.sawan.orderprocessing.order_service.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor

public class CreateOrderRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 225, message = "Product name must not exceed 225 characters")
    private String productName;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.01", message = "Unit price must be at least 0.01")
    @Digits(integer = 10, fraction = 2,
            message = "Unit price must have at most 10 integer digits and 2 decimal places")
    private BigDecimal unitPrice;
}

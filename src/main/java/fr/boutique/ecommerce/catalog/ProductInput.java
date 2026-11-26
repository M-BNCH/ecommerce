package fr.boutique.ecommerce.catalog;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

record ProductInput(
        @NotBlank @Size(max = 120) String name,
        @NotNull @PositiveOrZero @Digits(integer = 12, fraction = 2) BigDecimal price,
        @NotNull @PositiveOrZero Integer stockQuantity) {}

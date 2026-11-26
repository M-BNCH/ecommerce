package fr.boutique.ecommerce.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;
import java.util.UUID;

record CartPatch(@NotEmpty List<@NotNull @Valid Item> items) {
    record Item(@NotNull UUID productId, @NotNull @PositiveOrZero Integer quantity) {}
}

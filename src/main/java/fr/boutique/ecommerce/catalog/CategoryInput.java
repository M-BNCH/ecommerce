package fr.boutique.ecommerce.catalog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

record CategoryInput(
        @NotBlank @Size(max = 120) String name,
        @NotNull @Size(max = 2000) String description) {}

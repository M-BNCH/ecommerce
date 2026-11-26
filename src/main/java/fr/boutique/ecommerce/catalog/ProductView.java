package fr.boutique.ecommerce.catalog;

import java.math.BigDecimal;
import java.util.UUID;

record ProductView(UUID id, String name, BigDecimal price, int stockQuantity, UUID categoryId) {}

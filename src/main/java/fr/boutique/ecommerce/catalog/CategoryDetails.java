package fr.boutique.ecommerce.catalog;

import java.util.List;
import java.util.UUID;

record CategoryDetails(
        UUID id, String name, String description, UUID parentId,
        List<CategoryView> subcategories, List<ProductView> products) {}

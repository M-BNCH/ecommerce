package fr.boutique.ecommerce.catalog;

import java.util.UUID;

record CategoryView(UUID id, String name, String description, UUID parentId) {}

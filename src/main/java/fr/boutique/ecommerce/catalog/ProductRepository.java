package fr.boutique.ecommerce.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findAllByOrderByNameAscIdAsc();
    List<Product> findByCategory_IdOrderByNameAscIdAsc(UUID id);
}

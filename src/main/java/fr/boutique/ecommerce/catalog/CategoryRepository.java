package fr.boutique.ecommerce.catalog;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByParentIsNullOrderByNameAscIdAsc();
    List<Category> findByParent_IdOrderByNameAscIdAsc(UUID id);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Category> findForMoveById(UUID id);
}

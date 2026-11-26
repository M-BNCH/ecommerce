package fr.boutique.ecommerce.catalog;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import static fr.boutique.ecommerce.common.ApiException.conflict;
import static fr.boutique.ecommerce.common.ApiException.notFound;

@Service
@Transactional(readOnly = true)
public class CatalogService {

    private final CategoryRepository categories;
    private final ProductRepository products;

    CatalogService(CategoryRepository categories, ProductRepository products) {
        this.categories = categories;
        this.products = products;
    }

    List<CategoryView> roots() {
        return categories.findByParentIsNullOrderByNameAscIdAsc().stream().map(Category::view).toList();
    }

    CategoryDetails categoryDetails(UUID id) {
        CategoryView category = category(id).view();
        var children = categories.findByParent_IdOrderByNameAscIdAsc(id).stream().map(Category::view).toList();
        var contents = products.findByCategory_IdOrderByNameAscIdAsc(id).stream().map(Product::view).toList();
        return new CategoryDetails(id, category.name(), category.description(), category.parentId(), children, contents);
    }

    @Transactional
    CategoryView createCategory(CategoryInput input) {
        return categories.save(new Category(input)).view();
    }

    @Transactional
    CategoryView updateCategory(UUID id, CategoryInput input) {
        Category category = category(id);
        category.update(input);
        return category.view();
    }

    @Transactional
    void setParent(UUID id, UUID parentId) {

        Category child = categoryForMove(id);
        for (UUID ancestor = parentId; ancestor != null; ancestor = categoryForMove(ancestor).parentId()) {
            if (id.equals(ancestor)) {
                throw conflict("category_cycle", "Ce rattachement créerait un cycle.");
            }
        }
        child.setParent(parentId == null ? null : categories.getReferenceById(parentId));
    }

    @Transactional
    void deleteCategory(UUID id) {
        categories.delete(category(id));
        try {
            categories.flush();
        } catch (DataIntegrityViolationException referenced) {
            throw conflict("category_not_empty", "La catégorie contient encore des produits ou des sous-catégories.");
        }
    }

    List<ProductView> products() {
        return products.findAllByOrderByNameAscIdAsc().stream().map(Product::view).toList();
    }

    ProductView product(UUID id) {
        return requireProduct(id).view();
    }

    public List<Product> findProducts(Collection<UUID> ids) {
        return products.findAllById(ids);
    }

    @Transactional
    ProductView createProduct(ProductInput input) {
        return products.save(new Product(input)).view();
    }

    @Transactional
    ProductView updateProduct(UUID id, ProductInput input) {
        Product product = requireProduct(id);
        product.update(input);
        return product.view();
    }

    @Transactional
    void setProductCategory(UUID id, UUID categoryId) {
        requireProduct(id).setCategory(categoryId == null ? null : category(categoryId));
    }

    @Transactional
    void deleteProduct(UUID id) {
        products.delete(requireProduct(id));
        try {
            products.flush();
        } catch (DataIntegrityViolationException referenced) {
            throw conflict("product_in_cart", "Le produit est encore présent dans un panier.");
        }
    }

    private Category category(UUID id) {
        return categories.findById(id).orElseThrow(() -> notFound("Catégorie", id));
    }

    private Category categoryForMove(UUID id) {
        return categories.findForMoveById(id).orElseThrow(() -> notFound("Catégorie", id));
    }

    private Product requireProduct(UUID id) {
        return products.findById(id).orElseThrow(() -> notFound("Produit", id));
    }
}

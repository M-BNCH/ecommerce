package fr.boutique.ecommerce.catalog;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
class CatalogController {
    record ParentInput(@NotNull UUID parentId) {}
    record ProductCategoryInput(@NotNull UUID categoryId) {}

    private final CatalogService catalog;

    CatalogController(CatalogService catalog) {
        this.catalog = catalog;
    }

    @GetMapping("/categories")
    List<CategoryView> roots() {
        return catalog.roots();
    }

    @GetMapping("/categories/{id}")
    CategoryDetails category(@PathVariable UUID id) {
        return catalog.categoryDetails(id);
    }

    @PostMapping("/categories")
    ResponseEntity<CategoryView> createCategory(@Valid @RequestBody CategoryInput input) {
        CategoryView category = catalog.createCategory(input);
        return ResponseEntity.created(URI.create("/api/categories/" + category.id())).body(category);
    }

    @PutMapping("/categories/{id}")
    CategoryView updateCategory(@PathVariable UUID id, @Valid @RequestBody CategoryInput input) {
        return catalog.updateCategory(id, input);
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable UUID id) {
        catalog.deleteCategory(id);
    }

    @PutMapping("/categories/{id}/parent")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void attachCategory(@PathVariable UUID id, @Valid @RequestBody ParentInput input) {
        catalog.setParent(id, input.parentId());
    }

    @DeleteMapping("/categories/{id}/parent")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void detachCategory(@PathVariable UUID id) {
        catalog.setParent(id, null);
    }

    @GetMapping("/products")
    List<ProductView> products() {
        return catalog.products();
    }

    @GetMapping("/products/{id}")
    ProductView product(@PathVariable UUID id) {
        return catalog.product(id);
    }

    @PostMapping("/products")
    ResponseEntity<ProductView> createProduct(@Valid @RequestBody ProductInput input) {
        ProductView product = catalog.createProduct(input);
        return ResponseEntity.created(URI.create("/api/products/" + product.id())).body(product);
    }

    @PutMapping("/products/{id}")
    ProductView updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductInput input) {
        return catalog.updateProduct(id, input);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteProduct(@PathVariable UUID id) {
        catalog.deleteProduct(id);
    }

    @PutMapping("/products/{id}/category")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void attachProduct(@PathVariable UUID id, @Valid @RequestBody ProductCategoryInput input) {
        catalog.setProductCategory(id, input.categoryId());
    }

    @DeleteMapping("/products/{id}/category")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void detachProduct(@PathVariable UUID id) {
        catalog.setProductCategory(id, null);
    }
}

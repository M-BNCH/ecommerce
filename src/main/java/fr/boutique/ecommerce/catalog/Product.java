package fr.boutique.ecommerce.catalog;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {

    @Id @GeneratedValue
    private UUID id;

    @Version
    private long version;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stockQuantity;

    // Un produit peut être détaché et appartient au plus à une catégorie.
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    protected Product() {}

    Product(ProductInput input) {
        update(input);
    }

    void update(ProductInput input) {
        name = input.name().strip();
        price = input.price().setScale(2);
        stockQuantity = input.stockQuantity();
    }

    void setCategory(Category category) {
        this.category = category;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    ProductView view() {
        return new ProductView(id, name, price, stockQuantity, category == null ? null : category.getId());
    }
}

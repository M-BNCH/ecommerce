package fr.boutique.ecommerce.cart;

import fr.boutique.ecommerce.catalog.Product;
import jakarta.persistence.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static fr.boutique.ecommerce.common.ApiException.conflict;

@Entity
@Table(name = "carts")
class Cart {

    @Id @GeneratedValue
    private UUID id;

    @Version
    private long version;

    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "product_id", nullable = false)
    @Column(name = "quantity", nullable = false)
    private Map<Product, Integer> quantities = new HashMap<>();

    protected Cart() {}

    UUID getId() {
        return id;
    }
    Map<Product, Integer> quantities() {
        return Collections.unmodifiableMap(quantities);
    }

    void setQuantity(Product product, int quantity) {
        if (quantity > product.getStockQuantity()) {
            throw conflict("insufficient_stock", "Stock insuffisant pour " + product.getName() + ".");
        }
        if (quantity == 0) {
            quantities.remove(product);
        } else {
            quantities.put(product, quantity);
        }
    }
}

package fr.boutique.ecommerce.cart;

import fr.boutique.ecommerce.catalog.CatalogService;
import fr.boutique.ecommerce.catalog.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import static fr.boutique.ecommerce.common.ApiException.badRequest;
import static fr.boutique.ecommerce.common.ApiException.notFound;

@Service
class CartService {

    private final CartRepository carts;
    private final CatalogService catalog;

    CartService(CartRepository carts, CatalogService catalog) {
        this.carts = carts;
        this.catalog = catalog;
    }

    @Transactional
    CartView create() {
        return CartView.from(carts.save(new Cart()));
    }

    @Transactional(readOnly = true)
    CartView get(UUID id) {
        return CartView.from(requireCart(id));
    }

    @Transactional
    CartView updateItems(UUID id, CartPatch patch) {

        Cart cart = requireCart(id);
        Set<UUID> ids = patch.items().stream().map(CartPatch.Item::productId).collect(Collectors.toSet());

        if (ids.size() != patch.items().size()) {
            throw badRequest("duplicate_product", "Chaque produit doit apparaître une seule fois.");
        }

        Map<UUID, Product> products = catalog.findProducts(ids).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        for (CartPatch.Item item : patch.items()) {
            Product product = products.get(item.productId());
            if (product == null) {
                throw notFound("Produit", item.productId());
            }
            cart.setQuantity(product, item.quantity());
        }

        return CartView.from(cart);
    }

    private Cart requireCart(UUID id) {
        return carts.findById(id).orElseThrow(() -> notFound("Panier", id));
    }
}

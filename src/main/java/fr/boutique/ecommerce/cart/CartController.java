package fr.boutique.ecommerce.cart;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
class CartController {
    private final CartService carts;

    CartController(CartService carts) {
        this.carts = carts;
    }

    @PostMapping
    ResponseEntity<CartView> create() {
        CartView cart = carts.create();
        return ResponseEntity.created(URI.create("/api/carts/" + cart.id())).body(cart);
    }

    @GetMapping("/{id}")
    CartView get(@PathVariable UUID id) {
        return carts.get(id);
    }

    @PatchMapping("/{id}/items")
    CartView updateItems(@PathVariable UUID id, @Valid @RequestBody CartPatch patch) {
        return carts.updateItems(id, patch);
    }
}

package fr.boutique.ecommerce.cart;

import fr.boutique.ecommerce.catalog.Product;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

record CartView(UUID id, List<Line> items, BigDecimal total) {
    record Line(UUID productId, String name, BigDecimal unitPrice, int quantity, BigDecimal lineTotal) {
        static Line from(Product product, int quantity) {
            BigDecimal price = product.getPrice();
            return new Line(product.getId(), product.getName(), price, quantity,
                    price.multiply(BigDecimal.valueOf(quantity)));
        }
    }

    static CartView from(Cart cart) {
        var lines = cart.quantities().entrySet().stream()
                .map(entry -> Line.from(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(Line::name).thenComparing(Line::productId))
                .toList();
        BigDecimal total = lines.stream().map(Line::lineTotal).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
        return new CartView(cart.getId(), lines, total);
    }
}

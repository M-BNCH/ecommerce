package fr.boutique.ecommerce.catalog;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "categories")
class Category {
    @Id @GeneratedValue
    private UUID id;

    @Version
    private long version;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category parent;

    protected Category() {}

    Category(CategoryInput input) {
        update(input);
    }

    void update(CategoryInput input) {
        name = input.name().strip();
        description = input.description();
    }

    UUID getId() {
        return id;
    }

    UUID parentId() {
        return parent == null ? null : parent.getId();
    }

    void setParent(Category parent) {
        this.parent = parent;
    }

    CategoryView view() {
        return new CategoryView(id, name, description, parentId());
    }
}

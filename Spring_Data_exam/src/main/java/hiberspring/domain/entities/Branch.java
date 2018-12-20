package hiberspring.domain.entities;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Branch")
@Table(name = "branches")
public class Branch extends BaseEntity {

    private String name;
    private Town town;
    private List<Product> products;

    public Branch() {
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(targetEntity = Town.class, optional = false)
    @JoinColumn(name = "town", referencedColumnName = "id", nullable = false)
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    @OneToMany(targetEntity = Product.class, mappedBy = "branch")
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

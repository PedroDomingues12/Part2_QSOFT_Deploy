package com.jhipster.smartdine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dish.
 */
@Entity
@Table(name = "dish")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private Float price;

    @ManyToOne(optional = false)
    @NotNull
    private Ingredient ingridientName;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "dishes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "dishes" }, allowSetters = true)
    private Set<Purchase> purchases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dish id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Dish name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return this.price;
    }

    public Dish price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Ingredient getIngridientName() {
        return this.ingridientName;
    }

    public void setIngridientName(Ingredient ingredient) {
        this.ingridientName = ingredient;
    }

    public Dish ingridientName(Ingredient ingredient) {
        this.setIngridientName(ingredient);
        return this;
    }

    public Set<Purchase> getPurchases() {
        return this.purchases;
    }

    public void setPurchases(Set<Purchase> purchases) {
        if (this.purchases != null) {
            this.purchases.forEach(i -> i.removeDish(this));
        }
        if (purchases != null) {
            purchases.forEach(i -> i.addDish(this));
        }
        this.purchases = purchases;
    }

    public Dish purchases(Set<Purchase> purchases) {
        this.setPurchases(purchases);
        return this;
    }

    public Dish addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
        purchase.getDishes().add(this);
        return this;
    }

    public Dish removePurchase(Purchase purchase) {
        this.purchases.remove(purchase);
        purchase.getDishes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dish)) {
            return false;
        }
        return getId() != null && getId().equals(((Dish) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dish{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}

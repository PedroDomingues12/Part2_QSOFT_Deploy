package com.jhipster.smartdine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jhipster.smartdine.domain.enumeration.PaymentMethods;
import com.jhipster.smartdine.domain.enumeration.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Purchase.
 */
@Entity
@Table(name = "purchase")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Purchase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "ammount", precision = 21, scale = 2)
    private BigDecimal ammount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethods paymentMethod;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull
    @JoinTable(
        name = "rel_purchase__dish",
        joinColumns = @JoinColumn(name = "purchase_id"),
        inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ingridientName", "purchases" }, allowSetters = true)
    private Set<Dish> dishes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Purchase id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Purchase date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmmount() {
        return this.ammount;
    }

    public Purchase ammount(BigDecimal ammount) {
        this.setAmmount(ammount);
        return this;
    }

    public void setAmmount(BigDecimal ammount) {
        this.ammount = ammount;
    }

    public Status getStatus() {
        return this.status;
    }

    public Purchase status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public PaymentMethods getPaymentMethod() {
        return this.paymentMethod;
    }

    public Purchase paymentMethod(PaymentMethods paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(PaymentMethods paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Purchase user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Dish> getDishes() {
        return this.dishes;
    }

    public void setDishes(Set<Dish> dishes) {
        this.dishes = dishes;
    }

    public Purchase dishes(Set<Dish> dishes) {
        this.setDishes(dishes);
        return this;
    }

    public Purchase addDish(Dish dish) {
        this.dishes.add(dish);
        return this;
    }

    public Purchase removeDish(Dish dish) {
        this.dishes.remove(dish);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Purchase)) {
            return false;
        }
        return getId() != null && getId().equals(((Purchase) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Purchase{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", ammount=" + getAmmount() +
            ", status='" + getStatus() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            "}";
    }
}

package com.jhipster.smartdine.repository;

import com.jhipster.smartdine.domain.Purchase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PurchaseRepositoryWithBagRelationshipsImpl implements PurchaseRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PURCHASES_PARAMETER = "purchases";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Purchase> fetchBagRelationships(Optional<Purchase> purchase) {
        return purchase.map(this::fetchDishes);
    }

    @Override
    public Page<Purchase> fetchBagRelationships(Page<Purchase> purchases) {
        return new PageImpl<>(fetchBagRelationships(purchases.getContent()), purchases.getPageable(), purchases.getTotalElements());
    }

    @Override
    public List<Purchase> fetchBagRelationships(List<Purchase> purchases) {
        return Optional.of(purchases).map(this::fetchDishes).orElse(Collections.emptyList());
    }

    Purchase fetchDishes(Purchase result) {
        return entityManager
            .createQuery("select purchase from Purchase purchase left join fetch purchase.dishes where purchase.id = :id", Purchase.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Purchase> fetchDishes(List<Purchase> purchases) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, purchases.size()).forEach(index -> order.put(purchases.get(index).getId(), index));
        List<Purchase> result = entityManager
            .createQuery(
                "select purchase from Purchase purchase left join fetch purchase.dishes where purchase in :purchases",
                Purchase.class
            )
            .setParameter(PURCHASES_PARAMETER, purchases)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

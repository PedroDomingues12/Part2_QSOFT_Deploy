package com.jhipster.smartdine.repository;

import com.jhipster.smartdine.domain.Purchase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PurchaseRepositoryWithBagRelationships {
    Optional<Purchase> fetchBagRelationships(Optional<Purchase> purchase);

    List<Purchase> fetchBagRelationships(List<Purchase> purchases);

    Page<Purchase> fetchBagRelationships(Page<Purchase> purchases);
}

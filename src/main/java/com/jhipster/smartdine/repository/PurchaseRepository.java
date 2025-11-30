package com.jhipster.smartdine.repository;

import com.jhipster.smartdine.domain.Purchase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Purchase entity.
 *
 * When extending this class, extend PurchaseRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PurchaseRepository extends PurchaseRepositoryWithBagRelationships, JpaRepository<Purchase, Long> {
    @Query("select purchase from Purchase purchase where purchase.user.login = ?#{authentication.name}")
    List<Purchase> findByUserIsCurrentUser();

    default Optional<Purchase> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Purchase> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Purchase> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select purchase from Purchase purchase left join fetch purchase.user",
        countQuery = "select count(purchase) from Purchase purchase"
    )
    Page<Purchase> findAllWithToOneRelationships(Pageable pageable);

    @Query("select purchase from Purchase purchase left join fetch purchase.user")
    List<Purchase> findAllWithToOneRelationships();

    @Query("select purchase from Purchase purchase left join fetch purchase.user where purchase.id =:id")
    Optional<Purchase> findOneWithToOneRelationships(@Param("id") Long id);
}

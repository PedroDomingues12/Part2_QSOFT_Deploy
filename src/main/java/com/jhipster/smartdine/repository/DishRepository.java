package com.jhipster.smartdine.repository;

import com.jhipster.smartdine.domain.Dish;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Dish entity.
 */
@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    default Optional<Dish> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Dish> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Dish> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select dish from Dish dish left join fetch dish.ingridientName", countQuery = "select count(dish) from Dish dish")
    Page<Dish> findAllWithToOneRelationships(Pageable pageable);

    @Query("select dish from Dish dish left join fetch dish.ingridientName")
    List<Dish> findAllWithToOneRelationships();

    @Query("select dish from Dish dish left join fetch dish.ingridientName where dish.id =:id")
    Optional<Dish> findOneWithToOneRelationships(@Param("id") Long id);
}

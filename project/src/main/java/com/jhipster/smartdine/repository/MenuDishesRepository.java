package com.jhipster.smartdine.repository;

import com.jhipster.smartdine.domain.MenuDishes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MenuDishes entity.
 */
@Repository
public interface MenuDishesRepository extends JpaRepository<MenuDishes, Long> {
    default Optional<MenuDishes> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MenuDishes> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MenuDishes> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select menuDishes from MenuDishes menuDishes left join fetch menuDishes.dishNames",
        countQuery = "select count(menuDishes) from MenuDishes menuDishes"
    )
    Page<MenuDishes> findAllWithToOneRelationships(Pageable pageable);

    @Query("select menuDishes from MenuDishes menuDishes left join fetch menuDishes.dishNames")
    List<MenuDishes> findAllWithToOneRelationships();

    @Query("select menuDishes from MenuDishes menuDishes left join fetch menuDishes.dishNames where menuDishes.id =:id")
    Optional<MenuDishes> findOneWithToOneRelationships(@Param("id") Long id);
}

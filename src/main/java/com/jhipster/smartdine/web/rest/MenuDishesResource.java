package com.jhipster.smartdine.web.rest;

import com.jhipster.smartdine.domain.MenuDishes;
import com.jhipster.smartdine.repository.MenuDishesRepository;
import com.jhipster.smartdine.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jhipster.smartdine.domain.MenuDishes}.
 */
@RestController
@RequestMapping("/api/menu-dishes")
@Transactional
public class MenuDishesResource {

    private static final Logger LOG = LoggerFactory.getLogger(MenuDishesResource.class);

    private static final String ENTITY_NAME = "menuDishes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuDishesRepository menuDishesRepository;

    public MenuDishesResource(MenuDishesRepository menuDishesRepository) {
        this.menuDishesRepository = menuDishesRepository;
    }

    /**
     * {@code POST  /menu-dishes} : Create a new menuDishes.
     *
     * @param menuDishes the menuDishes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menuDishes, or with status {@code 400 (Bad Request)} if the menuDishes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MenuDishes> createMenuDishes(@Valid @RequestBody MenuDishes menuDishes) throws URISyntaxException {
        LOG.debug("REST request to save MenuDishes : {}", menuDishes);
        if (menuDishes.getId() != null) {
            throw new BadRequestAlertException("A new menuDishes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        menuDishes = menuDishesRepository.save(menuDishes);
        return ResponseEntity.created(new URI("/api/menu-dishes/" + menuDishes.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, menuDishes.getId().toString()))
            .body(menuDishes);
    }

    /**
     * {@code PUT  /menu-dishes/:id} : Updates an existing menuDishes.
     *
     * @param id the id of the menuDishes to save.
     * @param menuDishes the menuDishes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuDishes,
     * or with status {@code 400 (Bad Request)} if the menuDishes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menuDishes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuDishes> updateMenuDishes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MenuDishes menuDishes
    ) throws URISyntaxException {
        LOG.debug("REST request to update MenuDishes : {}, {}", id, menuDishes);
        if (menuDishes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuDishes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuDishesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        menuDishes = menuDishesRepository.save(menuDishes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuDishes.getId().toString()))
            .body(menuDishes);
    }

    /**
     * {@code PATCH  /menu-dishes/:id} : Partial updates given fields of an existing menuDishes, field will ignore if it is null
     *
     * @param id the id of the menuDishes to save.
     * @param menuDishes the menuDishes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuDishes,
     * or with status {@code 400 (Bad Request)} if the menuDishes is not valid,
     * or with status {@code 404 (Not Found)} if the menuDishes is not found,
     * or with status {@code 500 (Internal Server Error)} if the menuDishes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenuDishes> partialUpdateMenuDishes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MenuDishes menuDishes
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MenuDishes partially : {}, {}", id, menuDishes);
        if (menuDishes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuDishes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuDishesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MenuDishes> result = menuDishesRepository
            .findById(menuDishes.getId())
            .map(existingMenuDishes -> {
                if (menuDishes.getName() != null) {
                    existingMenuDishes.setName(menuDishes.getName());
                }

                return existingMenuDishes;
            })
            .map(menuDishesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuDishes.getId().toString())
        );
    }

    /**
     * {@code GET  /menu-dishes} : get all the menuDishes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menuDishes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MenuDishes>> getAllMenuDishes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of MenuDishes");
        Page<MenuDishes> page;
        if (eagerload) {
            page = menuDishesRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = menuDishesRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /menu-dishes/:id} : get the "id" menuDishes.
     *
     * @param id the id of the menuDishes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menuDishes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuDishes> getMenuDishes(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MenuDishes : {}", id);
        Optional<MenuDishes> menuDishes = menuDishesRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(menuDishes);
    }

    /**
     * {@code DELETE  /menu-dishes/:id} : delete the "id" menuDishes.
     *
     * @param id the id of the menuDishes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuDishes(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MenuDishes : {}", id);
        menuDishesRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

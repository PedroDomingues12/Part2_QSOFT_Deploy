package com.jhipster.smartdine.web.rest;

import static com.jhipster.smartdine.domain.MenuDishesAsserts.*;
import static com.jhipster.smartdine.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhipster.smartdine.IntegrationTest;
import com.jhipster.smartdine.domain.Dish;
import com.jhipster.smartdine.domain.MenuDishes;
import com.jhipster.smartdine.repository.MenuDishesRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MenuDishesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MenuDishesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/menu-dishes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MenuDishesRepository menuDishesRepository;

    @Mock
    private MenuDishesRepository menuDishesRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuDishesMockMvc;

    private MenuDishes menuDishes;

    private MenuDishes insertedMenuDishes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuDishes createEntity(EntityManager em) {
        MenuDishes menuDishes = new MenuDishes().name(DEFAULT_NAME);
        // Add required entity
        Dish dish;
        if (TestUtil.findAll(em, Dish.class).isEmpty()) {
            dish = DishResourceIT.createEntity(em);
            em.persist(dish);
            em.flush();
        } else {
            dish = TestUtil.findAll(em, Dish.class).get(0);
        }
        menuDishes.setDishNames(dish);
        return menuDishes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuDishes createUpdatedEntity(EntityManager em) {
        MenuDishes updatedMenuDishes = new MenuDishes().name(UPDATED_NAME);
        // Add required entity
        Dish dish;
        if (TestUtil.findAll(em, Dish.class).isEmpty()) {
            dish = DishResourceIT.createUpdatedEntity(em);
            em.persist(dish);
            em.flush();
        } else {
            dish = TestUtil.findAll(em, Dish.class).get(0);
        }
        updatedMenuDishes.setDishNames(dish);
        return updatedMenuDishes;
    }

    @BeforeEach
    void initTest() {
        menuDishes = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMenuDishes != null) {
            menuDishesRepository.delete(insertedMenuDishes);
            insertedMenuDishes = null;
        }
    }

    @Test
    @Transactional
    void createMenuDishes() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MenuDishes
        var returnedMenuDishes = om.readValue(
            restMenuDishesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDishes)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MenuDishes.class
        );

        // Validate the MenuDishes in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMenuDishesUpdatableFieldsEquals(returnedMenuDishes, getPersistedMenuDishes(returnedMenuDishes));

        insertedMenuDishes = returnedMenuDishes;
    }

    @Test
    @Transactional
    void createMenuDishesWithExistingId() throws Exception {
        // Create the MenuDishes with an existing ID
        menuDishes.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuDishesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDishes)))
            .andExpect(status().isBadRequest());

        // Validate the MenuDishes in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menuDishes.setName(null);

        // Create the MenuDishes, which fails.

        restMenuDishesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDishes)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMenuDishes() throws Exception {
        // Initialize the database
        insertedMenuDishes = menuDishesRepository.saveAndFlush(menuDishes);

        // Get all the menuDishesList
        restMenuDishesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuDishes.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMenuDishesWithEagerRelationshipsIsEnabled() throws Exception {
        when(menuDishesRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMenuDishesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(menuDishesRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMenuDishesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(menuDishesRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMenuDishesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(menuDishesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMenuDishes() throws Exception {
        // Initialize the database
        insertedMenuDishes = menuDishesRepository.saveAndFlush(menuDishes);

        // Get the menuDishes
        restMenuDishesMockMvc
            .perform(get(ENTITY_API_URL_ID, menuDishes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menuDishes.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingMenuDishes() throws Exception {
        // Get the menuDishes
        restMenuDishesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMenuDishes() throws Exception {
        // Initialize the database
        insertedMenuDishes = menuDishesRepository.saveAndFlush(menuDishes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuDishes
        MenuDishes updatedMenuDishes = menuDishesRepository.findById(menuDishes.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMenuDishes are not directly saved in db
        em.detach(updatedMenuDishes);
        updatedMenuDishes.name(UPDATED_NAME);

        restMenuDishesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMenuDishes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMenuDishes))
            )
            .andExpect(status().isOk());

        // Validate the MenuDishes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMenuDishesToMatchAllProperties(updatedMenuDishes);
    }

    @Test
    @Transactional
    void putNonExistingMenuDishes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuDishes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuDishesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuDishes.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDishes))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuDishes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenuDishes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuDishes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuDishesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(menuDishes))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuDishes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenuDishes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuDishes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuDishesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDishes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuDishes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuDishesWithPatch() throws Exception {
        // Initialize the database
        insertedMenuDishes = menuDishesRepository.saveAndFlush(menuDishes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuDishes using partial update
        MenuDishes partialUpdatedMenuDishes = new MenuDishes();
        partialUpdatedMenuDishes.setId(menuDishes.getId());

        partialUpdatedMenuDishes.name(UPDATED_NAME);

        restMenuDishesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuDishes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMenuDishes))
            )
            .andExpect(status().isOk());

        // Validate the MenuDishes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMenuDishesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMenuDishes, menuDishes),
            getPersistedMenuDishes(menuDishes)
        );
    }

    @Test
    @Transactional
    void fullUpdateMenuDishesWithPatch() throws Exception {
        // Initialize the database
        insertedMenuDishes = menuDishesRepository.saveAndFlush(menuDishes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuDishes using partial update
        MenuDishes partialUpdatedMenuDishes = new MenuDishes();
        partialUpdatedMenuDishes.setId(menuDishes.getId());

        partialUpdatedMenuDishes.name(UPDATED_NAME);

        restMenuDishesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuDishes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMenuDishes))
            )
            .andExpect(status().isOk());

        // Validate the MenuDishes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMenuDishesUpdatableFieldsEquals(partialUpdatedMenuDishes, getPersistedMenuDishes(partialUpdatedMenuDishes));
    }

    @Test
    @Transactional
    void patchNonExistingMenuDishes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuDishes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuDishesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menuDishes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(menuDishes))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuDishes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenuDishes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuDishes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuDishesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(menuDishes))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuDishes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenuDishes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuDishes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuDishesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(menuDishes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuDishes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenuDishes() throws Exception {
        // Initialize the database
        insertedMenuDishes = menuDishesRepository.saveAndFlush(menuDishes);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the menuDishes
        restMenuDishesMockMvc
            .perform(delete(ENTITY_API_URL_ID, menuDishes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return menuDishesRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected MenuDishes getPersistedMenuDishes(MenuDishes menuDishes) {
        return menuDishesRepository.findById(menuDishes.getId()).orElseThrow();
    }

    protected void assertPersistedMenuDishesToMatchAllProperties(MenuDishes expectedMenuDishes) {
        assertMenuDishesAllPropertiesEquals(expectedMenuDishes, getPersistedMenuDishes(expectedMenuDishes));
    }

    protected void assertPersistedMenuDishesToMatchUpdatableProperties(MenuDishes expectedMenuDishes) {
        assertMenuDishesAllUpdatablePropertiesEquals(expectedMenuDishes, getPersistedMenuDishes(expectedMenuDishes));
    }
}

package com.jhipster.smartdine.web.rest;

import static com.jhipster.smartdine.domain.DishAsserts.*;
import static com.jhipster.smartdine.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhipster.smartdine.IntegrationTest;
import com.jhipster.smartdine.domain.Dish;
import com.jhipster.smartdine.domain.Ingredient;
import com.jhipster.smartdine.repository.DishRepository;
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
 * Integration tests for the {@link DishResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DishResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final String ENTITY_API_URL = "/api/dishes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DishRepository dishRepository;

    @Mock
    private DishRepository dishRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDishMockMvc;

    private Dish dish;

    private Dish insertedDish;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dish createEntity(EntityManager em) {
        Dish dish = new Dish().name(DEFAULT_NAME).price(DEFAULT_PRICE);
        // Add required entity
        Ingredient ingredient;
        if (TestUtil.findAll(em, Ingredient.class).isEmpty()) {
            ingredient = IngredientResourceIT.createEntity();
            em.persist(ingredient);
            em.flush();
        } else {
            ingredient = TestUtil.findAll(em, Ingredient.class).get(0);
        }
        dish.setIngridientName(ingredient);
        return dish;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dish createUpdatedEntity(EntityManager em) {
        Dish updatedDish = new Dish().name(UPDATED_NAME).price(UPDATED_PRICE);
        // Add required entity
        Ingredient ingredient;
        if (TestUtil.findAll(em, Ingredient.class).isEmpty()) {
            ingredient = IngredientResourceIT.createUpdatedEntity();
            em.persist(ingredient);
            em.flush();
        } else {
            ingredient = TestUtil.findAll(em, Ingredient.class).get(0);
        }
        updatedDish.setIngridientName(ingredient);
        return updatedDish;
    }

    @BeforeEach
    void initTest() {
        dish = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDish != null) {
            dishRepository.delete(insertedDish);
            insertedDish = null;
        }
    }

    @Test
    @Transactional
    void createDish() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Dish
        var returnedDish = om.readValue(
            restDishMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dish)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Dish.class
        );

        // Validate the Dish in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDishUpdatableFieldsEquals(returnedDish, getPersistedDish(returnedDish));

        insertedDish = returnedDish;
    }

    @Test
    @Transactional
    void createDishWithExistingId() throws Exception {
        // Create the Dish with an existing ID
        dish.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dish)))
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dish.setName(null);

        // Create the Dish, which fails.

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dish)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dish.setPrice(null);

        // Create the Dish, which fails.

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dish)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDishes() throws Exception {
        // Initialize the database
        insertedDish = dishRepository.saveAndFlush(dish);

        // Get all the dishList
        restDishMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dish.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDishesWithEagerRelationshipsIsEnabled() throws Exception {
        when(dishRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDishMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dishRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDishesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dishRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDishMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(dishRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDish() throws Exception {
        // Initialize the database
        insertedDish = dishRepository.saveAndFlush(dish);

        // Get the dish
        restDishMockMvc
            .perform(get(ENTITY_API_URL_ID, dish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dish.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingDish() throws Exception {
        // Get the dish
        restDishMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDish() throws Exception {
        // Initialize the database
        insertedDish = dishRepository.saveAndFlush(dish);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dish
        Dish updatedDish = dishRepository.findById(dish.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDish are not directly saved in db
        em.detach(updatedDish);
        updatedDish.name(UPDATED_NAME).price(UPDATED_PRICE);

        restDishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDish.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDish))
            )
            .andExpect(status().isOk());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDishToMatchAllProperties(updatedDish);
    }

    @Test
    @Transactional
    void putNonExistingDish() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dish.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(put(ENTITY_API_URL_ID, dish.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dish)))
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDish() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dish.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDish() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dish.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dish)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDishWithPatch() throws Exception {
        // Initialize the database
        insertedDish = dishRepository.saveAndFlush(dish);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dish using partial update
        Dish partialUpdatedDish = new Dish();
        partialUpdatedDish.setId(dish.getId());

        partialUpdatedDish.name(UPDATED_NAME).price(UPDATED_PRICE);

        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDish.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDish))
            )
            .andExpect(status().isOk());

        // Validate the Dish in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDishUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDish, dish), getPersistedDish(dish));
    }

    @Test
    @Transactional
    void fullUpdateDishWithPatch() throws Exception {
        // Initialize the database
        insertedDish = dishRepository.saveAndFlush(dish);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dish using partial update
        Dish partialUpdatedDish = new Dish();
        partialUpdatedDish.setId(dish.getId());

        partialUpdatedDish.name(UPDATED_NAME).price(UPDATED_PRICE);

        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDish.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDish))
            )
            .andExpect(status().isOk());

        // Validate the Dish in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDishUpdatableFieldsEquals(partialUpdatedDish, getPersistedDish(partialUpdatedDish));
    }

    @Test
    @Transactional
    void patchNonExistingDish() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dish.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(patch(ENTITY_API_URL_ID, dish.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dish)))
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDish() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dish.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDish() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dish.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dish)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDish() throws Exception {
        // Initialize the database
        insertedDish = dishRepository.saveAndFlush(dish);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dish
        restDishMockMvc
            .perform(delete(ENTITY_API_URL_ID, dish.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dishRepository.count();
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

    protected Dish getPersistedDish(Dish dish) {
        return dishRepository.findById(dish.getId()).orElseThrow();
    }

    protected void assertPersistedDishToMatchAllProperties(Dish expectedDish) {
        assertDishAllPropertiesEquals(expectedDish, getPersistedDish(expectedDish));
    }

    protected void assertPersistedDishToMatchUpdatableProperties(Dish expectedDish) {
        assertDishAllUpdatablePropertiesEquals(expectedDish, getPersistedDish(expectedDish));
    }
}

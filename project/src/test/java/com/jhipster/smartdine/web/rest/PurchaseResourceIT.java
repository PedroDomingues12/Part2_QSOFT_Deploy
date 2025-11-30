package com.jhipster.smartdine.web.rest;

import static com.jhipster.smartdine.domain.PurchaseAsserts.*;
import static com.jhipster.smartdine.web.rest.TestUtil.createUpdateProxyForBean;
import static com.jhipster.smartdine.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhipster.smartdine.IntegrationTest;
import com.jhipster.smartdine.domain.Dish;
import com.jhipster.smartdine.domain.Purchase;
import com.jhipster.smartdine.domain.User;
import com.jhipster.smartdine.domain.enumeration.PaymentMethods;
import com.jhipster.smartdine.domain.enumeration.Status;
import com.jhipster.smartdine.repository.PurchaseRepository;
import com.jhipster.smartdine.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PurchaseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PurchaseResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_AMMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMMOUNT = new BigDecimal(2);

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.COMPLETED;

    private static final PaymentMethods DEFAULT_PAYMENT_METHOD = PaymentMethods.CARD;
    private static final PaymentMethods UPDATED_PAYMENT_METHOD = PaymentMethods.CASH;

    private static final String ENTITY_API_URL = "/api/purchases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PurchaseRepository purchaseRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseMockMvc;

    private Purchase purchase;

    private Purchase insertedPurchase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Purchase createEntity(EntityManager em) {
        Purchase purchase = new Purchase()
            .date(DEFAULT_DATE)
            .ammount(DEFAULT_AMMOUNT)
            .status(DEFAULT_STATUS)
            .paymentMethod(DEFAULT_PAYMENT_METHOD);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        purchase.setUser(user);
        // Add required entity
        Dish dish;
        if (TestUtil.findAll(em, Dish.class).isEmpty()) {
            dish = DishResourceIT.createEntity(em);
            em.persist(dish);
            em.flush();
        } else {
            dish = TestUtil.findAll(em, Dish.class).get(0);
        }
        purchase.getDishes().add(dish);
        return purchase;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Purchase createUpdatedEntity(EntityManager em) {
        Purchase updatedPurchase = new Purchase()
            .date(UPDATED_DATE)
            .ammount(UPDATED_AMMOUNT)
            .status(UPDATED_STATUS)
            .paymentMethod(UPDATED_PAYMENT_METHOD);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedPurchase.setUser(user);
        // Add required entity
        Dish dish;
        if (TestUtil.findAll(em, Dish.class).isEmpty()) {
            dish = DishResourceIT.createUpdatedEntity(em);
            em.persist(dish);
            em.flush();
        } else {
            dish = TestUtil.findAll(em, Dish.class).get(0);
        }
        updatedPurchase.getDishes().add(dish);
        return updatedPurchase;
    }

    @BeforeEach
    void initTest() {
        purchase = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPurchase != null) {
            purchaseRepository.delete(insertedPurchase);
            insertedPurchase = null;
        }
    }

    @Test
    @Transactional
    void createPurchase() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Purchase
        var returnedPurchase = om.readValue(
            restPurchaseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchase)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Purchase.class
        );

        // Validate the Purchase in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPurchaseUpdatableFieldsEquals(returnedPurchase, getPersistedPurchase(returnedPurchase));

        insertedPurchase = returnedPurchase;
    }

    @Test
    @Transactional
    void createPurchaseWithExistingId() throws Exception {
        // Create the Purchase with an existing ID
        purchase.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchase)))
            .andExpect(status().isBadRequest());

        // Validate the Purchase in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchase.setDate(null);

        // Create the Purchase, which fails.

        restPurchaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchase)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchase.setStatus(null);

        // Create the Purchase, which fails.

        restPurchaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchase)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentMethodIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchase.setPaymentMethod(null);

        // Create the Purchase, which fails.

        restPurchaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchase)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPurchases() throws Exception {
        // Initialize the database
        insertedPurchase = purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList
        restPurchaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchase.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].ammount").value(hasItem(sameNumber(DEFAULT_AMMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasesWithEagerRelationshipsIsEnabled() throws Exception {
        when(purchaseRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchaseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(purchaseRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(purchaseRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchaseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(purchaseRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPurchase() throws Exception {
        // Initialize the database
        insertedPurchase = purchaseRepository.saveAndFlush(purchase);

        // Get the purchase
        restPurchaseMockMvc
            .perform(get(ENTITY_API_URL_ID, purchase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchase.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.ammount").value(sameNumber(DEFAULT_AMMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPurchase() throws Exception {
        // Get the purchase
        restPurchaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchase() throws Exception {
        // Initialize the database
        insertedPurchase = purchaseRepository.saveAndFlush(purchase);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchase
        Purchase updatedPurchase = purchaseRepository.findById(purchase.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPurchase are not directly saved in db
        em.detach(updatedPurchase);
        updatedPurchase.date(UPDATED_DATE).ammount(UPDATED_AMMOUNT).status(UPDATED_STATUS).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPurchaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPurchase.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPurchase))
            )
            .andExpect(status().isOk());

        // Validate the Purchase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPurchaseToMatchAllProperties(updatedPurchase);
    }

    @Test
    @Transactional
    void putNonExistingPurchase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchase.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchase.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchase))
            )
            .andExpect(status().isBadRequest());

        // Validate the Purchase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchase.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchase))
            )
            .andExpect(status().isBadRequest());

        // Validate the Purchase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchase.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchase)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Purchase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseWithPatch() throws Exception {
        // Initialize the database
        insertedPurchase = purchaseRepository.saveAndFlush(purchase);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchase using partial update
        Purchase partialUpdatedPurchase = new Purchase();
        partialUpdatedPurchase.setId(purchase.getId());

        partialUpdatedPurchase.ammount(UPDATED_AMMOUNT).status(UPDATED_STATUS);

        restPurchaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchase.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchase))
            )
            .andExpect(status().isOk());

        // Validate the Purchase in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchaseUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPurchase, purchase), getPersistedPurchase(purchase));
    }

    @Test
    @Transactional
    void fullUpdatePurchaseWithPatch() throws Exception {
        // Initialize the database
        insertedPurchase = purchaseRepository.saveAndFlush(purchase);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchase using partial update
        Purchase partialUpdatedPurchase = new Purchase();
        partialUpdatedPurchase.setId(purchase.getId());

        partialUpdatedPurchase.date(UPDATED_DATE).ammount(UPDATED_AMMOUNT).status(UPDATED_STATUS).paymentMethod(UPDATED_PAYMENT_METHOD);

        restPurchaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchase.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchase))
            )
            .andExpect(status().isOk());

        // Validate the Purchase in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchaseUpdatableFieldsEquals(partialUpdatedPurchase, getPersistedPurchase(partialUpdatedPurchase));
    }

    @Test
    @Transactional
    void patchNonExistingPurchase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchase.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchase.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchase))
            )
            .andExpect(status().isBadRequest());

        // Validate the Purchase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchase.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchase))
            )
            .andExpect(status().isBadRequest());

        // Validate the Purchase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchase.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(purchase)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Purchase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchase() throws Exception {
        // Initialize the database
        insertedPurchase = purchaseRepository.saveAndFlush(purchase);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the purchase
        restPurchaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchase.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return purchaseRepository.count();
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

    protected Purchase getPersistedPurchase(Purchase purchase) {
        return purchaseRepository.findById(purchase.getId()).orElseThrow();
    }

    protected void assertPersistedPurchaseToMatchAllProperties(Purchase expectedPurchase) {
        assertPurchaseAllPropertiesEquals(expectedPurchase, getPersistedPurchase(expectedPurchase));
    }

    protected void assertPersistedPurchaseToMatchUpdatableProperties(Purchase expectedPurchase) {
        assertPurchaseAllUpdatablePropertiesEquals(expectedPurchase, getPersistedPurchase(expectedPurchase));
    }
}

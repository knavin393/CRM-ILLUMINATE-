package com.codebridgecommunity.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.codebridgecommunity.crm.IntegrationTest;
import com.codebridgecommunity.crm.domain.Customer;
import com.codebridgecommunity.crm.domain.Opportunity;
import com.codebridgecommunity.crm.domain.Product;
import com.codebridgecommunity.crm.domain.WinLoss;
import com.codebridgecommunity.crm.repository.EntityManager;
import com.codebridgecommunity.crm.repository.WinLossRepository;
import com.codebridgecommunity.crm.service.WinLossService;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link WinLossResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class WinLossResourceIT {

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/win-losses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WinLossRepository winLossRepository;

    @Mock
    private WinLossRepository winLossRepositoryMock;

    @Mock
    private WinLossService winLossServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private WinLoss winLoss;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WinLoss createEntity(EntityManager em) {
        WinLoss winLoss = new WinLoss().notes(DEFAULT_NOTES);
        // Add required entity
        Opportunity opportunity;
        opportunity = em.insert(OpportunityResourceIT.createEntity(em)).block();
        winLoss.setOpportunity(opportunity);
        // Add required entity
        Customer customer;
        customer = em.insert(CustomerResourceIT.createEntity(em)).block();
        winLoss.setCustomer(customer);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createEntity(em)).block();
        winLoss.setProduct(product);
        return winLoss;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WinLoss createUpdatedEntity(EntityManager em) {
        WinLoss winLoss = new WinLoss().notes(UPDATED_NOTES);
        // Add required entity
        Opportunity opportunity;
        opportunity = em.insert(OpportunityResourceIT.createUpdatedEntity(em)).block();
        winLoss.setOpportunity(opportunity);
        // Add required entity
        Customer customer;
        customer = em.insert(CustomerResourceIT.createUpdatedEntity(em)).block();
        winLoss.setCustomer(customer);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createUpdatedEntity(em)).block();
        winLoss.setProduct(product);
        return winLoss;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(WinLoss.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        OpportunityResourceIT.deleteEntities(em);
        CustomerResourceIT.deleteEntities(em);
        ProductResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        winLoss = createEntity(em);
    }

    @Test
    void createWinLoss() throws Exception {
        int databaseSizeBeforeCreate = winLossRepository.findAll().collectList().block().size();
        // Create the WinLoss
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(winLoss))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the WinLoss in the database
        List<WinLoss> winLossList = winLossRepository.findAll().collectList().block();
        assertThat(winLossList).hasSize(databaseSizeBeforeCreate + 1);
        WinLoss testWinLoss = winLossList.get(winLossList.size() - 1);
        assertThat(testWinLoss.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    void createWinLossWithExistingId() throws Exception {
        // Create the WinLoss with an existing ID
        winLoss.setId(1L);

        int databaseSizeBeforeCreate = winLossRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(winLoss))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WinLoss in the database
        List<WinLoss> winLossList = winLossRepository.findAll().collectList().block();
        assertThat(winLossList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllWinLosses() {
        // Initialize the database
        winLossRepository.save(winLoss).block();

        // Get all the winLossList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(winLoss.getId().intValue()))
            .jsonPath("$.[*].notes")
            .value(hasItem(DEFAULT_NOTES));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWinLossesWithEagerRelationshipsIsEnabled() {
        when(winLossServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(winLossServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWinLossesWithEagerRelationshipsIsNotEnabled() {
        when(winLossServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(winLossRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getWinLoss() {
        // Initialize the database
        winLossRepository.save(winLoss).block();

        // Get the winLoss
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, winLoss.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(winLoss.getId().intValue()))
            .jsonPath("$.notes")
            .value(is(DEFAULT_NOTES));
    }

    @Test
    void getNonExistingWinLoss() {
        // Get the winLoss
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingWinLoss() throws Exception {
        // Initialize the database
        winLossRepository.save(winLoss).block();

        int databaseSizeBeforeUpdate = winLossRepository.findAll().collectList().block().size();

        // Update the winLoss
        WinLoss updatedWinLoss = winLossRepository.findById(winLoss.getId()).block();
        updatedWinLoss.notes(UPDATED_NOTES);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedWinLoss.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedWinLoss))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WinLoss in the database
        List<WinLoss> winLossList = winLossRepository.findAll().collectList().block();
        assertThat(winLossList).hasSize(databaseSizeBeforeUpdate);
        WinLoss testWinLoss = winLossList.get(winLossList.size() - 1);
        assertThat(testWinLoss.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    void putNonExistingWinLoss() throws Exception {
        int databaseSizeBeforeUpdate = winLossRepository.findAll().collectList().block().size();
        winLoss.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, winLoss.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(winLoss))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WinLoss in the database
        List<WinLoss> winLossList = winLossRepository.findAll().collectList().block();
        assertThat(winLossList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchWinLoss() throws Exception {
        int databaseSizeBeforeUpdate = winLossRepository.findAll().collectList().block().size();
        winLoss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(winLoss))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WinLoss in the database
        List<WinLoss> winLossList = winLossRepository.findAll().collectList().block();
        assertThat(winLossList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamWinLoss() throws Exception {
        int databaseSizeBeforeUpdate = winLossRepository.findAll().collectList().block().size();
        winLoss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(winLoss))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the WinLoss in the database
        List<WinLoss> winLossList = winLossRepository.findAll().collectList().block();
        assertThat(winLossList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateWinLossWithPatch() throws Exception {
        // Initialize the database
        winLossRepository.save(winLoss).block();

        int databaseSizeBeforeUpdate = winLossRepository.findAll().collectList().block().size();

        // Update the winLoss using partial update
        WinLoss partialUpdatedWinLoss = new WinLoss();
        partialUpdatedWinLoss.setId(winLoss.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWinLoss.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWinLoss))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WinLoss in the database
        List<WinLoss> winLossList = winLossRepository.findAll().collectList().block();
        assertThat(winLossList).hasSize(databaseSizeBeforeUpdate);
        WinLoss testWinLoss = winLossList.get(winLossList.size() - 1);
        assertThat(testWinLoss.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    void fullUpdateWinLossWithPatch() throws Exception {
        // Initialize the database
        winLossRepository.save(winLoss).block();

        int databaseSizeBeforeUpdate = winLossRepository.findAll().collectList().block().size();

        // Update the winLoss using partial update
        WinLoss partialUpdatedWinLoss = new WinLoss();
        partialUpdatedWinLoss.setId(winLoss.getId());

        partialUpdatedWinLoss.notes(UPDATED_NOTES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWinLoss.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWinLoss))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WinLoss in the database
        List<WinLoss> winLossList = winLossRepository.findAll().collectList().block();
        assertThat(winLossList).hasSize(databaseSizeBeforeUpdate);
        WinLoss testWinLoss = winLossList.get(winLossList.size() - 1);
        assertThat(testWinLoss.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    void patchNonExistingWinLoss() throws Exception {
        int databaseSizeBeforeUpdate = winLossRepository.findAll().collectList().block().size();
        winLoss.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, winLoss.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(winLoss))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WinLoss in the database
        List<WinLoss> winLossList = winLossRepository.findAll().collectList().block();
        assertThat(winLossList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchWinLoss() throws Exception {
        int databaseSizeBeforeUpdate = winLossRepository.findAll().collectList().block().size();
        winLoss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(winLoss))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WinLoss in the database
        List<WinLoss> winLossList = winLossRepository.findAll().collectList().block();
        assertThat(winLossList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamWinLoss() throws Exception {
        int databaseSizeBeforeUpdate = winLossRepository.findAll().collectList().block().size();
        winLoss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(winLoss))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the WinLoss in the database
        List<WinLoss> winLossList = winLossRepository.findAll().collectList().block();
        assertThat(winLossList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteWinLoss() {
        // Initialize the database
        winLossRepository.save(winLoss).block();

        int databaseSizeBeforeDelete = winLossRepository.findAll().collectList().block().size();

        // Delete the winLoss
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, winLoss.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<WinLoss> winLossList = winLossRepository.findAll().collectList().block();
        assertThat(winLossList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.codebridgecommunity.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.codebridgecommunity.crm.IntegrationTest;
import com.codebridgecommunity.crm.domain.Customer;
import com.codebridgecommunity.crm.domain.Employee;
import com.codebridgecommunity.crm.domain.PotentialOpportunity;
import com.codebridgecommunity.crm.domain.Product;
import com.codebridgecommunity.crm.domain.enumeration.POStatus;
import com.codebridgecommunity.crm.repository.EntityManager;
import com.codebridgecommunity.crm.repository.PotentialOpportunityRepository;
import com.codebridgecommunity.crm.service.PotentialOpportunityService;
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
 * Integration tests for the {@link PotentialOpportunityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PotentialOpportunityResourceIT {

    private static final String DEFAULT_FOLLOW_UP = "AAAAAAAAAA";
    private static final String UPDATED_FOLLOW_UP = "BBBBBBBBBB";

    private static final POStatus DEFAULT_STATUS = POStatus.MAINTAIN_PO;
    private static final POStatus UPDATED_STATUS = POStatus.OPPORTUNITY;

    private static final String ENTITY_API_URL = "/api/potential-opportunities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PotentialOpportunityRepository potentialOpportunityRepository;

    @Mock
    private PotentialOpportunityRepository potentialOpportunityRepositoryMock;

    @Mock
    private PotentialOpportunityService potentialOpportunityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PotentialOpportunity potentialOpportunity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PotentialOpportunity createEntity(EntityManager em) {
        PotentialOpportunity potentialOpportunity = new PotentialOpportunity().followUp(DEFAULT_FOLLOW_UP).status(DEFAULT_STATUS);
        // Add required entity
        Customer customer;
        customer = em.insert(CustomerResourceIT.createEntity(em)).block();
        potentialOpportunity.setCustomer(customer);
        // Add required entity
        Employee employee;
        employee = em.insert(EmployeeResourceIT.createEntity(em)).block();
        potentialOpportunity.setEmployee(employee);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createEntity(em)).block();
        potentialOpportunity.setProduct(product);
        return potentialOpportunity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PotentialOpportunity createUpdatedEntity(EntityManager em) {
        PotentialOpportunity potentialOpportunity = new PotentialOpportunity().followUp(UPDATED_FOLLOW_UP).status(UPDATED_STATUS);
        // Add required entity
        Customer customer;
        customer = em.insert(CustomerResourceIT.createUpdatedEntity(em)).block();
        potentialOpportunity.setCustomer(customer);
        // Add required entity
        Employee employee;
        employee = em.insert(EmployeeResourceIT.createUpdatedEntity(em)).block();
        potentialOpportunity.setEmployee(employee);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createUpdatedEntity(em)).block();
        potentialOpportunity.setProduct(product);
        return potentialOpportunity;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PotentialOpportunity.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CustomerResourceIT.deleteEntities(em);
        EmployeeResourceIT.deleteEntities(em);
        ProductResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        potentialOpportunity = createEntity(em);
    }

    @Test
    void createPotentialOpportunity() throws Exception {
        int databaseSizeBeforeCreate = potentialOpportunityRepository.findAll().collectList().block().size();
        // Create the PotentialOpportunity
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(potentialOpportunity))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PotentialOpportunity in the database
        List<PotentialOpportunity> potentialOpportunityList = potentialOpportunityRepository.findAll().collectList().block();
        assertThat(potentialOpportunityList).hasSize(databaseSizeBeforeCreate + 1);
        PotentialOpportunity testPotentialOpportunity = potentialOpportunityList.get(potentialOpportunityList.size() - 1);
        assertThat(testPotentialOpportunity.getFollowUp()).isEqualTo(DEFAULT_FOLLOW_UP);
        assertThat(testPotentialOpportunity.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createPotentialOpportunityWithExistingId() throws Exception {
        // Create the PotentialOpportunity with an existing ID
        potentialOpportunity.setId(1L);

        int databaseSizeBeforeCreate = potentialOpportunityRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(potentialOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PotentialOpportunity in the database
        List<PotentialOpportunity> potentialOpportunityList = potentialOpportunityRepository.findAll().collectList().block();
        assertThat(potentialOpportunityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = potentialOpportunityRepository.findAll().collectList().block().size();
        // set the field null
        potentialOpportunity.setStatus(null);

        // Create the PotentialOpportunity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(potentialOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PotentialOpportunity> potentialOpportunityList = potentialOpportunityRepository.findAll().collectList().block();
        assertThat(potentialOpportunityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPotentialOpportunities() {
        // Initialize the database
        potentialOpportunityRepository.save(potentialOpportunity).block();

        // Get all the potentialOpportunityList
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
            .value(hasItem(potentialOpportunity.getId().intValue()))
            .jsonPath("$.[*].followUp")
            .value(hasItem(DEFAULT_FOLLOW_UP))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPotentialOpportunitiesWithEagerRelationshipsIsEnabled() {
        when(potentialOpportunityServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(potentialOpportunityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPotentialOpportunitiesWithEagerRelationshipsIsNotEnabled() {
        when(potentialOpportunityServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(potentialOpportunityRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPotentialOpportunity() {
        // Initialize the database
        potentialOpportunityRepository.save(potentialOpportunity).block();

        // Get the potentialOpportunity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, potentialOpportunity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(potentialOpportunity.getId().intValue()))
            .jsonPath("$.followUp")
            .value(is(DEFAULT_FOLLOW_UP))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingPotentialOpportunity() {
        // Get the potentialOpportunity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPotentialOpportunity() throws Exception {
        // Initialize the database
        potentialOpportunityRepository.save(potentialOpportunity).block();

        int databaseSizeBeforeUpdate = potentialOpportunityRepository.findAll().collectList().block().size();

        // Update the potentialOpportunity
        PotentialOpportunity updatedPotentialOpportunity = potentialOpportunityRepository.findById(potentialOpportunity.getId()).block();
        updatedPotentialOpportunity.followUp(UPDATED_FOLLOW_UP).status(UPDATED_STATUS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPotentialOpportunity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPotentialOpportunity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PotentialOpportunity in the database
        List<PotentialOpportunity> potentialOpportunityList = potentialOpportunityRepository.findAll().collectList().block();
        assertThat(potentialOpportunityList).hasSize(databaseSizeBeforeUpdate);
        PotentialOpportunity testPotentialOpportunity = potentialOpportunityList.get(potentialOpportunityList.size() - 1);
        assertThat(testPotentialOpportunity.getFollowUp()).isEqualTo(UPDATED_FOLLOW_UP);
        assertThat(testPotentialOpportunity.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingPotentialOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = potentialOpportunityRepository.findAll().collectList().block().size();
        potentialOpportunity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, potentialOpportunity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(potentialOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PotentialOpportunity in the database
        List<PotentialOpportunity> potentialOpportunityList = potentialOpportunityRepository.findAll().collectList().block();
        assertThat(potentialOpportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPotentialOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = potentialOpportunityRepository.findAll().collectList().block().size();
        potentialOpportunity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(potentialOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PotentialOpportunity in the database
        List<PotentialOpportunity> potentialOpportunityList = potentialOpportunityRepository.findAll().collectList().block();
        assertThat(potentialOpportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPotentialOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = potentialOpportunityRepository.findAll().collectList().block().size();
        potentialOpportunity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(potentialOpportunity))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PotentialOpportunity in the database
        List<PotentialOpportunity> potentialOpportunityList = potentialOpportunityRepository.findAll().collectList().block();
        assertThat(potentialOpportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePotentialOpportunityWithPatch() throws Exception {
        // Initialize the database
        potentialOpportunityRepository.save(potentialOpportunity).block();

        int databaseSizeBeforeUpdate = potentialOpportunityRepository.findAll().collectList().block().size();

        // Update the potentialOpportunity using partial update
        PotentialOpportunity partialUpdatedPotentialOpportunity = new PotentialOpportunity();
        partialUpdatedPotentialOpportunity.setId(potentialOpportunity.getId());

        partialUpdatedPotentialOpportunity.followUp(UPDATED_FOLLOW_UP).status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPotentialOpportunity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPotentialOpportunity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PotentialOpportunity in the database
        List<PotentialOpportunity> potentialOpportunityList = potentialOpportunityRepository.findAll().collectList().block();
        assertThat(potentialOpportunityList).hasSize(databaseSizeBeforeUpdate);
        PotentialOpportunity testPotentialOpportunity = potentialOpportunityList.get(potentialOpportunityList.size() - 1);
        assertThat(testPotentialOpportunity.getFollowUp()).isEqualTo(UPDATED_FOLLOW_UP);
        assertThat(testPotentialOpportunity.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void fullUpdatePotentialOpportunityWithPatch() throws Exception {
        // Initialize the database
        potentialOpportunityRepository.save(potentialOpportunity).block();

        int databaseSizeBeforeUpdate = potentialOpportunityRepository.findAll().collectList().block().size();

        // Update the potentialOpportunity using partial update
        PotentialOpportunity partialUpdatedPotentialOpportunity = new PotentialOpportunity();
        partialUpdatedPotentialOpportunity.setId(potentialOpportunity.getId());

        partialUpdatedPotentialOpportunity.followUp(UPDATED_FOLLOW_UP).status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPotentialOpportunity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPotentialOpportunity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PotentialOpportunity in the database
        List<PotentialOpportunity> potentialOpportunityList = potentialOpportunityRepository.findAll().collectList().block();
        assertThat(potentialOpportunityList).hasSize(databaseSizeBeforeUpdate);
        PotentialOpportunity testPotentialOpportunity = potentialOpportunityList.get(potentialOpportunityList.size() - 1);
        assertThat(testPotentialOpportunity.getFollowUp()).isEqualTo(UPDATED_FOLLOW_UP);
        assertThat(testPotentialOpportunity.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingPotentialOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = potentialOpportunityRepository.findAll().collectList().block().size();
        potentialOpportunity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, potentialOpportunity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(potentialOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PotentialOpportunity in the database
        List<PotentialOpportunity> potentialOpportunityList = potentialOpportunityRepository.findAll().collectList().block();
        assertThat(potentialOpportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPotentialOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = potentialOpportunityRepository.findAll().collectList().block().size();
        potentialOpportunity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(potentialOpportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PotentialOpportunity in the database
        List<PotentialOpportunity> potentialOpportunityList = potentialOpportunityRepository.findAll().collectList().block();
        assertThat(potentialOpportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPotentialOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = potentialOpportunityRepository.findAll().collectList().block().size();
        potentialOpportunity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(potentialOpportunity))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PotentialOpportunity in the database
        List<PotentialOpportunity> potentialOpportunityList = potentialOpportunityRepository.findAll().collectList().block();
        assertThat(potentialOpportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePotentialOpportunity() {
        // Initialize the database
        potentialOpportunityRepository.save(potentialOpportunity).block();

        int databaseSizeBeforeDelete = potentialOpportunityRepository.findAll().collectList().block().size();

        // Delete the potentialOpportunity
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, potentialOpportunity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PotentialOpportunity> potentialOpportunityList = potentialOpportunityRepository.findAll().collectList().block();
        assertThat(potentialOpportunityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

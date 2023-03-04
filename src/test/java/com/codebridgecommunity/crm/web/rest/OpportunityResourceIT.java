package com.codebridgecommunity.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.codebridgecommunity.crm.IntegrationTest;
import com.codebridgecommunity.crm.domain.Customer;
import com.codebridgecommunity.crm.domain.Employee;
import com.codebridgecommunity.crm.domain.Opportunity;
import com.codebridgecommunity.crm.domain.Product;
import com.codebridgecommunity.crm.domain.enumeration.OppStatus;
import com.codebridgecommunity.crm.repository.EntityManager;
import com.codebridgecommunity.crm.repository.OpportunityRepository;
import com.codebridgecommunity.crm.service.OpportunityService;
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
 * Integration tests for the {@link OpportunityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class OpportunityResourceIT {

    private static final String DEFAULT_FOLLOW_UP = "AAAAAAAAAA";
    private static final String UPDATED_FOLLOW_UP = "BBBBBBBBBB";

    private static final OppStatus DEFAULT_STATUS = OppStatus.MAINTAIN_OPPORTUNITY;
    private static final OppStatus UPDATED_STATUS = OppStatus.WIN;

    private static final String ENTITY_API_URL = "/api/opportunities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Mock
    private OpportunityRepository opportunityRepositoryMock;

    @Mock
    private OpportunityService opportunityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Opportunity opportunity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opportunity createEntity(EntityManager em) {
        Opportunity opportunity = new Opportunity().followUp(DEFAULT_FOLLOW_UP).status(DEFAULT_STATUS);
        // Add required entity
        Customer customer;
        customer = em.insert(CustomerResourceIT.createEntity(em)).block();
        opportunity.setCustomer(customer);
        // Add required entity
        Employee employee;
        employee = em.insert(EmployeeResourceIT.createEntity(em)).block();
        opportunity.setEmployee(employee);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createEntity(em)).block();
        opportunity.setProduct(product);
        return opportunity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opportunity createUpdatedEntity(EntityManager em) {
        Opportunity opportunity = new Opportunity().followUp(UPDATED_FOLLOW_UP).status(UPDATED_STATUS);
        // Add required entity
        Customer customer;
        customer = em.insert(CustomerResourceIT.createUpdatedEntity(em)).block();
        opportunity.setCustomer(customer);
        // Add required entity
        Employee employee;
        employee = em.insert(EmployeeResourceIT.createUpdatedEntity(em)).block();
        opportunity.setEmployee(employee);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createUpdatedEntity(em)).block();
        opportunity.setProduct(product);
        return opportunity;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Opportunity.class).block();
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
        opportunity = createEntity(em);
    }

    @Test
    void createOpportunity() throws Exception {
        int databaseSizeBeforeCreate = opportunityRepository.findAll().collectList().block().size();
        // Create the Opportunity
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(opportunity))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll().collectList().block();
        assertThat(opportunityList).hasSize(databaseSizeBeforeCreate + 1);
        Opportunity testOpportunity = opportunityList.get(opportunityList.size() - 1);
        assertThat(testOpportunity.getFollowUp()).isEqualTo(DEFAULT_FOLLOW_UP);
        assertThat(testOpportunity.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createOpportunityWithExistingId() throws Exception {
        // Create the Opportunity with an existing ID
        opportunity.setId(1L);

        int databaseSizeBeforeCreate = opportunityRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(opportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll().collectList().block();
        assertThat(opportunityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = opportunityRepository.findAll().collectList().block().size();
        // set the field null
        opportunity.setStatus(null);

        // Create the Opportunity, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(opportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Opportunity> opportunityList = opportunityRepository.findAll().collectList().block();
        assertThat(opportunityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllOpportunities() {
        // Initialize the database
        opportunityRepository.save(opportunity).block();

        // Get all the opportunityList
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
            .value(hasItem(opportunity.getId().intValue()))
            .jsonPath("$.[*].followUp")
            .value(hasItem(DEFAULT_FOLLOW_UP))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOpportunitiesWithEagerRelationshipsIsEnabled() {
        when(opportunityServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(opportunityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOpportunitiesWithEagerRelationshipsIsNotEnabled() {
        when(opportunityServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(opportunityRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getOpportunity() {
        // Initialize the database
        opportunityRepository.save(opportunity).block();

        // Get the opportunity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, opportunity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(opportunity.getId().intValue()))
            .jsonPath("$.followUp")
            .value(is(DEFAULT_FOLLOW_UP))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingOpportunity() {
        // Get the opportunity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingOpportunity() throws Exception {
        // Initialize the database
        opportunityRepository.save(opportunity).block();

        int databaseSizeBeforeUpdate = opportunityRepository.findAll().collectList().block().size();

        // Update the opportunity
        Opportunity updatedOpportunity = opportunityRepository.findById(opportunity.getId()).block();
        updatedOpportunity.followUp(UPDATED_FOLLOW_UP).status(UPDATED_STATUS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedOpportunity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedOpportunity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll().collectList().block();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);
        Opportunity testOpportunity = opportunityList.get(opportunityList.size() - 1);
        assertThat(testOpportunity.getFollowUp()).isEqualTo(UPDATED_FOLLOW_UP);
        assertThat(testOpportunity.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = opportunityRepository.findAll().collectList().block().size();
        opportunity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, opportunity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(opportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll().collectList().block();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = opportunityRepository.findAll().collectList().block().size();
        opportunity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(opportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll().collectList().block();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = opportunityRepository.findAll().collectList().block().size();
        opportunity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(opportunity))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll().collectList().block();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOpportunityWithPatch() throws Exception {
        // Initialize the database
        opportunityRepository.save(opportunity).block();

        int databaseSizeBeforeUpdate = opportunityRepository.findAll().collectList().block().size();

        // Update the opportunity using partial update
        Opportunity partialUpdatedOpportunity = new Opportunity();
        partialUpdatedOpportunity.setId(opportunity.getId());

        partialUpdatedOpportunity.followUp(UPDATED_FOLLOW_UP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOpportunity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOpportunity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll().collectList().block();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);
        Opportunity testOpportunity = opportunityList.get(opportunityList.size() - 1);
        assertThat(testOpportunity.getFollowUp()).isEqualTo(UPDATED_FOLLOW_UP);
        assertThat(testOpportunity.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void fullUpdateOpportunityWithPatch() throws Exception {
        // Initialize the database
        opportunityRepository.save(opportunity).block();

        int databaseSizeBeforeUpdate = opportunityRepository.findAll().collectList().block().size();

        // Update the opportunity using partial update
        Opportunity partialUpdatedOpportunity = new Opportunity();
        partialUpdatedOpportunity.setId(opportunity.getId());

        partialUpdatedOpportunity.followUp(UPDATED_FOLLOW_UP).status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOpportunity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOpportunity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll().collectList().block();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);
        Opportunity testOpportunity = opportunityList.get(opportunityList.size() - 1);
        assertThat(testOpportunity.getFollowUp()).isEqualTo(UPDATED_FOLLOW_UP);
        assertThat(testOpportunity.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = opportunityRepository.findAll().collectList().block().size();
        opportunity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, opportunity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(opportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll().collectList().block();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = opportunityRepository.findAll().collectList().block().size();
        opportunity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(opportunity))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll().collectList().block();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = opportunityRepository.findAll().collectList().block().size();
        opportunity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(opportunity))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll().collectList().block();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOpportunity() {
        // Initialize the database
        opportunityRepository.save(opportunity).block();

        int databaseSizeBeforeDelete = opportunityRepository.findAll().collectList().block().size();

        // Delete the opportunity
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, opportunity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Opportunity> opportunityList = opportunityRepository.findAll().collectList().block();
        assertThat(opportunityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

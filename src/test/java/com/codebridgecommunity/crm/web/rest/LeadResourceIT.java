package com.codebridgecommunity.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.codebridgecommunity.crm.IntegrationTest;
import com.codebridgecommunity.crm.domain.Customer;
import com.codebridgecommunity.crm.domain.Employee;
import com.codebridgecommunity.crm.domain.Lead;
import com.codebridgecommunity.crm.domain.Product;
import com.codebridgecommunity.crm.domain.enumeration.LeadStatus;
import com.codebridgecommunity.crm.repository.EntityManager;
import com.codebridgecommunity.crm.repository.LeadRepository;
import com.codebridgecommunity.crm.service.LeadService;
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
 * Integration tests for the {@link LeadResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LeadResourceIT {

    private static final String DEFAULT_FOLLOW_UP = "AAAAAAAAAA";
    private static final String UPDATED_FOLLOW_UP = "BBBBBBBBBB";

    private static final LeadStatus DEFAULT_STATUS = LeadStatus.MAINTAIN_LEAD;
    private static final LeadStatus UPDATED_STATUS = LeadStatus.POTENTIAL_OPPORTUNITY;

    private static final String ENTITY_API_URL = "/api/leads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeadRepository leadRepository;

    @Mock
    private LeadRepository leadRepositoryMock;

    @Mock
    private LeadService leadServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Lead lead;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lead createEntity(EntityManager em) {
        Lead lead = new Lead().followUp(DEFAULT_FOLLOW_UP).status(DEFAULT_STATUS);
        // Add required entity
        Customer customer;
        customer = em.insert(CustomerResourceIT.createEntity(em)).block();
        lead.setCustomer(customer);
        // Add required entity
        Employee employee;
        employee = em.insert(EmployeeResourceIT.createEntity(em)).block();
        lead.setEmployee(employee);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createEntity(em)).block();
        lead.setProduct(product);
        return lead;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lead createUpdatedEntity(EntityManager em) {
        Lead lead = new Lead().followUp(UPDATED_FOLLOW_UP).status(UPDATED_STATUS);
        // Add required entity
        Customer customer;
        customer = em.insert(CustomerResourceIT.createUpdatedEntity(em)).block();
        lead.setCustomer(customer);
        // Add required entity
        Employee employee;
        employee = em.insert(EmployeeResourceIT.createUpdatedEntity(em)).block();
        lead.setEmployee(employee);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createUpdatedEntity(em)).block();
        lead.setProduct(product);
        return lead;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Lead.class).block();
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
        lead = createEntity(em);
    }

    @Test
    void createLead() throws Exception {
        int databaseSizeBeforeCreate = leadRepository.findAll().collectList().block().size();
        // Create the Lead
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(lead))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll().collectList().block();
        assertThat(leadList).hasSize(databaseSizeBeforeCreate + 1);
        Lead testLead = leadList.get(leadList.size() - 1);
        assertThat(testLead.getFollowUp()).isEqualTo(DEFAULT_FOLLOW_UP);
        assertThat(testLead.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createLeadWithExistingId() throws Exception {
        // Create the Lead with an existing ID
        lead.setId(1L);

        int databaseSizeBeforeCreate = leadRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(lead))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll().collectList().block();
        assertThat(leadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = leadRepository.findAll().collectList().block().size();
        // set the field null
        lead.setStatus(null);

        // Create the Lead, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(lead))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Lead> leadList = leadRepository.findAll().collectList().block();
        assertThat(leadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllLeads() {
        // Initialize the database
        leadRepository.save(lead).block();

        // Get all the leadList
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
            .value(hasItem(lead.getId().intValue()))
            .jsonPath("$.[*].followUp")
            .value(hasItem(DEFAULT_FOLLOW_UP))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeadsWithEagerRelationshipsIsEnabled() {
        when(leadServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(leadServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeadsWithEagerRelationshipsIsNotEnabled() {
        when(leadServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(leadRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getLead() {
        // Initialize the database
        leadRepository.save(lead).block();

        // Get the lead
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, lead.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(lead.getId().intValue()))
            .jsonPath("$.followUp")
            .value(is(DEFAULT_FOLLOW_UP))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingLead() {
        // Get the lead
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLead() throws Exception {
        // Initialize the database
        leadRepository.save(lead).block();

        int databaseSizeBeforeUpdate = leadRepository.findAll().collectList().block().size();

        // Update the lead
        Lead updatedLead = leadRepository.findById(lead.getId()).block();
        updatedLead.followUp(UPDATED_FOLLOW_UP).status(UPDATED_STATUS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedLead.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedLead))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll().collectList().block();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
        Lead testLead = leadList.get(leadList.size() - 1);
        assertThat(testLead.getFollowUp()).isEqualTo(UPDATED_FOLLOW_UP);
        assertThat(testLead.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingLead() throws Exception {
        int databaseSizeBeforeUpdate = leadRepository.findAll().collectList().block().size();
        lead.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, lead.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(lead))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll().collectList().block();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLead() throws Exception {
        int databaseSizeBeforeUpdate = leadRepository.findAll().collectList().block().size();
        lead.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(lead))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll().collectList().block();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLead() throws Exception {
        int databaseSizeBeforeUpdate = leadRepository.findAll().collectList().block().size();
        lead.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(lead))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll().collectList().block();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLeadWithPatch() throws Exception {
        // Initialize the database
        leadRepository.save(lead).block();

        int databaseSizeBeforeUpdate = leadRepository.findAll().collectList().block().size();

        // Update the lead using partial update
        Lead partialUpdatedLead = new Lead();
        partialUpdatedLead.setId(lead.getId());

        partialUpdatedLead.status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLead.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLead))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll().collectList().block();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
        Lead testLead = leadList.get(leadList.size() - 1);
        assertThat(testLead.getFollowUp()).isEqualTo(DEFAULT_FOLLOW_UP);
        assertThat(testLead.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void fullUpdateLeadWithPatch() throws Exception {
        // Initialize the database
        leadRepository.save(lead).block();

        int databaseSizeBeforeUpdate = leadRepository.findAll().collectList().block().size();

        // Update the lead using partial update
        Lead partialUpdatedLead = new Lead();
        partialUpdatedLead.setId(lead.getId());

        partialUpdatedLead.followUp(UPDATED_FOLLOW_UP).status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLead.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLead))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll().collectList().block();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
        Lead testLead = leadList.get(leadList.size() - 1);
        assertThat(testLead.getFollowUp()).isEqualTo(UPDATED_FOLLOW_UP);
        assertThat(testLead.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingLead() throws Exception {
        int databaseSizeBeforeUpdate = leadRepository.findAll().collectList().block().size();
        lead.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, lead.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(lead))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll().collectList().block();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLead() throws Exception {
        int databaseSizeBeforeUpdate = leadRepository.findAll().collectList().block().size();
        lead.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(lead))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll().collectList().block();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLead() throws Exception {
        int databaseSizeBeforeUpdate = leadRepository.findAll().collectList().block().size();
        lead.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(lead))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Lead in the database
        List<Lead> leadList = leadRepository.findAll().collectList().block();
        assertThat(leadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLead() {
        // Initialize the database
        leadRepository.save(lead).block();

        int databaseSizeBeforeDelete = leadRepository.findAll().collectList().block().size();

        // Delete the lead
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, lead.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Lead> leadList = leadRepository.findAll().collectList().block();
        assertThat(leadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

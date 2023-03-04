package com.codebridgecommunity.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.codebridgecommunity.crm.IntegrationTest;
import com.codebridgecommunity.crm.domain.Customer;
import com.codebridgecommunity.crm.domain.enumeration.Gender;
import com.codebridgecommunity.crm.domain.enumeration.SocMed;
import com.codebridgecommunity.crm.repository.CustomerRepository;
import com.codebridgecommunity.crm.repository.EntityManager;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CustomerResourceIT {

    private static final String DEFAULT_CUST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUST_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_CUST_IC = "AAAAAAAAAA";
    private static final String UPDATED_CUST_IC = "BBBBBBBBBB";

    private static final String DEFAULT_CUST_EMAIL = "E&7R(@dZ,Ecom";
    private static final String UPDATED_CUST_EMAIL = "WD@k\"`com";

    private static final String DEFAULT_CUST_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_CUST_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CUST_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_CUST_JOB_TITLE = "BBBBBBBBBB";

    private static final SocMed DEFAULT_ENGAGE = SocMed.Instagram;
    private static final SocMed UPDATED_ENGAGE = SocMed.LinkedIn;

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Customer customer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .custName(DEFAULT_CUST_NAME)
            .gender(DEFAULT_GENDER)
            .custIC(DEFAULT_CUST_IC)
            .custEmail(DEFAULT_CUST_EMAIL)
            .custPhone(DEFAULT_CUST_PHONE)
            .companyName(DEFAULT_COMPANY_NAME)
            .custJobTitle(DEFAULT_CUST_JOB_TITLE)
            .engage(DEFAULT_ENGAGE);
        return customer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .custName(UPDATED_CUST_NAME)
            .gender(UPDATED_GENDER)
            .custIC(UPDATED_CUST_IC)
            .custEmail(UPDATED_CUST_EMAIL)
            .custPhone(UPDATED_CUST_PHONE)
            .companyName(UPDATED_COMPANY_NAME)
            .custJobTitle(UPDATED_CUST_JOB_TITLE)
            .engage(UPDATED_ENGAGE);
        return customer;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Customer.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        customer = createEntity(em);
    }

    @Test
    void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().collectList().block().size();
        // Create the Customer
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustName()).isEqualTo(DEFAULT_CUST_NAME);
        assertThat(testCustomer.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testCustomer.getCustIC()).isEqualTo(DEFAULT_CUST_IC);
        assertThat(testCustomer.getCustEmail()).isEqualTo(DEFAULT_CUST_EMAIL);
        assertThat(testCustomer.getCustPhone()).isEqualTo(DEFAULT_CUST_PHONE);
        assertThat(testCustomer.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testCustomer.getCustJobTitle()).isEqualTo(DEFAULT_CUST_JOB_TITLE);
        assertThat(testCustomer.getEngage()).isEqualTo(DEFAULT_ENGAGE);
    }

    @Test
    void createCustomerWithExistingId() throws Exception {
        // Create the Customer with an existing ID
        customer.setId(1L);

        int databaseSizeBeforeCreate = customerRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCustNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().collectList().block().size();
        // set the field null
        customer.setCustName(null);

        // Create the Customer, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().collectList().block().size();
        // set the field null
        customer.setGender(null);

        // Create the Customer, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCustICIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().collectList().block().size();
        // set the field null
        customer.setCustIC(null);

        // Create the Customer, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCustEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().collectList().block().size();
        // set the field null
        customer.setCustEmail(null);

        // Create the Customer, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCustPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().collectList().block().size();
        // set the field null
        customer.setCustPhone(null);

        // Create the Customer, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().collectList().block().size();
        // set the field null
        customer.setCompanyName(null);

        // Create the Customer, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCustJobTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().collectList().block().size();
        // set the field null
        customer.setCustJobTitle(null);

        // Create the Customer, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCustomers() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList
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
            .value(hasItem(customer.getId().intValue()))
            .jsonPath("$.[*].custName")
            .value(hasItem(DEFAULT_CUST_NAME))
            .jsonPath("$.[*].gender")
            .value(hasItem(DEFAULT_GENDER.toString()))
            .jsonPath("$.[*].custIC")
            .value(hasItem(DEFAULT_CUST_IC))
            .jsonPath("$.[*].custEmail")
            .value(hasItem(DEFAULT_CUST_EMAIL))
            .jsonPath("$.[*].custPhone")
            .value(hasItem(DEFAULT_CUST_PHONE))
            .jsonPath("$.[*].companyName")
            .value(hasItem(DEFAULT_COMPANY_NAME))
            .jsonPath("$.[*].custJobTitle")
            .value(hasItem(DEFAULT_CUST_JOB_TITLE))
            .jsonPath("$.[*].engage")
            .value(hasItem(DEFAULT_ENGAGE.toString()));
    }

    @Test
    void getCustomer() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get the customer
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, customer.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(customer.getId().intValue()))
            .jsonPath("$.custName")
            .value(is(DEFAULT_CUST_NAME))
            .jsonPath("$.gender")
            .value(is(DEFAULT_GENDER.toString()))
            .jsonPath("$.custIC")
            .value(is(DEFAULT_CUST_IC))
            .jsonPath("$.custEmail")
            .value(is(DEFAULT_CUST_EMAIL))
            .jsonPath("$.custPhone")
            .value(is(DEFAULT_CUST_PHONE))
            .jsonPath("$.companyName")
            .value(is(DEFAULT_COMPANY_NAME))
            .jsonPath("$.custJobTitle")
            .value(is(DEFAULT_CUST_JOB_TITLE))
            .jsonPath("$.engage")
            .value(is(DEFAULT_ENGAGE.toString()));
    }

    @Test
    void getNonExistingCustomer() {
        // Get the customer
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCustomer() throws Exception {
        // Initialize the database
        customerRepository.save(customer).block();

        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).block();
        updatedCustomer
            .custName(UPDATED_CUST_NAME)
            .gender(UPDATED_GENDER)
            .custIC(UPDATED_CUST_IC)
            .custEmail(UPDATED_CUST_EMAIL)
            .custPhone(UPDATED_CUST_PHONE)
            .companyName(UPDATED_COMPANY_NAME)
            .custJobTitle(UPDATED_CUST_JOB_TITLE)
            .engage(UPDATED_ENGAGE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCustomer.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCustomer))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustName()).isEqualTo(UPDATED_CUST_NAME);
        assertThat(testCustomer.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCustomer.getCustIC()).isEqualTo(UPDATED_CUST_IC);
        assertThat(testCustomer.getCustEmail()).isEqualTo(UPDATED_CUST_EMAIL);
        assertThat(testCustomer.getCustPhone()).isEqualTo(UPDATED_CUST_PHONE);
        assertThat(testCustomer.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCustomer.getCustJobTitle()).isEqualTo(UPDATED_CUST_JOB_TITLE);
        assertThat(testCustomer.getEngage()).isEqualTo(UPDATED_ENGAGE);
    }

    @Test
    void putNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();
        customer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, customer.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.save(customer).block();

        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer.custIC(UPDATED_CUST_IC).engage(UPDATED_ENGAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustName()).isEqualTo(DEFAULT_CUST_NAME);
        assertThat(testCustomer.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testCustomer.getCustIC()).isEqualTo(UPDATED_CUST_IC);
        assertThat(testCustomer.getCustEmail()).isEqualTo(DEFAULT_CUST_EMAIL);
        assertThat(testCustomer.getCustPhone()).isEqualTo(DEFAULT_CUST_PHONE);
        assertThat(testCustomer.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testCustomer.getCustJobTitle()).isEqualTo(DEFAULT_CUST_JOB_TITLE);
        assertThat(testCustomer.getEngage()).isEqualTo(UPDATED_ENGAGE);
    }

    @Test
    void fullUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.save(customer).block();

        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .custName(UPDATED_CUST_NAME)
            .gender(UPDATED_GENDER)
            .custIC(UPDATED_CUST_IC)
            .custEmail(UPDATED_CUST_EMAIL)
            .custPhone(UPDATED_CUST_PHONE)
            .companyName(UPDATED_COMPANY_NAME)
            .custJobTitle(UPDATED_CUST_JOB_TITLE)
            .engage(UPDATED_ENGAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustName()).isEqualTo(UPDATED_CUST_NAME);
        assertThat(testCustomer.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCustomer.getCustIC()).isEqualTo(UPDATED_CUST_IC);
        assertThat(testCustomer.getCustEmail()).isEqualTo(UPDATED_CUST_EMAIL);
        assertThat(testCustomer.getCustPhone()).isEqualTo(UPDATED_CUST_PHONE);
        assertThat(testCustomer.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCustomer.getCustJobTitle()).isEqualTo(UPDATED_CUST_JOB_TITLE);
        assertThat(testCustomer.getEngage()).isEqualTo(UPDATED_ENGAGE);
    }

    @Test
    void patchNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();
        customer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, customer.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCustomer() {
        // Initialize the database
        customerRepository.save(customer).block();

        int databaseSizeBeforeDelete = customerRepository.findAll().collectList().block().size();

        // Delete the customer
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, customer.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

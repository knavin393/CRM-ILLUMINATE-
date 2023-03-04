package com.codebridgecommunity.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.codebridgecommunity.crm.IntegrationTest;
import com.codebridgecommunity.crm.domain.Employee;
import com.codebridgecommunity.crm.domain.enumeration.Gender;
import com.codebridgecommunity.crm.domain.enumeration.SocMed;
import com.codebridgecommunity.crm.repository.EmployeeRepository;
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
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EmployeeResourceIT {

    private static final String DEFAULT_EMP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMP_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_EMP_AGE = 20;
    private static final Integer UPDATED_EMP_AGE = 21;

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_EMP_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_EMP_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_EMP_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_EMP_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMP_EMAIL = "Y_%@?r|ncom";
    private static final String UPDATED_EMP_EMAIL = "57%t,&@%9com";

    private static final SocMed DEFAULT_ENGAGE = SocMed.Instagram;
    private static final SocMed UPDATED_ENGAGE = SocMed.LinkedIn;

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .empName(DEFAULT_EMP_NAME)
            .empAge(DEFAULT_EMP_AGE)
            .gender(DEFAULT_GENDER)
            .empJobTitle(DEFAULT_EMP_JOB_TITLE)
            .empPhone(DEFAULT_EMP_PHONE)
            .empEmail(DEFAULT_EMP_EMAIL)
            .engage(DEFAULT_ENGAGE);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .empName(UPDATED_EMP_NAME)
            .empAge(UPDATED_EMP_AGE)
            .gender(UPDATED_GENDER)
            .empJobTitle(UPDATED_EMP_JOB_TITLE)
            .empPhone(UPDATED_EMP_PHONE)
            .empEmail(UPDATED_EMP_EMAIL)
            .engage(UPDATED_ENGAGE);
        return employee;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Employee.class).block();
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
        employee = createEntity(em);
    }

    @Test
    void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().collectList().block().size();
        // Create the Employee
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmpName()).isEqualTo(DEFAULT_EMP_NAME);
        assertThat(testEmployee.getEmpAge()).isEqualTo(DEFAULT_EMP_AGE);
        assertThat(testEmployee.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testEmployee.getEmpJobTitle()).isEqualTo(DEFAULT_EMP_JOB_TITLE);
        assertThat(testEmployee.getEmpPhone()).isEqualTo(DEFAULT_EMP_PHONE);
        assertThat(testEmployee.getEmpEmail()).isEqualTo(DEFAULT_EMP_EMAIL);
        assertThat(testEmployee.getEngage()).isEqualTo(DEFAULT_ENGAGE);
    }

    @Test
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);

        int databaseSizeBeforeCreate = employeeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkEmpNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setEmpName(null);

        // Create the Employee, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmpAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setEmpAge(null);

        // Create the Employee, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setGender(null);

        // Create the Employee, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmpJobTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setEmpJobTitle(null);

        // Create the Employee, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmpPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setEmpPhone(null);

        // Create the Employee, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmpEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setEmpEmail(null);

        // Create the Employee, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEmployees() {
        // Initialize the database
        employeeRepository.save(employee).block();

        // Get all the employeeList
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
            .value(hasItem(employee.getId().intValue()))
            .jsonPath("$.[*].empName")
            .value(hasItem(DEFAULT_EMP_NAME))
            .jsonPath("$.[*].empAge")
            .value(hasItem(DEFAULT_EMP_AGE))
            .jsonPath("$.[*].gender")
            .value(hasItem(DEFAULT_GENDER.toString()))
            .jsonPath("$.[*].empJobTitle")
            .value(hasItem(DEFAULT_EMP_JOB_TITLE))
            .jsonPath("$.[*].empPhone")
            .value(hasItem(DEFAULT_EMP_PHONE))
            .jsonPath("$.[*].empEmail")
            .value(hasItem(DEFAULT_EMP_EMAIL))
            .jsonPath("$.[*].engage")
            .value(hasItem(DEFAULT_ENGAGE.toString()));
    }

    @Test
    void getEmployee() {
        // Initialize the database
        employeeRepository.save(employee).block();

        // Get the employee
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, employee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(employee.getId().intValue()))
            .jsonPath("$.empName")
            .value(is(DEFAULT_EMP_NAME))
            .jsonPath("$.empAge")
            .value(is(DEFAULT_EMP_AGE))
            .jsonPath("$.gender")
            .value(is(DEFAULT_GENDER.toString()))
            .jsonPath("$.empJobTitle")
            .value(is(DEFAULT_EMP_JOB_TITLE))
            .jsonPath("$.empPhone")
            .value(is(DEFAULT_EMP_PHONE))
            .jsonPath("$.empEmail")
            .value(is(DEFAULT_EMP_EMAIL))
            .jsonPath("$.engage")
            .value(is(DEFAULT_ENGAGE.toString()));
    }

    @Test
    void getNonExistingEmployee() {
        // Get the employee
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).block();
        updatedEmployee
            .empName(UPDATED_EMP_NAME)
            .empAge(UPDATED_EMP_AGE)
            .gender(UPDATED_GENDER)
            .empJobTitle(UPDATED_EMP_JOB_TITLE)
            .empPhone(UPDATED_EMP_PHONE)
            .empEmail(UPDATED_EMP_EMAIL)
            .engage(UPDATED_ENGAGE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedEmployee.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedEmployee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmpName()).isEqualTo(UPDATED_EMP_NAME);
        assertThat(testEmployee.getEmpAge()).isEqualTo(UPDATED_EMP_AGE);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getEmpJobTitle()).isEqualTo(UPDATED_EMP_JOB_TITLE);
        assertThat(testEmployee.getEmpPhone()).isEqualTo(UPDATED_EMP_PHONE);
        assertThat(testEmployee.getEmpEmail()).isEqualTo(UPDATED_EMP_EMAIL);
        assertThat(testEmployee.getEngage()).isEqualTo(UPDATED_ENGAGE);
    }

    @Test
    void putNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, employee.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee.engage(UPDATED_ENGAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmpName()).isEqualTo(DEFAULT_EMP_NAME);
        assertThat(testEmployee.getEmpAge()).isEqualTo(DEFAULT_EMP_AGE);
        assertThat(testEmployee.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testEmployee.getEmpJobTitle()).isEqualTo(DEFAULT_EMP_JOB_TITLE);
        assertThat(testEmployee.getEmpPhone()).isEqualTo(DEFAULT_EMP_PHONE);
        assertThat(testEmployee.getEmpEmail()).isEqualTo(DEFAULT_EMP_EMAIL);
        assertThat(testEmployee.getEngage()).isEqualTo(UPDATED_ENGAGE);
    }

    @Test
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .empName(UPDATED_EMP_NAME)
            .empAge(UPDATED_EMP_AGE)
            .gender(UPDATED_GENDER)
            .empJobTitle(UPDATED_EMP_JOB_TITLE)
            .empPhone(UPDATED_EMP_PHONE)
            .empEmail(UPDATED_EMP_EMAIL)
            .engage(UPDATED_ENGAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmpName()).isEqualTo(UPDATED_EMP_NAME);
        assertThat(testEmployee.getEmpAge()).isEqualTo(UPDATED_EMP_AGE);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getEmpJobTitle()).isEqualTo(UPDATED_EMP_JOB_TITLE);
        assertThat(testEmployee.getEmpPhone()).isEqualTo(UPDATED_EMP_PHONE);
        assertThat(testEmployee.getEmpEmail()).isEqualTo(UPDATED_EMP_EMAIL);
        assertThat(testEmployee.getEngage()).isEqualTo(UPDATED_ENGAGE);
    }

    @Test
    void patchNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, employee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEmployee() {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeDelete = employeeRepository.findAll().collectList().block().size();

        // Delete the employee
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, employee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

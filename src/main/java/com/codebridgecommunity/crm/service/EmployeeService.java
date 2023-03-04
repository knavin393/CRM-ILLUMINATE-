package com.codebridgecommunity.crm.service;

import com.codebridgecommunity.crm.domain.Employee;
import com.codebridgecommunity.crm.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
@Transactional
public class EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Save a employee.
     *
     * @param employee the entity to save.
     * @return the persisted entity.
     */
    public Mono<Employee> save(Employee employee) {
        log.debug("Request to save Employee : {}", employee);
        return employeeRepository.save(employee);
    }

    /**
     * Update a employee.
     *
     * @param employee the entity to save.
     * @return the persisted entity.
     */
    public Mono<Employee> update(Employee employee) {
        log.debug("Request to update Employee : {}", employee);
        return employeeRepository.save(employee);
    }

    /**
     * Partially update a employee.
     *
     * @param employee the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Employee> partialUpdate(Employee employee) {
        log.debug("Request to partially update Employee : {}", employee);

        return employeeRepository
            .findById(employee.getId())
            .map(existingEmployee -> {
                if (employee.getEmpName() != null) {
                    existingEmployee.setEmpName(employee.getEmpName());
                }
                if (employee.getEmpAge() != null) {
                    existingEmployee.setEmpAge(employee.getEmpAge());
                }
                if (employee.getGender() != null) {
                    existingEmployee.setGender(employee.getGender());
                }
                if (employee.getEmpJobTitle() != null) {
                    existingEmployee.setEmpJobTitle(employee.getEmpJobTitle());
                }
                if (employee.getEmpPhone() != null) {
                    existingEmployee.setEmpPhone(employee.getEmpPhone());
                }
                if (employee.getEmpEmail() != null) {
                    existingEmployee.setEmpEmail(employee.getEmpEmail());
                }
                if (employee.getEngage() != null) {
                    existingEmployee.setEngage(employee.getEngage());
                }

                return existingEmployee;
            })
            .flatMap(employeeRepository::save);
    }

    /**
     * Get all the employees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Employee> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeeRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of employees available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return employeeRepository.count();
    }

    /**
     * Get one employee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Employee> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findById(id);
    }

    /**
     * Delete the employee by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        return employeeRepository.deleteById(id);
    }
}

package com.codebridgecommunity.crm.service;

import com.codebridgecommunity.crm.domain.Company;
import com.codebridgecommunity.crm.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Company}.
 */
@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * Save a company.
     *
     * @param company the entity to save.
     * @return the persisted entity.
     */
    public Mono<Company> save(Company company) {
        log.debug("Request to save Company : {}", company);
        return companyRepository.save(company);
    }

    /**
     * Update a company.
     *
     * @param company the entity to save.
     * @return the persisted entity.
     */
    public Mono<Company> update(Company company) {
        log.debug("Request to update Company : {}", company);
        return companyRepository.save(company);
    }

    /**
     * Partially update a company.
     *
     * @param company the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Company> partialUpdate(Company company) {
        log.debug("Request to partially update Company : {}", company);

        return companyRepository
            .findById(company.getId())
            .map(existingCompany -> {
                if (company.getIndustry() != null) {
                    existingCompany.setIndustry(company.getIndustry());
                }
                if (company.getLocation() != null) {
                    existingCompany.setLocation(company.getLocation());
                }
                if (company.getEstablished() != null) {
                    existingCompany.setEstablished(company.getEstablished());
                }
                if (company.getEngage() != null) {
                    existingCompany.setEngage(company.getEngage());
                }

                return existingCompany;
            })
            .flatMap(companyRepository::save);
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Company> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companyRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of companies available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return companyRepository.count();
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Company> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        return companyRepository.deleteById(id);
    }
}

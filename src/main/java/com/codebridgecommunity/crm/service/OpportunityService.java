package com.codebridgecommunity.crm.service;

import com.codebridgecommunity.crm.domain.Opportunity;
import com.codebridgecommunity.crm.repository.OpportunityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Opportunity}.
 */
@Service
@Transactional
public class OpportunityService {

    private final Logger log = LoggerFactory.getLogger(OpportunityService.class);

    private final OpportunityRepository opportunityRepository;

    public OpportunityService(OpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    /**
     * Save a opportunity.
     *
     * @param opportunity the entity to save.
     * @return the persisted entity.
     */
    public Mono<Opportunity> save(Opportunity opportunity) {
        log.debug("Request to save Opportunity : {}", opportunity);
        return opportunityRepository.save(opportunity);
    }

    /**
     * Update a opportunity.
     *
     * @param opportunity the entity to save.
     * @return the persisted entity.
     */
    public Mono<Opportunity> update(Opportunity opportunity) {
        log.debug("Request to update Opportunity : {}", opportunity);
        return opportunityRepository.save(opportunity);
    }

    /**
     * Partially update a opportunity.
     *
     * @param opportunity the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Opportunity> partialUpdate(Opportunity opportunity) {
        log.debug("Request to partially update Opportunity : {}", opportunity);

        return opportunityRepository
            .findById(opportunity.getId())
            .map(existingOpportunity -> {
                if (opportunity.getFollowUp() != null) {
                    existingOpportunity.setFollowUp(opportunity.getFollowUp());
                }
                if (opportunity.getStatus() != null) {
                    existingOpportunity.setStatus(opportunity.getStatus());
                }

                return existingOpportunity;
            })
            .flatMap(opportunityRepository::save);
    }

    /**
     * Get all the opportunities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Opportunity> findAll(Pageable pageable) {
        log.debug("Request to get all Opportunities");
        return opportunityRepository.findAllBy(pageable);
    }

    /**
     * Get all the opportunities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<Opportunity> findAllWithEagerRelationships(Pageable pageable) {
        return opportunityRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Returns the number of opportunities available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return opportunityRepository.count();
    }

    /**
     * Get one opportunity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Opportunity> findOne(Long id) {
        log.debug("Request to get Opportunity : {}", id);
        return opportunityRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the opportunity by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Opportunity : {}", id);
        return opportunityRepository.deleteById(id);
    }
}

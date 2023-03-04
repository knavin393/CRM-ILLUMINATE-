package com.codebridgecommunity.crm.service;

import com.codebridgecommunity.crm.domain.Lead;
import com.codebridgecommunity.crm.repository.LeadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Lead}.
 */
@Service
@Transactional
public class LeadService {

    private final Logger log = LoggerFactory.getLogger(LeadService.class);

    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    /**
     * Save a lead.
     *
     * @param lead the entity to save.
     * @return the persisted entity.
     */
    public Mono<Lead> save(Lead lead) {
        log.debug("Request to save Lead : {}", lead);
        return leadRepository.save(lead);
    }

    /**
     * Update a lead.
     *
     * @param lead the entity to save.
     * @return the persisted entity.
     */
    public Mono<Lead> update(Lead lead) {
        log.debug("Request to update Lead : {}", lead);
        return leadRepository.save(lead);
    }

    /**
     * Partially update a lead.
     *
     * @param lead the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Lead> partialUpdate(Lead lead) {
        log.debug("Request to partially update Lead : {}", lead);

        return leadRepository
            .findById(lead.getId())
            .map(existingLead -> {
                if (lead.getFollowUp() != null) {
                    existingLead.setFollowUp(lead.getFollowUp());
                }
                if (lead.getStatus() != null) {
                    existingLead.setStatus(lead.getStatus());
                }

                return existingLead;
            })
            .flatMap(leadRepository::save);
    }

    /**
     * Get all the leads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Lead> findAll(Pageable pageable) {
        log.debug("Request to get all Leads");
        return leadRepository.findAllBy(pageable);
    }

    /**
     * Get all the leads with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<Lead> findAllWithEagerRelationships(Pageable pageable) {
        return leadRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Returns the number of leads available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return leadRepository.count();
    }

    /**
     * Get one lead by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Lead> findOne(Long id) {
        log.debug("Request to get Lead : {}", id);
        return leadRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the lead by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Lead : {}", id);
        return leadRepository.deleteById(id);
    }
}

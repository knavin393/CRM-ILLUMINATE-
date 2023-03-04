package com.codebridgecommunity.crm.service;

import com.codebridgecommunity.crm.domain.PotentialOpportunity;
import com.codebridgecommunity.crm.repository.PotentialOpportunityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link PotentialOpportunity}.
 */
@Service
@Transactional
public class PotentialOpportunityService {

    private final Logger log = LoggerFactory.getLogger(PotentialOpportunityService.class);

    private final PotentialOpportunityRepository potentialOpportunityRepository;

    public PotentialOpportunityService(PotentialOpportunityRepository potentialOpportunityRepository) {
        this.potentialOpportunityRepository = potentialOpportunityRepository;
    }

    /**
     * Save a potentialOpportunity.
     *
     * @param potentialOpportunity the entity to save.
     * @return the persisted entity.
     */
    public Mono<PotentialOpportunity> save(PotentialOpportunity potentialOpportunity) {
        log.debug("Request to save PotentialOpportunity : {}", potentialOpportunity);
        return potentialOpportunityRepository.save(potentialOpportunity);
    }

    /**
     * Update a potentialOpportunity.
     *
     * @param potentialOpportunity the entity to save.
     * @return the persisted entity.
     */
    public Mono<PotentialOpportunity> update(PotentialOpportunity potentialOpportunity) {
        log.debug("Request to update PotentialOpportunity : {}", potentialOpportunity);
        return potentialOpportunityRepository.save(potentialOpportunity);
    }

    /**
     * Partially update a potentialOpportunity.
     *
     * @param potentialOpportunity the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PotentialOpportunity> partialUpdate(PotentialOpportunity potentialOpportunity) {
        log.debug("Request to partially update PotentialOpportunity : {}", potentialOpportunity);

        return potentialOpportunityRepository
            .findById(potentialOpportunity.getId())
            .map(existingPotentialOpportunity -> {
                if (potentialOpportunity.getFollowUp() != null) {
                    existingPotentialOpportunity.setFollowUp(potentialOpportunity.getFollowUp());
                }
                if (potentialOpportunity.getStatus() != null) {
                    existingPotentialOpportunity.setStatus(potentialOpportunity.getStatus());
                }

                return existingPotentialOpportunity;
            })
            .flatMap(potentialOpportunityRepository::save);
    }

    /**
     * Get all the potentialOpportunities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PotentialOpportunity> findAll(Pageable pageable) {
        log.debug("Request to get all PotentialOpportunities");
        return potentialOpportunityRepository.findAllBy(pageable);
    }

    /**
     * Get all the potentialOpportunities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<PotentialOpportunity> findAllWithEagerRelationships(Pageable pageable) {
        return potentialOpportunityRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Returns the number of potentialOpportunities available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return potentialOpportunityRepository.count();
    }

    /**
     * Get one potentialOpportunity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PotentialOpportunity> findOne(Long id) {
        log.debug("Request to get PotentialOpportunity : {}", id);
        return potentialOpportunityRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the potentialOpportunity by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PotentialOpportunity : {}", id);
        return potentialOpportunityRepository.deleteById(id);
    }
}

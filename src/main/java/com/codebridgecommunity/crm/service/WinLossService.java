package com.codebridgecommunity.crm.service;

import com.codebridgecommunity.crm.domain.WinLoss;
import com.codebridgecommunity.crm.repository.WinLossRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link WinLoss}.
 */
@Service
@Transactional
public class WinLossService {

    private final Logger log = LoggerFactory.getLogger(WinLossService.class);

    private final WinLossRepository winLossRepository;

    public WinLossService(WinLossRepository winLossRepository) {
        this.winLossRepository = winLossRepository;
    }

    /**
     * Save a winLoss.
     *
     * @param winLoss the entity to save.
     * @return the persisted entity.
     */
    public Mono<WinLoss> save(WinLoss winLoss) {
        log.debug("Request to save WinLoss : {}", winLoss);
        return winLossRepository.save(winLoss);
    }

    /**
     * Update a winLoss.
     *
     * @param winLoss the entity to save.
     * @return the persisted entity.
     */
    public Mono<WinLoss> update(WinLoss winLoss) {
        log.debug("Request to update WinLoss : {}", winLoss);
        return winLossRepository.save(winLoss);
    }

    /**
     * Partially update a winLoss.
     *
     * @param winLoss the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WinLoss> partialUpdate(WinLoss winLoss) {
        log.debug("Request to partially update WinLoss : {}", winLoss);

        return winLossRepository
            .findById(winLoss.getId())
            .map(existingWinLoss -> {
                if (winLoss.getNotes() != null) {
                    existingWinLoss.setNotes(winLoss.getNotes());
                }

                return existingWinLoss;
            })
            .flatMap(winLossRepository::save);
    }

    /**
     * Get all the winLosses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WinLoss> findAll(Pageable pageable) {
        log.debug("Request to get all WinLosses");
        return winLossRepository.findAllBy(pageable);
    }

    /**
     * Get all the winLosses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<WinLoss> findAllWithEagerRelationships(Pageable pageable) {
        return winLossRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Returns the number of winLosses available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return winLossRepository.count();
    }

    /**
     * Get one winLoss by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<WinLoss> findOne(Long id) {
        log.debug("Request to get WinLoss : {}", id);
        return winLossRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the winLoss by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete WinLoss : {}", id);
        return winLossRepository.deleteById(id);
    }
}

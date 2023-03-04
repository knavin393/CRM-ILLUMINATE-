package com.codebridgecommunity.crm.web.rest;

import com.codebridgecommunity.crm.domain.PotentialOpportunity;
import com.codebridgecommunity.crm.repository.PotentialOpportunityRepository;
import com.codebridgecommunity.crm.service.PotentialOpportunityService;
import com.codebridgecommunity.crm.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.codebridgecommunity.crm.domain.PotentialOpportunity}.
 */
@RestController
@RequestMapping("/api")
public class PotentialOpportunityResource {

    private final Logger log = LoggerFactory.getLogger(PotentialOpportunityResource.class);

    private static final String ENTITY_NAME = "potentialOpportunity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PotentialOpportunityService potentialOpportunityService;

    private final PotentialOpportunityRepository potentialOpportunityRepository;

    public PotentialOpportunityResource(
        PotentialOpportunityService potentialOpportunityService,
        PotentialOpportunityRepository potentialOpportunityRepository
    ) {
        this.potentialOpportunityService = potentialOpportunityService;
        this.potentialOpportunityRepository = potentialOpportunityRepository;
    }

    /**
     * {@code POST  /potential-opportunities} : Create a new potentialOpportunity.
     *
     * @param potentialOpportunity the potentialOpportunity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new potentialOpportunity, or with status {@code 400 (Bad Request)} if the potentialOpportunity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/potential-opportunities")
    public Mono<ResponseEntity<PotentialOpportunity>> createPotentialOpportunity(
        @Valid @RequestBody PotentialOpportunity potentialOpportunity
    ) throws URISyntaxException {
        log.debug("REST request to save PotentialOpportunity : {}", potentialOpportunity);
        if (potentialOpportunity.getId() != null) {
            throw new BadRequestAlertException("A new potentialOpportunity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return potentialOpportunityService
            .save(potentialOpportunity)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/potential-opportunities/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /potential-opportunities/:id} : Updates an existing potentialOpportunity.
     *
     * @param id the id of the potentialOpportunity to save.
     * @param potentialOpportunity the potentialOpportunity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated potentialOpportunity,
     * or with status {@code 400 (Bad Request)} if the potentialOpportunity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the potentialOpportunity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/potential-opportunities/{id}")
    public Mono<ResponseEntity<PotentialOpportunity>> updatePotentialOpportunity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PotentialOpportunity potentialOpportunity
    ) throws URISyntaxException {
        log.debug("REST request to update PotentialOpportunity : {}, {}", id, potentialOpportunity);
        if (potentialOpportunity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, potentialOpportunity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return potentialOpportunityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return potentialOpportunityService
                    .update(potentialOpportunity)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /potential-opportunities/:id} : Partial updates given fields of an existing potentialOpportunity, field will ignore if it is null
     *
     * @param id the id of the potentialOpportunity to save.
     * @param potentialOpportunity the potentialOpportunity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated potentialOpportunity,
     * or with status {@code 400 (Bad Request)} if the potentialOpportunity is not valid,
     * or with status {@code 404 (Not Found)} if the potentialOpportunity is not found,
     * or with status {@code 500 (Internal Server Error)} if the potentialOpportunity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/potential-opportunities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PotentialOpportunity>> partialUpdatePotentialOpportunity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PotentialOpportunity potentialOpportunity
    ) throws URISyntaxException {
        log.debug("REST request to partial update PotentialOpportunity partially : {}, {}", id, potentialOpportunity);
        if (potentialOpportunity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, potentialOpportunity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return potentialOpportunityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PotentialOpportunity> result = potentialOpportunityService.partialUpdate(potentialOpportunity);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /potential-opportunities} : get all the potentialOpportunities.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of potentialOpportunities in body.
     */
    @GetMapping("/potential-opportunities")
    public Mono<ResponseEntity<List<PotentialOpportunity>>> getAllPotentialOpportunities(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of PotentialOpportunities");
        return potentialOpportunityService
            .countAll()
            .zipWith(potentialOpportunityService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /potential-opportunities/:id} : get the "id" potentialOpportunity.
     *
     * @param id the id of the potentialOpportunity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the potentialOpportunity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/potential-opportunities/{id}")
    public Mono<ResponseEntity<PotentialOpportunity>> getPotentialOpportunity(@PathVariable Long id) {
        log.debug("REST request to get PotentialOpportunity : {}", id);
        Mono<PotentialOpportunity> potentialOpportunity = potentialOpportunityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(potentialOpportunity);
    }

    /**
     * {@code DELETE  /potential-opportunities/:id} : delete the "id" potentialOpportunity.
     *
     * @param id the id of the potentialOpportunity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/potential-opportunities/{id}")
    public Mono<ResponseEntity<Void>> deletePotentialOpportunity(@PathVariable Long id) {
        log.debug("REST request to delete PotentialOpportunity : {}", id);
        return potentialOpportunityService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}

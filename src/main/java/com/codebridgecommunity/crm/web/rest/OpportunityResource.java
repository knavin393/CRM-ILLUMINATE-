package com.codebridgecommunity.crm.web.rest;

import com.codebridgecommunity.crm.domain.Opportunity;
import com.codebridgecommunity.crm.repository.OpportunityRepository;
import com.codebridgecommunity.crm.service.OpportunityService;
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
 * REST controller for managing {@link com.codebridgecommunity.crm.domain.Opportunity}.
 */
@RestController
@RequestMapping("/api")
public class OpportunityResource {

    private final Logger log = LoggerFactory.getLogger(OpportunityResource.class);

    private static final String ENTITY_NAME = "opportunity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OpportunityService opportunityService;

    private final OpportunityRepository opportunityRepository;

    public OpportunityResource(OpportunityService opportunityService, OpportunityRepository opportunityRepository) {
        this.opportunityService = opportunityService;
        this.opportunityRepository = opportunityRepository;
    }

    /**
     * {@code POST  /opportunities} : Create a new opportunity.
     *
     * @param opportunity the opportunity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new opportunity, or with status {@code 400 (Bad Request)} if the opportunity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/opportunities")
    public Mono<ResponseEntity<Opportunity>> createOpportunity(@Valid @RequestBody Opportunity opportunity) throws URISyntaxException {
        log.debug("REST request to save Opportunity : {}", opportunity);
        if (opportunity.getId() != null) {
            throw new BadRequestAlertException("A new opportunity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return opportunityService
            .save(opportunity)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/opportunities/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /opportunities/:id} : Updates an existing opportunity.
     *
     * @param id the id of the opportunity to save.
     * @param opportunity the opportunity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opportunity,
     * or with status {@code 400 (Bad Request)} if the opportunity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the opportunity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/opportunities/{id}")
    public Mono<ResponseEntity<Opportunity>> updateOpportunity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Opportunity opportunity
    ) throws URISyntaxException {
        log.debug("REST request to update Opportunity : {}, {}", id, opportunity);
        if (opportunity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, opportunity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return opportunityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return opportunityService
                    .update(opportunity)
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
     * {@code PATCH  /opportunities/:id} : Partial updates given fields of an existing opportunity, field will ignore if it is null
     *
     * @param id the id of the opportunity to save.
     * @param opportunity the opportunity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opportunity,
     * or with status {@code 400 (Bad Request)} if the opportunity is not valid,
     * or with status {@code 404 (Not Found)} if the opportunity is not found,
     * or with status {@code 500 (Internal Server Error)} if the opportunity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/opportunities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Opportunity>> partialUpdateOpportunity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Opportunity opportunity
    ) throws URISyntaxException {
        log.debug("REST request to partial update Opportunity partially : {}, {}", id, opportunity);
        if (opportunity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, opportunity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return opportunityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Opportunity> result = opportunityService.partialUpdate(opportunity);

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
     * {@code GET  /opportunities} : get all the opportunities.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of opportunities in body.
     */
    @GetMapping("/opportunities")
    public Mono<ResponseEntity<List<Opportunity>>> getAllOpportunities(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Opportunities");
        return opportunityService
            .countAll()
            .zipWith(opportunityService.findAll(pageable).collectList())
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
     * {@code GET  /opportunities/:id} : get the "id" opportunity.
     *
     * @param id the id of the opportunity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the opportunity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/opportunities/{id}")
    public Mono<ResponseEntity<Opportunity>> getOpportunity(@PathVariable Long id) {
        log.debug("REST request to get Opportunity : {}", id);
        Mono<Opportunity> opportunity = opportunityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(opportunity);
    }

    /**
     * {@code DELETE  /opportunities/:id} : delete the "id" opportunity.
     *
     * @param id the id of the opportunity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/opportunities/{id}")
    public Mono<ResponseEntity<Void>> deleteOpportunity(@PathVariable Long id) {
        log.debug("REST request to delete Opportunity : {}", id);
        return opportunityService
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

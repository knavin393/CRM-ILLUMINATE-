package com.codebridgecommunity.crm.web.rest;

import com.codebridgecommunity.crm.domain.Lead;
import com.codebridgecommunity.crm.repository.LeadRepository;
import com.codebridgecommunity.crm.service.LeadService;
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
 * REST controller for managing {@link com.codebridgecommunity.crm.domain.Lead}.
 */
@RestController
@RequestMapping("/api")
public class LeadResource {

    private final Logger log = LoggerFactory.getLogger(LeadResource.class);

    private static final String ENTITY_NAME = "lead";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeadService leadService;

    private final LeadRepository leadRepository;

    public LeadResource(LeadService leadService, LeadRepository leadRepository) {
        this.leadService = leadService;
        this.leadRepository = leadRepository;
    }

    /**
     * {@code POST  /leads} : Create a new lead.
     *
     * @param lead the lead to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lead, or with status {@code 400 (Bad Request)} if the lead has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leads")
    public Mono<ResponseEntity<Lead>> createLead(@Valid @RequestBody Lead lead) throws URISyntaxException {
        log.debug("REST request to save Lead : {}", lead);
        if (lead.getId() != null) {
            throw new BadRequestAlertException("A new lead cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return leadService
            .save(lead)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/leads/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /leads/:id} : Updates an existing lead.
     *
     * @param id the id of the lead to save.
     * @param lead the lead to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lead,
     * or with status {@code 400 (Bad Request)} if the lead is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lead couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leads/{id}")
    public Mono<ResponseEntity<Lead>> updateLead(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Lead lead
    ) throws URISyntaxException {
        log.debug("REST request to update Lead : {}, {}", id, lead);
        if (lead.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lead.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return leadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return leadService
                    .update(lead)
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
     * {@code PATCH  /leads/:id} : Partial updates given fields of an existing lead, field will ignore if it is null
     *
     * @param id the id of the lead to save.
     * @param lead the lead to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lead,
     * or with status {@code 400 (Bad Request)} if the lead is not valid,
     * or with status {@code 404 (Not Found)} if the lead is not found,
     * or with status {@code 500 (Internal Server Error)} if the lead couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/leads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Lead>> partialUpdateLead(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Lead lead
    ) throws URISyntaxException {
        log.debug("REST request to partial update Lead partially : {}, {}", id, lead);
        if (lead.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lead.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return leadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Lead> result = leadService.partialUpdate(lead);

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
     * {@code GET  /leads} : get all the leads.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leads in body.
     */
    @GetMapping("/leads")
    public Mono<ResponseEntity<List<Lead>>> getAllLeads(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Leads");
        return leadService
            .countAll()
            .zipWith(leadService.findAll(pageable).collectList())
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
     * {@code GET  /leads/:id} : get the "id" lead.
     *
     * @param id the id of the lead to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lead, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leads/{id}")
    public Mono<ResponseEntity<Lead>> getLead(@PathVariable Long id) {
        log.debug("REST request to get Lead : {}", id);
        Mono<Lead> lead = leadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lead);
    }

    /**
     * {@code DELETE  /leads/:id} : delete the "id" lead.
     *
     * @param id the id of the lead to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leads/{id}")
    public Mono<ResponseEntity<Void>> deleteLead(@PathVariable Long id) {
        log.debug("REST request to delete Lead : {}", id);
        return leadService
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

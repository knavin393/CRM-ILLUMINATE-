package com.codebridgecommunity.crm.web.rest;

import com.codebridgecommunity.crm.domain.WinLoss;
import com.codebridgecommunity.crm.repository.WinLossRepository;
import com.codebridgecommunity.crm.service.WinLossService;
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
 * REST controller for managing {@link com.codebridgecommunity.crm.domain.WinLoss}.
 */
@RestController
@RequestMapping("/api")
public class WinLossResource {

    private final Logger log = LoggerFactory.getLogger(WinLossResource.class);

    private static final String ENTITY_NAME = "winLoss";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WinLossService winLossService;

    private final WinLossRepository winLossRepository;

    public WinLossResource(WinLossService winLossService, WinLossRepository winLossRepository) {
        this.winLossService = winLossService;
        this.winLossRepository = winLossRepository;
    }

    /**
     * {@code POST  /win-losses} : Create a new winLoss.
     *
     * @param winLoss the winLoss to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new winLoss, or with status {@code 400 (Bad Request)} if the winLoss has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/win-losses")
    public Mono<ResponseEntity<WinLoss>> createWinLoss(@Valid @RequestBody WinLoss winLoss) throws URISyntaxException {
        log.debug("REST request to save WinLoss : {}", winLoss);
        if (winLoss.getId() != null) {
            throw new BadRequestAlertException("A new winLoss cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return winLossService
            .save(winLoss)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/win-losses/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /win-losses/:id} : Updates an existing winLoss.
     *
     * @param id the id of the winLoss to save.
     * @param winLoss the winLoss to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated winLoss,
     * or with status {@code 400 (Bad Request)} if the winLoss is not valid,
     * or with status {@code 500 (Internal Server Error)} if the winLoss couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/win-losses/{id}")
    public Mono<ResponseEntity<WinLoss>> updateWinLoss(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WinLoss winLoss
    ) throws URISyntaxException {
        log.debug("REST request to update WinLoss : {}, {}", id, winLoss);
        if (winLoss.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, winLoss.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return winLossRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return winLossService
                    .update(winLoss)
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
     * {@code PATCH  /win-losses/:id} : Partial updates given fields of an existing winLoss, field will ignore if it is null
     *
     * @param id the id of the winLoss to save.
     * @param winLoss the winLoss to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated winLoss,
     * or with status {@code 400 (Bad Request)} if the winLoss is not valid,
     * or with status {@code 404 (Not Found)} if the winLoss is not found,
     * or with status {@code 500 (Internal Server Error)} if the winLoss couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/win-losses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<WinLoss>> partialUpdateWinLoss(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WinLoss winLoss
    ) throws URISyntaxException {
        log.debug("REST request to partial update WinLoss partially : {}, {}", id, winLoss);
        if (winLoss.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, winLoss.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return winLossRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WinLoss> result = winLossService.partialUpdate(winLoss);

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
     * {@code GET  /win-losses} : get all the winLosses.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of winLosses in body.
     */
    @GetMapping("/win-losses")
    public Mono<ResponseEntity<List<WinLoss>>> getAllWinLosses(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of WinLosses");
        return winLossService
            .countAll()
            .zipWith(winLossService.findAll(pageable).collectList())
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
     * {@code GET  /win-losses/:id} : get the "id" winLoss.
     *
     * @param id the id of the winLoss to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the winLoss, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/win-losses/{id}")
    public Mono<ResponseEntity<WinLoss>> getWinLoss(@PathVariable Long id) {
        log.debug("REST request to get WinLoss : {}", id);
        Mono<WinLoss> winLoss = winLossService.findOne(id);
        return ResponseUtil.wrapOrNotFound(winLoss);
    }

    /**
     * {@code DELETE  /win-losses/:id} : delete the "id" winLoss.
     *
     * @param id the id of the winLoss to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/win-losses/{id}")
    public Mono<ResponseEntity<Void>> deleteWinLoss(@PathVariable Long id) {
        log.debug("REST request to delete WinLoss : {}", id);
        return winLossService
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

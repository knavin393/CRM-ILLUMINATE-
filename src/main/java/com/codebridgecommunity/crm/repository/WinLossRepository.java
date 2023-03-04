package com.codebridgecommunity.crm.repository;

import com.codebridgecommunity.crm.domain.WinLoss;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the WinLoss entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WinLossRepository extends ReactiveCrudRepository<WinLoss, Long>, WinLossRepositoryInternal {
    Flux<WinLoss> findAllBy(Pageable pageable);

    @Override
    Mono<WinLoss> findOneWithEagerRelationships(Long id);

    @Override
    Flux<WinLoss> findAllWithEagerRelationships();

    @Override
    Flux<WinLoss> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM win_loss entity WHERE entity.opportunity_id = :id")
    Flux<WinLoss> findByOpportunity(Long id);

    @Query("SELECT * FROM win_loss entity WHERE entity.opportunity_id IS NULL")
    Flux<WinLoss> findAllWhereOpportunityIsNull();

    @Query("SELECT * FROM win_loss entity WHERE entity.customer_id = :id")
    Flux<WinLoss> findByCustomer(Long id);

    @Query("SELECT * FROM win_loss entity WHERE entity.customer_id IS NULL")
    Flux<WinLoss> findAllWhereCustomerIsNull();

    @Query("SELECT * FROM win_loss entity WHERE entity.product_id = :id")
    Flux<WinLoss> findByProduct(Long id);

    @Query("SELECT * FROM win_loss entity WHERE entity.product_id IS NULL")
    Flux<WinLoss> findAllWhereProductIsNull();

    @Override
    <S extends WinLoss> Mono<S> save(S entity);

    @Override
    Flux<WinLoss> findAll();

    @Override
    Mono<WinLoss> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface WinLossRepositoryInternal {
    <S extends WinLoss> Mono<S> save(S entity);

    Flux<WinLoss> findAllBy(Pageable pageable);

    Flux<WinLoss> findAll();

    Mono<WinLoss> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<WinLoss> findAllBy(Pageable pageable, Criteria criteria);

    Mono<WinLoss> findOneWithEagerRelationships(Long id);

    Flux<WinLoss> findAllWithEagerRelationships();

    Flux<WinLoss> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

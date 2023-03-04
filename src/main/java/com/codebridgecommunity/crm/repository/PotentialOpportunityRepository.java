package com.codebridgecommunity.crm.repository;

import com.codebridgecommunity.crm.domain.PotentialOpportunity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PotentialOpportunity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PotentialOpportunityRepository
    extends ReactiveCrudRepository<PotentialOpportunity, Long>, PotentialOpportunityRepositoryInternal {
    Flux<PotentialOpportunity> findAllBy(Pageable pageable);

    @Override
    Mono<PotentialOpportunity> findOneWithEagerRelationships(Long id);

    @Override
    Flux<PotentialOpportunity> findAllWithEagerRelationships();

    @Override
    Flux<PotentialOpportunity> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM potential_opportunity entity WHERE entity.customer_id = :id")
    Flux<PotentialOpportunity> findByCustomer(Long id);

    @Query("SELECT * FROM potential_opportunity entity WHERE entity.customer_id IS NULL")
    Flux<PotentialOpportunity> findAllWhereCustomerIsNull();

    @Query("SELECT * FROM potential_opportunity entity WHERE entity.employee_id = :id")
    Flux<PotentialOpportunity> findByEmployee(Long id);

    @Query("SELECT * FROM potential_opportunity entity WHERE entity.employee_id IS NULL")
    Flux<PotentialOpportunity> findAllWhereEmployeeIsNull();

    @Query("SELECT * FROM potential_opportunity entity WHERE entity.product_id = :id")
    Flux<PotentialOpportunity> findByProduct(Long id);

    @Query("SELECT * FROM potential_opportunity entity WHERE entity.product_id IS NULL")
    Flux<PotentialOpportunity> findAllWhereProductIsNull();

    @Override
    <S extends PotentialOpportunity> Mono<S> save(S entity);

    @Override
    Flux<PotentialOpportunity> findAll();

    @Override
    Mono<PotentialOpportunity> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PotentialOpportunityRepositoryInternal {
    <S extends PotentialOpportunity> Mono<S> save(S entity);

    Flux<PotentialOpportunity> findAllBy(Pageable pageable);

    Flux<PotentialOpportunity> findAll();

    Mono<PotentialOpportunity> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PotentialOpportunity> findAllBy(Pageable pageable, Criteria criteria);

    Mono<PotentialOpportunity> findOneWithEagerRelationships(Long id);

    Flux<PotentialOpportunity> findAllWithEagerRelationships();

    Flux<PotentialOpportunity> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

package com.codebridgecommunity.crm.repository;

import com.codebridgecommunity.crm.domain.Opportunity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Opportunity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OpportunityRepository extends ReactiveCrudRepository<Opportunity, Long>, OpportunityRepositoryInternal {
    Flux<Opportunity> findAllBy(Pageable pageable);

    @Override
    Mono<Opportunity> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Opportunity> findAllWithEagerRelationships();

    @Override
    Flux<Opportunity> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM opportunity entity WHERE entity.customer_id = :id")
    Flux<Opportunity> findByCustomer(Long id);

    @Query("SELECT * FROM opportunity entity WHERE entity.customer_id IS NULL")
    Flux<Opportunity> findAllWhereCustomerIsNull();

    @Query("SELECT * FROM opportunity entity WHERE entity.employee_id = :id")
    Flux<Opportunity> findByEmployee(Long id);

    @Query("SELECT * FROM opportunity entity WHERE entity.employee_id IS NULL")
    Flux<Opportunity> findAllWhereEmployeeIsNull();

    @Query("SELECT * FROM opportunity entity WHERE entity.product_id = :id")
    Flux<Opportunity> findByProduct(Long id);

    @Query("SELECT * FROM opportunity entity WHERE entity.product_id IS NULL")
    Flux<Opportunity> findAllWhereProductIsNull();

    @Override
    <S extends Opportunity> Mono<S> save(S entity);

    @Override
    Flux<Opportunity> findAll();

    @Override
    Mono<Opportunity> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface OpportunityRepositoryInternal {
    <S extends Opportunity> Mono<S> save(S entity);

    Flux<Opportunity> findAllBy(Pageable pageable);

    Flux<Opportunity> findAll();

    Mono<Opportunity> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Opportunity> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Opportunity> findOneWithEagerRelationships(Long id);

    Flux<Opportunity> findAllWithEagerRelationships();

    Flux<Opportunity> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

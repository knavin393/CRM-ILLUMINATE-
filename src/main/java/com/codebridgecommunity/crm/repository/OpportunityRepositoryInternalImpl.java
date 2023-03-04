package com.codebridgecommunity.crm.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.codebridgecommunity.crm.domain.Opportunity;
import com.codebridgecommunity.crm.domain.enumeration.OppStatus;
import com.codebridgecommunity.crm.repository.rowmapper.CustomerRowMapper;
import com.codebridgecommunity.crm.repository.rowmapper.EmployeeRowMapper;
import com.codebridgecommunity.crm.repository.rowmapper.OpportunityRowMapper;
import com.codebridgecommunity.crm.repository.rowmapper.ProductRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Opportunity entity.
 */
@SuppressWarnings("unused")
class OpportunityRepositoryInternalImpl extends SimpleR2dbcRepository<Opportunity, Long> implements OpportunityRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CustomerRowMapper customerMapper;
    private final EmployeeRowMapper employeeMapper;
    private final ProductRowMapper productMapper;
    private final OpportunityRowMapper opportunityMapper;

    private static final Table entityTable = Table.aliased("opportunity", EntityManager.ENTITY_ALIAS);
    private static final Table customerTable = Table.aliased("customer", "customer");
    private static final Table employeeTable = Table.aliased("employee", "employee");
    private static final Table productTable = Table.aliased("product", "product");

    public OpportunityRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CustomerRowMapper customerMapper,
        EmployeeRowMapper employeeMapper,
        ProductRowMapper productMapper,
        OpportunityRowMapper opportunityMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Opportunity.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.customerMapper = customerMapper;
        this.employeeMapper = employeeMapper;
        this.productMapper = productMapper;
        this.opportunityMapper = opportunityMapper;
    }

    @Override
    public Flux<Opportunity> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Opportunity> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = OpportunitySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CustomerSqlHelper.getColumns(customerTable, "customer"));
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        columns.addAll(ProductSqlHelper.getColumns(productTable, "product"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(customerTable)
            .on(Column.create("customer_id", entityTable))
            .equals(Column.create("id", customerTable))
            .leftOuterJoin(employeeTable)
            .on(Column.create("employee_id", entityTable))
            .equals(Column.create("id", employeeTable))
            .leftOuterJoin(productTable)
            .on(Column.create("product_id", entityTable))
            .equals(Column.create("id", productTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Opportunity.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Opportunity> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Opportunity> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Opportunity> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Opportunity> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Opportunity> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Opportunity process(Row row, RowMetadata metadata) {
        Opportunity entity = opportunityMapper.apply(row, "e");
        entity.setCustomer(customerMapper.apply(row, "customer"));
        entity.setEmployee(employeeMapper.apply(row, "employee"));
        entity.setProduct(productMapper.apply(row, "product"));
        return entity;
    }

    @Override
    public <S extends Opportunity> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

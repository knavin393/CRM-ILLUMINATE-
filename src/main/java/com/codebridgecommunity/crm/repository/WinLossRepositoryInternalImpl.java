package com.codebridgecommunity.crm.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.codebridgecommunity.crm.domain.WinLoss;
import com.codebridgecommunity.crm.repository.rowmapper.CustomerRowMapper;
import com.codebridgecommunity.crm.repository.rowmapper.OpportunityRowMapper;
import com.codebridgecommunity.crm.repository.rowmapper.ProductRowMapper;
import com.codebridgecommunity.crm.repository.rowmapper.WinLossRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the WinLoss entity.
 */
@SuppressWarnings("unused")
class WinLossRepositoryInternalImpl extends SimpleR2dbcRepository<WinLoss, Long> implements WinLossRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final OpportunityRowMapper opportunityMapper;
    private final CustomerRowMapper customerMapper;
    private final ProductRowMapper productMapper;
    private final WinLossRowMapper winlossMapper;

    private static final Table entityTable = Table.aliased("win_loss", EntityManager.ENTITY_ALIAS);
    private static final Table opportunityTable = Table.aliased("opportunity", "opportunity");
    private static final Table customerTable = Table.aliased("customer", "customer");
    private static final Table productTable = Table.aliased("product", "product");

    public WinLossRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        OpportunityRowMapper opportunityMapper,
        CustomerRowMapper customerMapper,
        ProductRowMapper productMapper,
        WinLossRowMapper winlossMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(WinLoss.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.opportunityMapper = opportunityMapper;
        this.customerMapper = customerMapper;
        this.productMapper = productMapper;
        this.winlossMapper = winlossMapper;
    }

    @Override
    public Flux<WinLoss> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<WinLoss> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = WinLossSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(OpportunitySqlHelper.getColumns(opportunityTable, "opportunity"));
        columns.addAll(CustomerSqlHelper.getColumns(customerTable, "customer"));
        columns.addAll(ProductSqlHelper.getColumns(productTable, "product"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(opportunityTable)
            .on(Column.create("opportunity_id", entityTable))
            .equals(Column.create("id", opportunityTable))
            .leftOuterJoin(customerTable)
            .on(Column.create("customer_id", entityTable))
            .equals(Column.create("id", customerTable))
            .leftOuterJoin(productTable)
            .on(Column.create("product_id", entityTable))
            .equals(Column.create("id", productTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, WinLoss.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<WinLoss> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<WinLoss> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<WinLoss> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<WinLoss> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<WinLoss> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private WinLoss process(Row row, RowMetadata metadata) {
        WinLoss entity = winlossMapper.apply(row, "e");
        entity.setOpportunity(opportunityMapper.apply(row, "opportunity"));
        entity.setCustomer(customerMapper.apply(row, "customer"));
        entity.setProduct(productMapper.apply(row, "product"));
        return entity;
    }

    @Override
    public <S extends WinLoss> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

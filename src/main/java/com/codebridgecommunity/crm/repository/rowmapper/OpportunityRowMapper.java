package com.codebridgecommunity.crm.repository.rowmapper;

import com.codebridgecommunity.crm.domain.Opportunity;
import com.codebridgecommunity.crm.domain.enumeration.OppStatus;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Opportunity}, with proper type conversions.
 */
@Service
public class OpportunityRowMapper implements BiFunction<Row, String, Opportunity> {

    private final ColumnConverter converter;

    public OpportunityRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Opportunity} stored in the database.
     */
    @Override
    public Opportunity apply(Row row, String prefix) {
        Opportunity entity = new Opportunity();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFollowUp(converter.fromRow(row, prefix + "_follow_up", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", OppStatus.class));
        entity.setCustomerId(converter.fromRow(row, prefix + "_customer_id", Long.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", Long.class));
        entity.setProductId(converter.fromRow(row, prefix + "_product_id", Long.class));
        return entity;
    }
}

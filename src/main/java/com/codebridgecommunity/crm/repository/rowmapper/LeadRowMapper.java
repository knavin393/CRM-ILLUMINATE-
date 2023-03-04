package com.codebridgecommunity.crm.repository.rowmapper;

import com.codebridgecommunity.crm.domain.Lead;
import com.codebridgecommunity.crm.domain.enumeration.LeadStatus;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Lead}, with proper type conversions.
 */
@Service
public class LeadRowMapper implements BiFunction<Row, String, Lead> {

    private final ColumnConverter converter;

    public LeadRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Lead} stored in the database.
     */
    @Override
    public Lead apply(Row row, String prefix) {
        Lead entity = new Lead();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFollowUp(converter.fromRow(row, prefix + "_follow_up", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", LeadStatus.class));
        entity.setCustomerId(converter.fromRow(row, prefix + "_customer_id", Long.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", Long.class));
        entity.setProductId(converter.fromRow(row, prefix + "_product_id", Long.class));
        return entity;
    }
}

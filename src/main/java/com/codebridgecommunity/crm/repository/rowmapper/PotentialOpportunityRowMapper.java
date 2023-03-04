package com.codebridgecommunity.crm.repository.rowmapper;

import com.codebridgecommunity.crm.domain.PotentialOpportunity;
import com.codebridgecommunity.crm.domain.enumeration.POStatus;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PotentialOpportunity}, with proper type conversions.
 */
@Service
public class PotentialOpportunityRowMapper implements BiFunction<Row, String, PotentialOpportunity> {

    private final ColumnConverter converter;

    public PotentialOpportunityRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PotentialOpportunity} stored in the database.
     */
    @Override
    public PotentialOpportunity apply(Row row, String prefix) {
        PotentialOpportunity entity = new PotentialOpportunity();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFollowUp(converter.fromRow(row, prefix + "_follow_up", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", POStatus.class));
        entity.setCustomerId(converter.fromRow(row, prefix + "_customer_id", Long.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", Long.class));
        entity.setProductId(converter.fromRow(row, prefix + "_product_id", Long.class));
        return entity;
    }
}

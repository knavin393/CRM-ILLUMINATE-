package com.codebridgecommunity.crm.repository.rowmapper;

import com.codebridgecommunity.crm.domain.WinLoss;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link WinLoss}, with proper type conversions.
 */
@Service
public class WinLossRowMapper implements BiFunction<Row, String, WinLoss> {

    private final ColumnConverter converter;

    public WinLossRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link WinLoss} stored in the database.
     */
    @Override
    public WinLoss apply(Row row, String prefix) {
        WinLoss entity = new WinLoss();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNotes(converter.fromRow(row, prefix + "_notes", String.class));
        entity.setOpportunityId(converter.fromRow(row, prefix + "_opportunity_id", Long.class));
        entity.setCustomerId(converter.fromRow(row, prefix + "_customer_id", Long.class));
        entity.setProductId(converter.fromRow(row, prefix + "_product_id", Long.class));
        return entity;
    }
}

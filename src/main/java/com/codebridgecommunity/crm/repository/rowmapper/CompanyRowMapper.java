package com.codebridgecommunity.crm.repository.rowmapper;

import com.codebridgecommunity.crm.domain.Company;
import com.codebridgecommunity.crm.domain.enumeration.SocMed;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Company}, with proper type conversions.
 */
@Service
public class CompanyRowMapper implements BiFunction<Row, String, Company> {

    private final ColumnConverter converter;

    public CompanyRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Company} stored in the database.
     */
    @Override
    public Company apply(Row row, String prefix) {
        Company entity = new Company();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIndustry(converter.fromRow(row, prefix + "_industry", String.class));
        entity.setLocation(converter.fromRow(row, prefix + "_location", String.class));
        entity.setEstablished(converter.fromRow(row, prefix + "_established", String.class));
        entity.setEngage(converter.fromRow(row, prefix + "_engage", SocMed.class));
        return entity;
    }
}

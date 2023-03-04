package com.codebridgecommunity.crm.repository.rowmapper;

import com.codebridgecommunity.crm.domain.Customer;
import com.codebridgecommunity.crm.domain.enumeration.Gender;
import com.codebridgecommunity.crm.domain.enumeration.SocMed;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Customer}, with proper type conversions.
 */
@Service
public class CustomerRowMapper implements BiFunction<Row, String, Customer> {

    private final ColumnConverter converter;

    public CustomerRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Customer} stored in the database.
     */
    @Override
    public Customer apply(Row row, String prefix) {
        Customer entity = new Customer();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCustName(converter.fromRow(row, prefix + "_cust_name", String.class));
        entity.setGender(converter.fromRow(row, prefix + "_gender", Gender.class));
        entity.setCustIC(converter.fromRow(row, prefix + "_cust_ic", String.class));
        entity.setCustEmail(converter.fromRow(row, prefix + "_cust_email", String.class));
        entity.setCustPhone(converter.fromRow(row, prefix + "_cust_phone", String.class));
        entity.setCompanyName(converter.fromRow(row, prefix + "_company_name", String.class));
        entity.setCustJobTitle(converter.fromRow(row, prefix + "_cust_job_title", String.class));
        entity.setEngage(converter.fromRow(row, prefix + "_engage", SocMed.class));
        entity.setCompanyId(converter.fromRow(row, prefix + "_company_id", Long.class));
        return entity;
    }
}

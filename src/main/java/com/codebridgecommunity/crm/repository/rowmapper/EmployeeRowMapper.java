package com.codebridgecommunity.crm.repository.rowmapper;

import com.codebridgecommunity.crm.domain.Employee;
import com.codebridgecommunity.crm.domain.enumeration.Gender;
import com.codebridgecommunity.crm.domain.enumeration.SocMed;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Employee}, with proper type conversions.
 */
@Service
public class EmployeeRowMapper implements BiFunction<Row, String, Employee> {

    private final ColumnConverter converter;

    public EmployeeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Employee} stored in the database.
     */
    @Override
    public Employee apply(Row row, String prefix) {
        Employee entity = new Employee();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setEmpName(converter.fromRow(row, prefix + "_emp_name", String.class));
        entity.setEmpAge(converter.fromRow(row, prefix + "_emp_age", Integer.class));
        entity.setGender(converter.fromRow(row, prefix + "_gender", Gender.class));
        entity.setEmpJobTitle(converter.fromRow(row, prefix + "_emp_job_title", String.class));
        entity.setEmpPhone(converter.fromRow(row, prefix + "_emp_phone", String.class));
        entity.setEmpEmail(converter.fromRow(row, prefix + "_emp_email", String.class));
        entity.setEngage(converter.fromRow(row, prefix + "_engage", SocMed.class));
        return entity;
    }
}

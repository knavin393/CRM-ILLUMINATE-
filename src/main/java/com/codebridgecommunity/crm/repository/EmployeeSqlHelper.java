package com.codebridgecommunity.crm.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class EmployeeSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("emp_name", table, columnPrefix + "_emp_name"));
        columns.add(Column.aliased("emp_age", table, columnPrefix + "_emp_age"));
        columns.add(Column.aliased("gender", table, columnPrefix + "_gender"));
        columns.add(Column.aliased("emp_job_title", table, columnPrefix + "_emp_job_title"));
        columns.add(Column.aliased("emp_phone", table, columnPrefix + "_emp_phone"));
        columns.add(Column.aliased("emp_email", table, columnPrefix + "_emp_email"));
        columns.add(Column.aliased("engage", table, columnPrefix + "_engage"));

        return columns;
    }
}

package com.codebridgecommunity.crm.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CustomerSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("cust_name", table, columnPrefix + "_cust_name"));
        columns.add(Column.aliased("gender", table, columnPrefix + "_gender"));
        columns.add(Column.aliased("cust_ic", table, columnPrefix + "_cust_ic"));
        columns.add(Column.aliased("cust_email", table, columnPrefix + "_cust_email"));
        columns.add(Column.aliased("cust_phone", table, columnPrefix + "_cust_phone"));
        columns.add(Column.aliased("company_name", table, columnPrefix + "_company_name"));
        columns.add(Column.aliased("cust_job_title", table, columnPrefix + "_cust_job_title"));
        columns.add(Column.aliased("engage", table, columnPrefix + "_engage"));

        columns.add(Column.aliased("company_id", table, columnPrefix + "_company_id"));
        return columns;
    }
}

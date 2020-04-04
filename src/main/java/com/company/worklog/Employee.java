package com.company.worklog;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class Employee extends Model {
    Employee(Connection conn) throws SQLException {
        super(conn);

        this.tableName = "Employee";

        this.fields = new HashMap<>();
        this.fields.put("Name", "CharField");

        this.required = new String[1];
        this.required[0] = "Name";

        this.createTable();
    }
}

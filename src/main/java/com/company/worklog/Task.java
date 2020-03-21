package com.company.worklog;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class Task extends Model {
    Task(Connection conn) throws SQLException {
        super(conn);

        this.tableName = "Task";

        this.fields = new HashMap<String, String>();
        this.fields.put("task_date", "DateTime");
        this.fields.put("title", "CharField");
        this.fields.put("time_spent", "Integer");
        this.fields.put("notes", "TextField");

        this.required = new String[1];
        this.required[0] = "Name";

        this.foreignKeys = new String[1];
        this.foreignKeys[0] = "Employee";

        createTable();
    }
}

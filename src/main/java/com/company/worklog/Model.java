package com.company.worklog;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class Model {
    private Connection conn;
    private Map<String, String> fieldMapper;

    protected String tableName;
    protected Map<String, String> fields;
    protected String[] foreignKeys;
    protected String[] required;

    /**
     * Represents a model to the sqlite database.
     *
     * @param conn - The database connection.
     */
    Model(Connection conn) {
        this.conn = conn;

        this.fieldMapper = new HashMap<String, String>();
        this.fieldMapper.put("charfield", "TEXT");
        this.fieldMapper.put("datetime", "INTEGER");
        this.fieldMapper.put("integer", "INTEGER");
        this.fieldMapper.put("textfield", "TEXT");
    }

    /**
     * Creates initial table schema for model. Could probably use some refactoring at a later point.
     *
     * @throws SQLException - Any unexpected sql errors encountered in application.
     */
    protected void createTable() throws SQLException {
        try {
            Statement stmt = this.conn.createStatement();

            if (this.fields.isEmpty()) {
                throw new Error("Fields map cannot be empty.");
            }

            StringBuilder fieldSql = new StringBuilder("(id INTEGER PRIMARY KEY, ");
            int i = 0;
            for (Map.Entry<String, String> entry : this.fields.entrySet()) {
                String key = entry.getKey();
                String value = this.fieldMapper.get(entry.getValue().toLowerCase());
                String required = "";

                if (Arrays.asList(this.required).contains(key)) {
                    required = " NOT NULL";
                }

                String fullField = key + " " + value.toUpperCase() + required;
                fieldSql.append(fullField);

                if ((i + 1) < this.fields.keySet().size()) {
                    fieldSql.append(", ");
                }
                i++;
            }

            StringBuilder foreignKeySql = new StringBuilder();
            if (this.foreignKeys != null && this.foreignKeys.length > 0) {
                for (String foreignKey : this.foreignKeys) {
                    String foreignKeyId = foreignKey.toLowerCase() + "_id";

                    String foreignKeyCreation = ", " + foreignKeyId +  " " + this.fieldMapper.get("integer");

                    String fullForeignKey = foreignKeyCreation +
                            ", FOREIGN KEY(" +
                            foreignKeyId +
                            ") REFERENCES " +
                            foreignKey +
                            "(id)";
                    foreignKeySql.append(fullForeignKey);
                }
            }

            String sql = "CREATE TABLE IF NOT EXISTS " +
                    this.tableName +
                    fieldSql +
                    foreignKeySql + ");";
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private boolean isValidFields(HashMap<String, String> values) {
        if (values.isEmpty()) {
            return false;
        }

        ArrayList<String> modelKeys = new ArrayList<String>();

        for (Map.Entry<String, String> entry : this.fields.entrySet()) {
            modelKeys.add(entry.getKey());
        }

        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey();

            if (!modelKeys.contains(key)) {
                return false;
            }
        }

        return true;
    }

    public void create(HashMap<String, String> entry) throws Error {
        if (!this.isValidFields(entry)) {
            throw new Error("SQL fields provided are invalid.");
        }

        try {
            Statement stmt = this.conn.createStatement();

            StringBuilder fields = new StringBuilder("(");
            StringBuilder values = new StringBuilder("(");

            for (Map.Entry<String, String> record : entry.entrySet()) {
                fields.append(record.getKey());
                values.append(record.getValue());
            }

            fields.append(")");
            values.append(")");

            String sql = "INSERT INTO " +
                    this.tableName +
                    " " +
                    fields +
                    "VALUES " +
                    values;
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void insert(HashMap<String, String> entry) throws Error {
        // TODO: Implement
    }

    public void find(String searchField, String searchValue) throws Error {
        // TODO: Check for invalid field

        try {
            Statement stmt = this.conn.createStatement();

            String sql = "SELCT * FROM " + this.tableName + " WHERE " + searchField + " = " + searchValue + ";";

            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void find(String searchField, int searchValue) throws Error {
        // TODO: Check for invalid field

        try {
            Statement stmt = this.conn.createStatement();

            String sql = "SELECT * FROM " + this.tableName + " WHERE " + searchField + " = " + searchValue + ";";

            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}

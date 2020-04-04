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

        this.fieldMapper = new HashMap<>();
        this.fieldMapper.put("charfield", "TEXT");
        this.fieldMapper.put("datetime", "INTEGER");
        this.fieldMapper.put("integer", "INTEGER");
        this.fieldMapper.put("textfield", "TEXT");
    }

    /**
     * Creates initial table schema for model.
     */
    protected void createTable() {
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

    /**
     * Creates a database entry.
     *
     * @param entry - The values and fields to use when creating the entry.
     * @throws Exception - Throws exception if fields are not valid.
     */
    public void create(HashMap<String, String> entry) throws Exception {
        if (!this.isValidFields(entry)) {
            throw new Exception("SQL fields provided are invalid.");
        }

        try {
            Statement stmt = this.conn.createStatement();

            StringBuilder fields = new StringBuilder("(");
            StringBuilder values = new StringBuilder("(");

            for (Map.Entry<String, String> record : entry.entrySet()) {
                fields.append(record.getKey());
                fields.append(", ");
                values.append(record.getValue());
                values.append(", ");
            }

            fields.deleteCharAt(fields.length() - 1);
            fields.deleteCharAt(fields.length() - 1);
            fields.append(")");

            values.deleteCharAt(values.length() - 1);
            values.deleteCharAt(values.length() - 1);
            values.append(")");

            String sql = "INSERT INTO " +
                    this.tableName +
                    " " +
                    fields +
                    " VALUES " +
                    values +
                    ";";
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    /**
     * Updates a row in the database with values provided.
     *
     * @param entry - The database row to update.
     * @throws Exception - Throws exception if fields are not valid.
     */
    public void update(HashMap<String, String> entry) throws Exception {
        if (!this.isValidFields(entry)) {
            throw new Exception("SQL fields provided are invalid.");
        }

        try {
            String setUpUpdate = "UPDATE " + this.tableName + " SET ";

            StringBuilder sql = new StringBuilder(setUpUpdate);

            for (Map.Entry<String, String> record : entry.entrySet()) {
                String updateCol = record.getKey() + " = " + record.getValue() + ", ";
                sql.append(updateCol);
            }

            sql.deleteCharAt(sql.length() - 1);
            sql.deleteCharAt(sql.length() - 1);
            sql.append(";");

            Statement stmt = this.conn.createStatement();
            stmt.executeUpdate(sql.toString());
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    /**
     * Find a database entry based on a field value.
     *
     * @param searchField - Model field to search on.
     * @param searchValue - Field value to search with.
     * @throws Exception - Throws exception if fields are not valid.
     */
    public void find(String searchField, String searchValue) throws Exception {
        HashMap<String, String> findMap = new HashMap<>();
        findMap.put(searchField, searchValue);

        if (!this.isValidFields(findMap)) {
            throw new Exception("SQL fields provided are invalid.");
        }

        try {
            Statement stmt = this.conn.createStatement();

            String sql = "SELECT * FROM " + this.tableName + " WHERE " + searchField + " = " + searchValue + ";";

            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void find(String searchField, int searchValue) throws Exception {
        String strSearchValue = Integer.toString(searchValue);

        this.find(searchField, strSearchValue);
    }

    /**
     * Validates that the fields are registered with the model.
     *
     * @param values - The model fields to check against.
     * @return - If valid.
     */
    private boolean isValidFields(HashMap<String, String> values) {
        if (values.isEmpty()) {
            return false;
        }

        ArrayList<String> modelKeys = new ArrayList<>();

        for (Map.Entry<String, String> entry : this.fields.entrySet()) {
            modelKeys.add(entry.getKey().toLowerCase());
        }

        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey().toLowerCase();

            if (!modelKeys.contains(key)) {
                return false;
            }
        }

        return true;
    }
}

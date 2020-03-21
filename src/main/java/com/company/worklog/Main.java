package com.company.worklog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:worklog.db";

        try {
            Connection conn = DriverManager.getConnection(url);
            Employee employee = new Employee(conn);
            Task task = new Task(conn);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

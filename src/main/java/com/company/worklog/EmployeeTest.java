package com.company.worklog;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class EmployeeTest {
    Employee employee;

    @Mock
    private Connection mockConnection;
    @Mock
    private Statement mockStatement;

    @Captor
    private ArgumentCaptor<String> statementArgumentCaptor;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        try {
            Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
            this.employee = new Employee(mockConnection);
        } catch (SQLException ignored) {
        }
    }

    @Test
    void createTable() {
        try {
            verify(mockStatement).executeUpdate(statementArgumentCaptor.capture());

            String expectedSql = "CREATE TABLE IF NOT EXISTS Employee(id INTEGER PRIMARY KEY, Name TEXT NOT NULL);";
            Assertions.assertEquals(statementArgumentCaptor.getValue(), expectedSql);
        } catch (SQLException ignored) {
        }
    }

    @Test
    void create() {
        HashMap<String, String> employeeMap = new HashMap<>();
        employeeMap.put("Name", "Foo");

        try {
            this.employee.create(employeeMap);
            verify(mockStatement, times(2)).executeUpdate(statementArgumentCaptor.capture());
        } catch (Exception ignored) {
        }

        String expectedSql = "INSERT INTO Employee (Name) VALUES (Foo);";
        Assertions.assertEquals(statementArgumentCaptor.getValue(), expectedSql);
    }

    @Test
    void create_invalid_fields() {
        HashMap<String, String> employeeMap = new HashMap<>();
        employeeMap.put("Foo", "Bar");

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            this.employee.create(employeeMap);
        });

        String expectedMessage = "SQL fields provided are invalid.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        HashMap<String, String> employeeMap = new HashMap<>();
        employeeMap.put("Name", "Bar");

        try {
            this.employee.update(employeeMap);
            verify(mockStatement, times(2)).executeUpdate(statementArgumentCaptor.capture());
        } catch (Exception ignored) {
        }

        String expectedSql = "UPDATE Employee SET Name = Bar;";
        Assertions.assertEquals(statementArgumentCaptor.getValue(), expectedSql);
    }

    @Test
    void update_invalid_fields() {
        HashMap<String, String> employeeMap = new HashMap<>();
        employeeMap.put("Foo", "Bar");

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            this.employee.update(employeeMap);
        });

        String expectedMessage = "SQL fields provided are invalid.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void find() {
        try {
            this.employee.find("Name", "Foo");
            verify(mockStatement, times(2)).executeUpdate(statementArgumentCaptor.capture());
        } catch (Exception ignored) {
        }

        String expectedSql = "SELECT * FROM Employee WHERE Name = Foo;";
        Assertions.assertEquals(statementArgumentCaptor.getValue(), expectedSql);
    }

    @Test
    void find_invalid_fields() {
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            this.employee.find("Foo", "Bar");
        });

        String expectedMessage = "SQL fields provided are invalid.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
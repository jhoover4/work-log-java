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

class TaskTest {
    Task task;

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
            this.task = new Task(mockConnection);
        } catch (SQLException ignored) {
        }
    }

    @Test
    void createTable() {
        try {
            verify(mockStatement).executeUpdate(statementArgumentCaptor.capture());

            String expectedSql = "CREATE TABLE IF NOT EXISTS Task(id INTEGER PRIMARY KEY, notes TEXT, task_date INTEGER, " +
                    "title TEXT, time_spent INTEGER, employee_id INTEGER, FOREIGN KEY(employee_id) REFERENCES Employee(id));";
            Assertions.assertEquals(statementArgumentCaptor.getValue(), expectedSql);
        } catch (SQLException ignored) {
        }
    }

    @Test
    void create() {
        HashMap<String, String> employeeMap = new HashMap<>();
        employeeMap.put("task_date", "1/1/2020");
        employeeMap.put("title", "Test");
        employeeMap.put("time_spent", "1");
        employeeMap.put("notes", "FooFooFoo");

        try {
            this.task.create(employeeMap);
            verify(mockStatement, times(2)).executeUpdate(statementArgumentCaptor.capture());
        } catch (Exception ignored) {
        }

        String expectedSql = "INSERT INTO Task (notes, task_date, title, time_spent) VALUES (FooFooFoo, 1/1/2020, Test, 1);";
        Assertions.assertEquals(statementArgumentCaptor.getValue(), expectedSql);
    }

    @Test
    void create_invalid_fields() {
        HashMap<String, String> taskMap = new HashMap<>();
        taskMap.put("Foo", "Bar");

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            this.task.create(taskMap);
        });

        String expectedMessage = "SQL fields provided are invalid.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        HashMap<String, String> employeeMap = new HashMap<>();
        employeeMap.put("task_date", "1/1/2019");
        employeeMap.put("title", "Test2");
        employeeMap.put("time_spent", "2");
        employeeMap.put("notes", "BarBarBar");

        try {
            this.task.update(employeeMap);
            verify(mockStatement, times(2)).executeUpdate(statementArgumentCaptor.capture());
        } catch (Exception ignored) {
        }

        String expectedSql = "UPDATE Task SET notes = BarBarBar, task_date = 1/1/2019, title = Test2, time_spent = 2;";
        Assertions.assertEquals(statementArgumentCaptor.getValue(), expectedSql);
    }

    @Test
    void update_invalid_fields() {
        HashMap<String, String> taskMap = new HashMap<>();
        taskMap.put("Foo", "Bar");

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            this.task.update(taskMap);
        });

        String expectedMessage = "SQL fields provided are invalid.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void find() {
        try {
            this.task.find("title", "Test");
            verify(mockStatement, times(2)).executeUpdate(statementArgumentCaptor.capture());
        } catch (Exception ignored) {
        }

        String expectedSql = "SELECT * FROM Task WHERE title = Test;";
        Assertions.assertEquals(statementArgumentCaptor.getValue(), expectedSql);
    }

    @Test
    void find_invalid_fields() {
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            this.task.find("Foo", "Bar");
        });

        String expectedMessage = "SQL fields provided are invalid.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
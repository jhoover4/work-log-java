package com.company.worklog;

import picocli.CommandLine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Callable;

@CommandLine.Command(description = "Main worklog command",
        name = "worklog", mixinStandardHelpOptions = true, subcommands = AddTask.class)
public class Main implements Callable<Integer> {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:worklog.db";

        try {
            Connection conn = DriverManager.getConnection(url);
            Employee employee = new Employee(conn);
            Task task = new Task(conn);

            int exitCode = new CommandLine(new Main()).addSubcommand("add", new AddTask(task)).execute(args);
            System.exit(exitCode);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override public Integer call() {
        return 1;
    }
}

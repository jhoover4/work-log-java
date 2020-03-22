package com.company.worklog;

import picocli.CommandLine;

import java.util.HashMap;
import java.util.concurrent.Callable;

//The script should ask for a task name, how much time was spent on the task, and any general notes about the task.
// Record each of these items into a row of a CSV file along with a date.

@CommandLine.Command(description = "Asks user to add a new task to database.",
        name = "add", mixinStandardHelpOptions = true)
class AddTask implements Callable<Integer> {

    @CommandLine.Option(names = {"-n", "--task_name"}, description = "Task Name", interactive = true)
    private String taskName;

    @CommandLine.Option(names = {"-d", "--date"}, description = "Date task was completed", interactive = true)
    private String taskDate;

    @CommandLine.Option(names = {"-t", "--time"}, description = "Time Spent on the task (in minutes)", interactive = true)
    private String timeSpent;

    @CommandLine.Option(names = {"-N", "--notes"}, description = "Any notes about the task", interactive = true)
    private String notes;

    private Task task;

//    public static void main(String... args) throws Exception {
//        int exitCode = new CommandLine(new AddTask()).execute(args);
//        System.exit(exitCode);
//    }

    AddTask(Task task) {
        this.task = task;
    }

    @Override
    public Integer call() throws Exception {
        HashMap<String, String> employeeMap = new HashMap<String, String>();
        employeeMap.put("task_date", taskDate);
        employeeMap.put("title", taskName);
        employeeMap.put("time_spent", timeSpent);
        employeeMap.put("notes", notes);
        this.task.create(employeeMap);

        System.out.println("Task added successfully!");
        return 0;
    }
}
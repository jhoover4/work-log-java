package com.company.worklog;

import com.company.worklog.models.Employee;
import com.company.worklog.models.Task;
import com.company.worklog.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;


@Component
@Command(name = "worklog", mixinStandardHelpOptions = true, subcommands = WorkLogCommand.AddTask.class)
public class WorkLogCommand implements Callable<Integer> {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Integer call() {
        System.out.print("mycommand was called.");
        return 23;
    }

    @CommandLine.Command(description = "Asks user to add a new task to database.",
            name = "add", mixinStandardHelpOptions = true)
    class AddTask implements Callable<Integer> {

        @CommandLine.Option(names = {"-e", "--employee"}, description = "Employee name", interactive = true)
        private String employeeName;

        @CommandLine.Option(names = {"-n", "--task_name"}, description = "Task name", interactive = true)
        private String taskName;

        @CommandLine.Option(names = {"-d", "--date"}, description = "Date task was completed", interactive = true)
        private String taskDate;

        @CommandLine.Option(names = {"-t", "--time"}, description = "Time Spent on the task (in minutes)", interactive = true)
        private String timeSpent;

        @CommandLine.Option(names = {"-N", "--notes"}, description = "Any notes about the task", interactive = true)
        private String notes;

        private Task task;

        AddTask(Task task) {
            this.task = task;
        }

        @Override
        public Integer call() throws Exception {
            Employee employee = employeeRepository.findByName(employeeName).get(0);

            Task newTask = new Task(taskName, Integer.parseInt(taskDate), Integer.parseInt(timeSpent), notes, employee);

            System.out.println("Task added successfully!");
            return 0;
        }
    }

    @CommandLine.Command(description = "Lets user find a list of tasks.",
            name = "list", mixinStandardHelpOptions = true)
    class ListTask implements Callable<Integer> {

        @CommandLine.Option(names = {"-s", "--search"}, description = "Return tasks with search term in title.", interactive = true)
        private String searchTerm;

        @CommandLine.Option(names = {"-d", "--date"}, description = "Date task was completed", interactive = true)
        private String taskDate;

        @CommandLine.Option(names = {"-e", "--employee"}, description = "Time Spent on the task (in minutes)", interactive = true)
        private String taskEmployee;

        private Task task;

        ListTask(Task task) {
            this.task = task;
        }

        @Override
        public Integer call() throws Exception {
//            this.task.create(employeeMap);

            System.out.println("Task added successfully!");
            return 0;
        }
    }
}
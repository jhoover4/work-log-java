package com.company.worklog;

import picocli.CommandLine;
import picocli.CommandLine.IFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

@Component
public class WorkLogRunner implements CommandLineRunner, ExitCodeGenerator {

    private final WorkLogCommand workLogCommand;

    private final IFactory factory; // auto-configured to inject PicocliSpringFactory

    private int exitCode;

    public WorkLogRunner(WorkLogCommand workLogCommand, IFactory factory) {
        this.workLogCommand = workLogCommand;
        this.factory = factory;
    }

    @Override
    public void run(String... args) throws Exception {
        exitCode = new CommandLine(workLogCommand, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
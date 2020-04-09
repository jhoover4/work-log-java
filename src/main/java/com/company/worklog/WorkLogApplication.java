package com.company.worklog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkLogApplication {

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(WorkLogApplication.class, args)));
    }

}

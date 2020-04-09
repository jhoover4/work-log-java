package com.company.worklog.models;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private int timeSpent;
    private int taskDate;
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    protected Task() {
    }

    public Task(String title, int timeSpent, int taskDate, String notes, Employee employee) {
        this.title = title;
        this.timeSpent = timeSpent;
        this.taskDate = taskDate;
        this.notes = notes;
        this.employee = employee;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, title='%s', timeSpent='%d', taskDate='%d', notes='%s', employee='%s']",
                id, title, timeSpent, taskDate, notes, employee.getName());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public void setTaskDate(int taskDate) {
        this.taskDate = taskDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public int getTaskDate() {
        return taskDate;
    }

    public String getNotes() {
        return notes;
    }
}
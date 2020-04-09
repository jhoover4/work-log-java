package com.company.worklog.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Task> tasks;

    protected Employee() {}

    public Employee(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, name='%s']",
                id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
package com.company.worklog.repositories;

import com.company.worklog.models.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    List<Employee> findByName(String lastName);

    Employee findById(long id);
}
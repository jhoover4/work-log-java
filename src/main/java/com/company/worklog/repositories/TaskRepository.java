package com.company.worklog.repositories;

import com.company.worklog.models.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

    Task findById(long id);
}
package org.unibl.etf.todo.repo;

import org.unibl.etf.todo.domain.Task;
import org.unibl.etf.todo.dto.TaskCreateDto;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> getTasks(Integer categoryId);

    Optional<Task> addTask(Integer categoryId, TaskCreateDto taskCreateDto);
}

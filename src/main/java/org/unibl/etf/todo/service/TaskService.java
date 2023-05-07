package org.unibl.etf.todo.service;

import org.unibl.etf.todo.dto.TaskCreateDto;
import org.unibl.etf.todo.dto.TaskReadDto;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<TaskReadDto> getTasks(Integer categoryId);

    Optional<TaskReadDto> addTask(Integer categoryId, TaskCreateDto taskCreateDto);
}

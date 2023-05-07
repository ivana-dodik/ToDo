package org.unibl.etf.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.todo.dto.CategoryCreateDto;
import org.unibl.etf.todo.dto.CategoryReadDto;
import org.unibl.etf.todo.dto.TaskCreateDto;
import org.unibl.etf.todo.dto.TaskReadDto;
import org.unibl.etf.todo.service.TaskService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/categories/{categoryId}/tasks")
    public ResponseEntity<List<TaskReadDto>> getTasks(@PathVariable("categoryId") Integer categoryId) {
        return ResponseEntity.ok(taskService.getTasks(categoryId));
    }

    @PostMapping("/categories/{categoryId}/tasks")
    public ResponseEntity<TaskReadDto> addTasks(
            @PathVariable("categoryId") Integer categoryId, @RequestBody TaskCreateDto taskCreateDto) {
        Optional<TaskReadDto> task = taskService.addTask(categoryId, taskCreateDto);

        if (task.isPresent()) {
            return ResponseEntity.ok(task.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

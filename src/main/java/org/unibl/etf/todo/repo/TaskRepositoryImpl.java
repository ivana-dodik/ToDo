package org.unibl.etf.todo.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.unibl.etf.todo.domain.Priority;
import org.unibl.etf.todo.domain.Task;
import org.unibl.etf.todo.dto.TaskCreateDto;
import org.unibl.etf.todo.dto.TaskUpdateDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Task> getTasks(Integer categoryId) {
        String sql = "SELECT * FROM task WHERE category_id = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), categoryId);
    }

    @Override
    public Optional<Task> addTask(Integer categoryId, TaskCreateDto taskCreateDto) {
        String sql = "INSERT INTO task (category_id, name, priority, due) VALUES (?, ?, ?, ?)";
        int rowsInserted = jdbcTemplate.update(sql, categoryId, taskCreateDto.getName(),
                taskCreateDto.getPriority().name(), taskCreateDto.getDue());
        if (rowsInserted > 0) {
            int taskId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            return Optional.of(new Task(taskId, taskCreateDto.getName(), taskCreateDto.getDue(),
                    taskCreateDto.getPriority(), false));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteTask(Integer taskId) {
        String sql = "DELETE FROM task WHERE task_id = ?";
        jdbcTemplate.update(sql, taskId);
    }

    @Override
    public Optional<Task> editTask(int taskId, TaskUpdateDto taskUpdateDto, Task originalTask) {
        /*
        @Override
public Optional<Task> editTask(int taskId, TaskUpdateDto taskUpdateDto) {
    String newName = taskUpdateDto.name();
    LocalDateTime newDue = taskUpdateDto.due();
    Priority newPriority = taskUpdateDto.priority();
    boolean newCompleted = taskUpdateDto.completed();

    // Build the SQL query based on the non-null fields
    StringBuilder sqlBuilder = new StringBuilder("UPDATE task SET ");
    List<Object> params = new ArrayList<>();
    if (newName != null) {
        sqlBuilder.append("name = ?, ");
        params.add(newName);
    }
    if (newDue != null) {
        sqlBuilder.append("due = ?, ");
        params.add(newDue);
    }
    if (newPriority != null) {
        sqlBuilder.append("priority = ?, ");
        params.add(newPriority.name());
    }
    if (newCompleted != null) {
        sqlBuilder.append("completed = ?, ");
        params.add(newCompleted);
    }
    // Remove the trailing comma and space
    sqlBuilder.setLength(sqlBuilder.length() - 2);
    sqlBuilder.append(" WHERE task_id = ?");
    params.add(taskId);

    int rowsAffected = jdbcTemplate.update(sqlBuilder.toString(), params.toArray());

    if (rowsAffected == 0) {
        return Optional.empty();
    } else {
        // Fetch the updated task from the database and return it
        String selectSql = "SELECT * FROM task WHERE task_id = ?";
        Task task = jdbcTemplate.queryForObject(selectSql, new Object[]{taskId}, new TaskRowMapper());
        return Optional.of(task);
    }

         */
//        String newName = taskUpdateDto.name();
//        LocalDateTime newDue = taskUpdateDto.due();
//        Priority newPriority = taskUpdateDto.priority();
//        boolean newCompleted = taskUpdateDto.completed();
//        String sql = "UPDATE task SET name = ?, due = ?, priority = ?, completed = ? WHERE task_id = ?";
//        int rowsAffected = jdbcTemplate.update(sql, newName, newDue, newPriority.name(), newCompleted, taskId);
//        if (rowsAffected == 0) {
//            return Optional.empty();
//        } else {
//            Task updateTask = new Task(taskId, newName, newDue, newPriority, newCompleted);
//            return Optional.of(updateTask);
//        }

        String newName = taskUpdateDto.name();
        LocalDateTime newDue = taskUpdateDto.due();
        Priority newPriority = taskUpdateDto.priority();
        boolean newCompleted = taskUpdateDto.completed();

        // Build the SQL query based on the non-null fields
        StringBuilder sqlBuilder = new StringBuilder("UPDATE task SET ");
        List<Object> params = new ArrayList<>();

        if (newName != null && !newName.equals(originalTask.getName())) {
            sqlBuilder.append("name = ?, ");
            params.add(newName);
            originalTask.setName(newName);
        }
        if (newDue != null && !newDue.equals(originalTask.getDue())) {
            sqlBuilder.append("due = ?, ");
            params.add(newDue);
            originalTask.setDue(newDue);
        }
        if (newPriority != null && !newPriority.equals(originalTask.getPriority())) {
            sqlBuilder.append("priority = ?, ");
            params.add(newPriority.name());
            originalTask.setPriority(newPriority);
        }
        if (newCompleted != originalTask.isCompleted()) {
            sqlBuilder.append("completed = ?, ");
            params.add(newCompleted);
            originalTask.setCompleted(newCompleted);
        }

        if (params.isEmpty()) {
            return Optional.of(originalTask);
        }

        // Remove the trailing comma and space
        sqlBuilder.setLength(sqlBuilder.length() - 2);
        sqlBuilder.append(" WHERE task_id = ?");
        params.add(taskId);

        int rowsAffected = jdbcTemplate.update(sqlBuilder.toString(), params.toArray());

        if (rowsAffected == 0) {
            return Optional.empty();
        } else {
            return Optional.of(originalTask);
        }
    }

    private static class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            int id = rs.getInt("task_id");
            String name = rs.getString("name");
            Timestamp timestamp = rs.getTimestamp("due");
            LocalDateTime due = null;
            if (timestamp != null) {
                due = timestamp.toLocalDateTime();
            }
            Priority priority = Priority.valueOf(rs.getString("priority"));
            boolean completed = rs.getBoolean("completed");
            return new Task(id, name, due, priority, completed);
        }
    }

    @Override
    public Optional<Task> getTaskById(int taskId) {
        String sql = "SELECT * FROM task WHERE task_id = ?";
        try {
            Task task = jdbcTemplate.queryForObject(sql, new TaskRowMapper(), taskId);
            return Optional.ofNullable(task);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

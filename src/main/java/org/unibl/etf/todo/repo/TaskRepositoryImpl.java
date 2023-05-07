package org.unibl.etf.todo.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.unibl.etf.todo.domain.Priority;
import org.unibl.etf.todo.domain.Task;
import org.unibl.etf.todo.dto.TaskCreateDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    public Optional<Task> addTask(Integer categoryId, TaskCreateDto taskCreateDto) {
        // TODO: If no priority got picked, pick default
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
}

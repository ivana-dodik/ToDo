package org.unibl.etf.todo.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.unibl.etf.todo.domain.Category;
import org.unibl.etf.todo.dto.CategoryCreateDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Category> getCategories() {
        String sql = "SELECT * FROM category";

        return jdbcTemplate.query(sql, new CategoryRowMapper());
    }

    @Override
    public Category addCategory(CategoryCreateDto category) {
        String sql = "INSERT INTO Category (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, category.name());
            return ps;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        return new Category(id, category.name(), null);
    }

    private static class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            return  new Category(rs.getInt("id"), rs.getString("name"), null);
        }
    }
}

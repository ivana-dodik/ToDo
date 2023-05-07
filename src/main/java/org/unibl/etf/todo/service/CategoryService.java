package org.unibl.etf.todo.service;

import org.unibl.etf.todo.dto.CategoryCreateDto;
import org.unibl.etf.todo.dto.CategoryReadDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryReadDto> getCategories();

    CategoryReadDto addCategory(CategoryCreateDto category);

    Optional<CategoryReadDto> getCategory(Integer categoryId);

    void deleteCategory(Integer categoryId);
}

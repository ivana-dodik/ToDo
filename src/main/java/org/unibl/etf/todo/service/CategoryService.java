package org.unibl.etf.todo.service;

import org.unibl.etf.todo.dto.CategoryCreateDto;
import org.unibl.etf.todo.dto.CategoryReadDto;

import java.util.List;

public interface CategoryService {
    List<CategoryReadDto> getCategories();

    CategoryReadDto addCategory(CategoryCreateDto category);

    CategoryReadDto getCategory(Integer categoryId);
}

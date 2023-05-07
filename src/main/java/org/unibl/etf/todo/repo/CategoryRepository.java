package org.unibl.etf.todo.repo;

import org.unibl.etf.todo.domain.Category;
import org.unibl.etf.todo.dto.CategoryCreateDto;

import java.util.List;

public interface CategoryRepository {
    List<Category> getCategories();

    Category addCategory(CategoryCreateDto category);

    Category getCategory(Integer categoryId);
}

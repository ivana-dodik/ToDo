package org.unibl.etf.todo.mapper;

import org.springframework.stereotype.Component;
import org.unibl.etf.todo.domain.Category;
import org.unibl.etf.todo.dto.CategoryReadDto;

import java.util.List;

@Component
public class CategoryMapper {

    public CategoryReadDto toDto(Category category) {
        CategoryReadDto categoryReadDto = new CategoryReadDto();
        categoryReadDto.setId(category.id());
        categoryReadDto.setName(category.name());
        return categoryReadDto;
    }

    public List<CategoryReadDto> toDtoList(List<Category> categories) {
        return categories.stream().map(this::toDto).toList();
    }

}


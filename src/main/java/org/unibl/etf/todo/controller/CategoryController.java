package org.unibl.etf.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.todo.dto.CategoryCreateDto;
import org.unibl.etf.todo.dto.CategoryReadDto;
import org.unibl.etf.todo.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryReadDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryReadDto> addCategory(@RequestBody CategoryCreateDto category) {
        return ResponseEntity.ok(categoryService.addCategory(category));
    }

}

package com.blogapp.blog_platform_backend.domain.controllers;

import com.blogapp.blog_platform_backend.domain.dtos.CategoryDto;
import com.blogapp.blog_platform_backend.domain.dtos.CreateCategoryRequest;
import com.blogapp.blog_platform_backend.domain.entities.Category;
import com.blogapp.blog_platform_backend.domain.mappers.CategoryMapper;
import com.blogapp.blog_platform_backend.domain.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories(){
        List<CategoryDto> categories = categoryService.listCategories()
                .stream().map(categoryMapper::toDto)
                .toList();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest){
       Category category = categoryMapper.toEntity(createCategoryRequest);
       Category savedCategory = categoryService.createCategory(category);
       return new ResponseEntity<>(
               categoryMapper.toDto(savedCategory),
               HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

package com.tastytown.backend.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tastytown.backend.dto.CategoryRequestDTO;
import com.tastytown.backend.entity.Category;
import com.tastytown.backend.service.ICategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Category API", description = "This controller manages crud operations for category entities")
@CrossOrigin(
        origins = {
            "http://localhost:5173",
            "http://127.0.0.1:5173"
        }
    )
public class CategoryController {

    private final ICategoryService categoryService;
    
    @PostMapping
    @ApiResponse(responseCode = "201", description = "Category Created") //Swagger Annotation
    @Operation(summary = "Create a new category")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequestDTO requestDTO){
        //return savedCategory = categoryService.saveCategory(requestDTO);
        //return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);

        return new ResponseEntity<>(categoryService.saveCategory(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all categories")
    @ApiResponse(responseCode = "200", description = "Fetched  all categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Get category by id")
    @ApiResponse(description = "Extract category by id ")
    public ResponseEntity<Category> getCategoryById(@PathVariable String categoryId){
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete category by id")
    @ApiResponse(responseCode = "402", description = "Delete category by id ")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable String categoryId){
        categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update category by id")
    @ApiResponse(responseCode = "201", description = "Update category by id ")
    public ResponseEntity<Category> updateCategory(@PathVariable String categoryId, @RequestBody CategoryRequestDTO requestDTO){
        //return new ResponseEntity<>(categoryService.updateCategoryById(categoryId, requestDTO), HttpStatus.OK);
        return ResponseEntity.ok(categoryService.updateCategoryById(categoryId, requestDTO));
    }
}

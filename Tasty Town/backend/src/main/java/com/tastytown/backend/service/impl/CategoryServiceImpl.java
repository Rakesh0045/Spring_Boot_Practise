package com.tastytown.backend.service.impl;



import java.util.List;

import org.springframework.stereotype.Service;

import com.tastytown.backend.dto.CategoryRequestDTO;
import com.tastytown.backend.entity.Category;
import com.tastytown.backend.exception.CategoryNotFoundException;
import com.tastytown.backend.repository.CategoryRepository;
import com.tastytown.backend.service.ICategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(CategoryRequestDTO requestDTO) {
        var category = Category.builder()
                .categoryName(requestDTO.getCategoryName())
                .build();

        return categoryRepository.save(category);
    }    

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow( () -> new CategoryNotFoundException("Category not found with id : "+categoryId));
    }

    @Override
    public void deleteCategoryById(String categoryId) {
        var category = getCategoryById(categoryId);
        category.setCategoryId(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public Category updateCategoryById(String categoryId, CategoryRequestDTO requestDTO) {
        var existingCategory =  getCategoryById(categoryId); 
        //check if id exists or not, if not exists it will hit exception
        
        existingCategory.setCategoryName(requestDTO.getCategoryName());
        return categoryRepository.save(existingCategory);
    }

}

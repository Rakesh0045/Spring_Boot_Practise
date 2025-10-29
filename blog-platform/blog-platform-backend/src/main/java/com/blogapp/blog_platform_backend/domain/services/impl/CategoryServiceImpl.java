package com.blogapp.blog_platform_backend.domain.services.impl;

import com.blogapp.blog_platform_backend.domain.entities.Category;
import com.blogapp.blog_platform_backend.domain.repositories.CategoryRepository;
import com.blogapp.blog_platform_backend.domain.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    @Override
    public Category createCategory(Category category) {
        if(categoryRepository.existsByNameIgnoreCase(category.getName())){
            throw new IllegalArgumentException("Category already exists with name: "+category.getName());
        }
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public void deleteCategory(UUID id) {
        Category category = getCategoryById(id);

        // Check if category has associated posts
        if (!category.getPosts().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete category: " + category.getName() + ". It has associated posts.");
        }
        categoryRepository.delete(category);
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Category not found with id: " + id));
    }

}

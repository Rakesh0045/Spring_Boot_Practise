package com.tastytown.backend.service;

import java.util.List;

import com.tastytown.backend.dto.CategoryRequestDTO;
import com.tastytown.backend.entity.Category;

public interface ICategoryService {
    /**
     *  <h3> It creates a Category object by category name </h3>
     */
    Category saveCategory(CategoryRequestDTO requestDTO);

    /**
     *  <h3> It returns all the available Category as a List </h3>
     */
    List<Category> getAllCategories();

    /**
     *  <h3> It returns a category by its id </h3>
     */
    Category getCategoryById(String categoryId);

    /**
     *  <h3> It deletes a category by its id </h3>
     */
    void deleteCategoryById(String categoryId);


    /**
     *  <h3> It updates a Category by its id </h3>
     */
     Category updateCategoryById(String categoryId, CategoryRequestDTO requestDTO);

}

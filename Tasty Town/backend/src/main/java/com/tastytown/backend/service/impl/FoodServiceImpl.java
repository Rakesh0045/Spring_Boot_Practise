package com.tastytown.backend.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tastytown.backend.dto.FoodRequestDTO;
import com.tastytown.backend.dto.FoodResponseDTO;

import com.tastytown.backend.entity.Food;
import com.tastytown.backend.helper.FileServiceHelper;
import com.tastytown.backend.helper.FoodServiceHelper;
import com.tastytown.backend.mapper.FoodMapper;
import com.tastytown.backend.repository.FoodRepository;
import com.tastytown.backend.service.ICategoryService;
import com.tastytown.backend.service.IFoodService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements IFoodService {

    private final ICategoryService categoryService;
    private final FoodRepository foodRepository;
    private final FileServiceHelper fileServiceHelper;
    private final FoodServiceHelper foodServiceHelper;

    @Value("${upload.file.dir}") // Spring Expression Language
	private String FILE_DIR;

    @Override
    public FoodResponseDTO createFood(FoodRequestDTO requestDTO, MultipartFile foodImage) throws IOException {
        // Check category exist or not
        var existingCategory = categoryService.getCategoryById(requestDTO.categoryId());

        // save the image in the folder
        var fileName = fileServiceHelper.uploadFile(foodImage);
        
        // Save food in DB
        var food = FoodMapper.convertToEntity(requestDTO, existingCategory, fileName);
        var savedFood = foodRepository.save(food);

        // Return the FoodResponseDTO
        var foodResponseDTO = FoodMapper.convertToDTO(savedFood);
        return foodResponseDTO;

    }

    


    @Override
    public List<FoodResponseDTO> getAllFoods(){
        var foods = foodRepository.findAll();
       
        //return foods.stream().map((food) -> FoodMapper.convertToDTO(food)).toList();

        return foods.stream().map(FoodMapper :: convertToDTO).toList();
    } 

    @Override
    public FoodResponseDTO getFoodById(String foodId){
        var food = foodServiceHelper.getFoodById(foodId);
        return FoodMapper.convertToDTO(food);
    }


    @Override
    public FoodResponseDTO deleteFoodById(String foodId) throws IOException{

        // check food exists or not
        Food food = foodServiceHelper.getFoodById(foodId);

        fileServiceHelper.deleteFoodImage(food.getFoodImage());

        foodRepository.delete(food);

        return FoodMapper.convertToDTO(food);

    }

    

    @Override
    public FoodResponseDTO updateFoodById(String foodId, FoodRequestDTO requestDTO, MultipartFile foodImage) throws IOException {

        // check food exists or not
        Food existingFood = foodServiceHelper.getFoodById(foodId);

        
        
        //update fields
        existingFood.setFoodName(requestDTO.foodName());
        existingFood.setFoodDescription(requestDTO.foodDescription());
        existingFood.setFoodPrice(requestDTO.foodPrice());

        //check category exists or not & then update category
        if(requestDTO.categoryId() != null && !requestDTO.categoryId().isEmpty()){
            var category = categoryService.getCategoryById(requestDTO.categoryId());
            existingFood.setCategory(category);
        }
           
        // update image if exists
        if(foodImage != null && !foodImage.isEmpty()){

            // Delete existing food image
            fileServiceHelper.deleteFoodImage(existingFood.getFoodImage());

            // Save new Image
            var newFoodImageName = fileServiceHelper.uploadFile(foodImage);
            existingFood.setFoodName(newFoodImageName);
        }

        var savedFood = foodRepository.save(existingFood);
        return FoodMapper.convertToDTO(savedFood);
    }

    @Override
    public Page<FoodResponseDTO> getPaginatedFoods(int pageNumber, int pageSize, String categoryId, String search){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        
        //filteration
        Page<Food> foodPage;

        if(!categoryId.equals("all") && !search.equals("all")){
            foodPage = foodRepository.findByCategory_CategoryIdAndFoodNameContainingIgnoreCase(categoryId, search, pageable);
        }else if(!categoryId.equals("all")){
            foodPage = foodRepository.findByCategory_CategoryId(categoryId, pageable);
        }else if(!search.equals("all")){
            foodPage = foodRepository.findByFoodNameContainingIgnoreCase(search, pageable);
        }
        else{
            foodPage = foodRepository.findAll(pageable);
        }
        
        Page<Food> foods = foodRepository.findAll(pageable);
        return foodPage.map(FoodMapper :: convertToDTO);
    }
    
}

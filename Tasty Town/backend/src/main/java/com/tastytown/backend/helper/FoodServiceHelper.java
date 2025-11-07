package com.tastytown.backend.helper;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Component;

import com.tastytown.backend.entity.Food;
import com.tastytown.backend.repository.FoodRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FoodServiceHelper {
    
    private final FoodRepository foodRepository;
    
    public Food getFoodById(String foodId){
        return foodRepository.findById(foodId).orElseThrow(() -> new NoSuchElementException("Food not found with id: "+foodId));
    }

}
